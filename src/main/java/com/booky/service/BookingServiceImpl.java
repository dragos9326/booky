package com.booky.service;

import com.booky.exceptions.GeneralException;
import com.booky.mapper.BookingMapper;
import com.booky.model.dto.BookingDTO;
import com.booky.model.dto.BookingRq;
import com.booky.model.dto.GeneralResponse;
import com.booky.model.entity.*;
import com.booky.repository.BookingRepository;
import com.booky.repository.PropertyRepository;
import com.booky.repository.UserRepository;
import com.booky.service.interfaces.IBookingService;
import com.booky.utils.Errors;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author dragos
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements IBookingService {

    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDTO findBlockById(UUID id) {
        return BookingMapper.INSTANCE.map(findById(id, false));
    }

    @Override
    public BookingDTO findBookingById(UUID id) {
        return BookingMapper.INSTANCE.map(findById(id, true));
    }

    public Booking findById(UUID id, boolean isBooking) {
        String notFoundMessage = isBooking ? "No booking found" : "No block found";

        Optional<Booking> optBooking = bookingRepository.findById(id);
        if (optBooking.isPresent()) {
            Booking booking = optBooking.get();
            if (isBooking && !BookingTypeEnum.BOOK.name().equalsIgnoreCase(booking.getType())) {
                throw new EntityNotFoundException(notFoundMessage);
            }
            return booking;
        } else {
            throw new EntityNotFoundException(notFoundMessage);
        }
    }

    @Override
    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAllByType(BookingTypeEnum.BOOK.name()).stream().map(BookingMapper.INSTANCE::map).toList();
    }

    @Override
    public List<BookingDTO> getAllBlocksByProperty(Long propertyId) {
        return bookingRepository.findAllByTypeAndPropertyId(BookingTypeEnum.BLOCK.name(), propertyId).stream().map(BookingMapper.INSTANCE::map).toList();
    }

    /**
     * @param req booking request to be saved
     * @param username username of the requesting user
     * @return {@link BookingDTO}
     */
    @Override
    public BookingDTO saveBooking(BookingRq req, String username) {
        checkBookingRequest(req);

        Optional<Booking> bookOpt = getOverlappingBooking(req);
        if (bookOpt.isPresent()) {
            throw new GeneralException("Your booking overlaps with another booking");
        }
        Optional<Booking> blockOpt = getOverlappingBlock(req);

        if (blockOpt.isPresent()) {
            throw new GeneralException("You cannot book in this period due to a block");
        }

        Booking booking = BookingMapper.INSTANCE.mapCreateReq(req);

        //check user and set it on booking
        Optional<User> userOpt = userRepository.findByEmail(username);
        User user = userOpt.orElseThrow(() -> new UsernameNotFoundException(Errors.USERNAME_NOT_FOUND));
        booking.setUser(user);

        //search and set property
        Long propertyId = req.getPropertyId();
        Optional<Property> propertyOpt = propertyRepository.findById(propertyId);
        Property property = propertyOpt.orElseThrow(() -> new EntityNotFoundException(Errors.NO_PROPERTY_FOR_ID));
        booking.setProperty(property);

        //set last update by
        booking.setLastUpdateBy(username);

        booking.setType(BookingTypeEnum.BOOK.name());

        booking = bookingRepository.save(booking);
        return BookingMapper.INSTANCE.map(booking);
    }

    /**
     * NORMAL users can delete their bookings,
     * OWNER users can delete bookings for their properties
     * ADMIN users can delete any booking
     *
     * @param bookingId booking id to delete
     * @param username username of the requesting user
     * @return {@link GeneralResponse}
     */
    @Override
    public GeneralResponse deleteBooking(UUID bookingId, String username, boolean isBooking) {
        Booking booking = findById(bookingId, isBooking);

        checkIfUserCanModifyBooking(booking, username, false);

        bookingRepository.delete(booking);
        return new GeneralResponse(0, "Booking deleted successfully", LocalDateTime.now());
    }


    /**
     * Saves a block, or it updates the existing one if it is overlapping with another block
     *
     * @param req the BookingRq object to be saved
     * @param username username of the requesting user
     * @return {@link BookingDTO}
     */
    @Override
    public BookingDTO saveBlock(BookingRq req, String username) {
        checkBookingRequest(req);

        Optional<Booking> bookOpt = getOverlappingBooking(req);
        if (bookOpt.isPresent()) {
            throw new GeneralException("Your booking overlaps with another booking");
        }

        Optional<Booking> blockOpt = getOverlappingBlock(req);
        if (blockOpt.isPresent()) {
            Booking existingBooking = blockOpt.get();
            BookingDTO dto = manageSaveOverlappingBlock(req, existingBooking);
            if (dto != null) {
                return dto;
            }
        }

        Booking booking = BookingMapper.INSTANCE.mapCreateReq(req);

        //check user and set it on booking
        Optional<User> userOpt = userRepository.findByEmail(username);
        User user = userOpt.orElseThrow(() -> new UsernameNotFoundException(Errors.USERNAME_NOT_FOUND));
        booking.setUser(user);

        //search and set property
        Long propertyId = req.getPropertyId();
        Optional<Property> propertyOpt = propertyRepository.findById(propertyId);
        Property property = propertyOpt.orElseThrow(() -> new EntityNotFoundException(Errors.NO_PROPERTY_FOR_ID));
        booking.setProperty(property);

        //set last update by
        booking.setLastUpdateBy(username);

        booking.setType(BookingTypeEnum.BLOCK.name());

        booking = bookingRepository.save(booking);
        return BookingMapper.INSTANCE.map(booking);
    }


    /**
     * NORMAL users can update their bookings,
     * OWNER users can update bookings for their properties
     * ADMIN users can update any booking
     *
     * @param bookingId booking id to update
     * @param req new data to update
     * @param username  username of the requesting user
     * @return {@link BookingDTO}
     */
    @Override
    public BookingDTO updateBooking(UUID bookingId, BookingRq req, String username) {
        return updateBookingInternal(bookingId, req, username, true);
    }

    @Override
    public BookingDTO updateBlock(UUID bookingId, BookingRq req, String username) {
        return updateBookingInternal(bookingId, req, username, false);
    }

    public BookingDTO updateBookingInternal(UUID bookingId, BookingRq req, String username, boolean isBooking) {
        Booking booking = findById(bookingId, false);

        checkBookingRequest(req);
        checkIfUserCanModifyBooking(booking, username, true);

        Optional<Booking> bookOpt = getOverlappingBooking(req);
        if (bookOpt.isPresent()) {
            throw new GeneralException("Your " + (isBooking ? "booking" : "block") + " overlaps with another booking");
        }
        Optional<Booking> blockOpt = getOverlappingBlock(req);
        if (blockOpt.isPresent()) {
            if (isBooking) {
                throw new GeneralException("You cannot book in this period due to a block");
            } else {
                Booking existingBlock = blockOpt.get();
                BookingDTO dto = manageSaveOverlappingBlock(req, existingBlock);
                if (dto != null) {
                    return dto;
                }
            }
        }

        //search and set property
        Long propertyId = req.getPropertyId();
        Optional<Property> propertyOpt = propertyRepository.findById(propertyId);
        Property property = propertyOpt.orElseThrow(() -> new EntityNotFoundException(Errors.NO_PROPERTY_FOR_ID));
        booking.setProperty(property);

        //map check in and checkout dates
        booking.setCheckinDate(req.getCheckinDate());
        booking.setCheckoutDate(req.getCheckoutDate());

        //set last update by
        booking.setLastUpdateBy(username);
        booking = bookingRepository.save(booking);
        return BookingMapper.INSTANCE.map(booking);
    }

    public BookingDTO manageSaveOverlappingBlock(BookingRq req, Booking existingBooking) {
        //if it overlaps with a booking, throw error
        if (existingBooking.getType().equals(BookingTypeEnum.BOOK.name())) {
            throw new GeneralException("This period overlaps with a booking");
        } else if (existingBooking.getType().equals(BookingTypeEnum.BLOCK.name())) {
            //if it overlaps with a block, just enlarge the existing booking
            if (existingBooking.getCheckinDate().isAfter(req.getCheckinDate())) {
                existingBooking.setCheckinDate(req.getCheckinDate());
            }
            if (existingBooking.getCheckoutDate().isBefore(req.getCheckoutDate())) {
                existingBooking.setCheckoutDate(req.getCheckoutDate());
            }

            existingBooking = bookingRepository.save(existingBooking);
            return BookingMapper.INSTANCE.map(existingBooking);
        } else {
            throw new GeneralException("Invalid booking type");
        }
    }

    public Optional<Booking> getOverlappingBlock(BookingRq req) {
        return getOverlappingBooking(req, false);
    }

    public Optional<Booking> getOverlappingBooking(BookingRq req) {
        return getOverlappingBooking(req, true);
    }

    public Optional<Booking> getOverlappingBooking(BookingRq req, boolean isBooking) {
        if (req == null) {
            throw new GeneralException("Invalid Request");
        }

        Long propertyId = req.getPropertyId();
        String type = BookingTypeEnum.BOOK.name();
        if (!isBooking) {
            type = BookingTypeEnum.BLOCK.name();
        }

        List<Booking> futureBookings = bookingRepository.findAllByTypeAndPropertyIdAndCheckinDateAfterOrderByCheckinDateAsc(type, propertyId, LocalDate.now());

        if (CollectionUtils.isEmpty(futureBookings)) {
            return Optional.empty();
        }

        LocalDate checkInDate = req.getCheckinDate();
        LocalDate checkOutDate = req.getCheckoutDate();
        return futureBookings.stream().filter(b ->
                (hasOverlap(b, checkInDate, checkOutDate))).findFirst();
    }

    public boolean hasOverlap(Booking booking, LocalDate checkInDate, LocalDate checkOutDate) {
        return booking.getCheckoutDate().isAfter(checkInDate) && booking.getCheckinDate().isBefore(checkOutDate);
    }

    public void checkBookingRequest(BookingRq req) {
        if (req == null) {
            throw new GeneralException("Invalid Request");
        }

        if (req.getCheckinDate() == null || req.getCheckoutDate() == null) {
            throw new GeneralException("Invalid check in/checkout dates");
        }

        if (req.getCheckinDate().isBefore(LocalDate.now())) {
            throw new GeneralException("Check in date should be in the future");
        }

        if (req.getCheckinDate().isAfter(req.getCheckoutDate())) {
            throw new GeneralException("Check in date should be before checkout date");
        }

    }

    public void checkIfUserCanModifyBooking(Booking booking, String username, boolean update) {
        String cannotDeleteOrUpdate = "cannot" + (update ? "update" : "delete");
        //check user and set it on booking
        Optional<User> userOpt = userRepository.findByEmail(username);
        User user = userOpt.orElseThrow(() -> new UsernameNotFoundException(Errors.USERNAME_NOT_FOUND));
        List<Role> roleList = user.getRoleList();
        if (CollectionUtils.isEmpty(roleList)) {
            throw new GeneralException(cannotDeleteOrUpdate + " this booking");
        }

        List<RoleEnum> roles = roleList.stream().map(role -> RoleEnum.valueOf(role.getLabel())).toList();

        if (!roles.contains(RoleEnum.ROLE_ADMIN)) {
            //if it's not admin, it should own the property or the booking
            if (roles.contains(RoleEnum.ROLE_OWNER)) {
                //if its owner he should own to property or the booking
                if (!booking.getProperty().getAdmin().getEmail().equals(username)
                        && !booking.getUser().getEmail().equals(username)) {
                    throw new GeneralException(cannotDeleteOrUpdate + " booking of another user or property");
                }
            } else {
                //if it's not admin or owner, he should own the booking
                if (!booking.getUser().getEmail().equals(username)) {
                    throw new GeneralException(cannotDeleteOrUpdate + " booking of another user");
                }
            }
        }
    }
}
