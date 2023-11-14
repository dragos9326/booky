package com.booky.service;

import com.booky.exceptions.BookingOverlappingException;
import com.booky.exceptions.GeneralException;
import com.booky.mapper.BookingMapper;
import com.booky.model.dto.BookingDTO;
import com.booky.model.dto.BookingRq;
import com.booky.model.entity.*;
import com.booky.repository.BookingRepository;
import com.booky.repository.PropertyRepository;
import com.booky.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.hamcrest.MockitoHamcrest;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private UUID id;
    private Booking booking;
    private BookingDTO bookingDTO;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        booking = new Booking();
        booking.setId(id);
        booking.setType(BookingTypeEnum.BOOK.name());
        bookingDTO = BookingMapper.INSTANCE.map(booking);
    }

    @Test
    void testFindByIdWhenBookingFoundAndIsBookThenReturnBooking() {
        when(bookingRepository.findById(id)).thenReturn(Optional.of(booking));

        Booking result = bookingService.findById(id, true);

        assertEquals(booking, result);
        verify(bookingRepository, times(1)).findById(id);
    }

    @Test
    void testFindByIdWhenBookingFoundAndIsNotBookThenThrowException() {
        booking.setType(BookingTypeEnum.BLOCK.name());
        when(bookingRepository.findById(id)).thenReturn(Optional.of(booking));

        assertThrows(EntityNotFoundException.class, () -> bookingService.findById(id, true));
        verify(bookingRepository, times(1)).findById(id);
    }

    @Test
    void testFindByIdWhenBookingNotFoundThenThrowException() {
        when(bookingRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.findById(id, true));
        verify(bookingRepository, times(1)).findById(id);
    }

    @Test
    void testFindByIdWhenBlockFoundAndIsBlockThenReturnBlock() {
        booking.setType(BookingTypeEnum.BLOCK.name());
        when(bookingRepository.findById(id)).thenReturn(Optional.of(booking));

        Booking result = bookingService.findById(id, false);

        assertEquals(booking, result);
        verify(bookingRepository, times(1)).findById(id);
    }

    @Test
    void testSaveBookingWhenOverlappingBookingThenThrowGeneralException() {
        BookingRq req = new BookingRq(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 1L);
        String username = "test@test.com";
        User user = new User();
        user.setEmail(username);
        Booking overlappingBooking = new Booking();

        BookingServiceImpl bookingServSpy = spy(bookingService);
        doReturn(Optional.of(overlappingBooking)).when(bookingServSpy).getOverlappingBooking(any(BookingRq.class));

        // Act and Assert
        assertThrows(BookingOverlappingException.class, () -> bookingServSpy.saveBooking(req, username));
    }

    @Test
    void testSaveBookingWhenOverlappingBlockThenThrowGeneralException() {
        BookingRq req = new BookingRq(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 1L);
        String username = "test@test.com";
        User user = new User();
        user.setEmail(username);
        Property property = new Property();
        Booking overlappingBlock = new Booking();
        overlappingBlock.setType(BookingTypeEnum.BLOCK.name());

        BookingServiceImpl bookingServSpy = spy(bookingService);
        doReturn(Optional.empty()).when(bookingServSpy).getOverlappingBooking(any(BookingRq.class));
        doReturn(Optional.of(overlappingBlock)).when(bookingServSpy).getOverlappingBlock(any(BookingRq.class));

        assertThrows(BookingOverlappingException.class, () -> bookingServSpy.saveBooking(req, username));
    }

    @Test
    void testSaveBookingWhenUsernameInvalidThenThrowException() {
        BookingRq req = new BookingRq(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 1L);
        String username = "test@test.com";
        User user = new User();
        user.setEmail(username);

        BookingServiceImpl bookingServSpy = spy(bookingService);
        doReturn(Optional.empty()).when(bookingServSpy).getOverlappingBooking(any(BookingRq.class));
        doReturn(Optional.empty()).when(bookingServSpy).getOverlappingBlock(any(BookingRq.class));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> bookingServSpy.saveBooking(req, username));
    }

    @Test
    void testSaveBookingWhenPropertyInvalidThenThrowException() {
        BookingRq req = new BookingRq(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 1L);
        String username = "test@test.com";
        User user = new User();
        user.setEmail(username);
        Property property = new Property();

        BookingServiceImpl bookingServSpy = spy(bookingService);
        doReturn(Optional.empty()).when(bookingServSpy).getOverlappingBooking(any(BookingRq.class));
        doReturn(Optional.empty()).when(bookingServSpy).getOverlappingBlock(any(BookingRq.class));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(propertyRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingServSpy.saveBooking(req, username));
    }

    @Test
    void testSaveBookingWhenAllGoodThenSave() {
        BookingRq req = new BookingRq(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 1L);
        String username = "test@test.com";
        User user = new User();
        user.setEmail(username);
        Property property = new Property();

        BookingServiceImpl bookingServSpy = spy(bookingService);
        doReturn(Optional.empty()).when(bookingServSpy).getOverlappingBooking(any(BookingRq.class));
        doReturn(Optional.empty()).when(bookingServSpy).getOverlappingBlock(any(BookingRq.class));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(propertyRepository.findById(any())).thenReturn(Optional.of(property));

        BookingDTO result = bookingServSpy.saveBooking(req, username);

        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testDeleteBookingWhenUserCanModifyThenDelete() {
        String username = "test@test.com";
        BookingServiceImpl bookingServSpy = spy(bookingService);
        doReturn(booking).when(bookingServSpy).findById(any(UUID.class), anyBoolean());
        doNothing().when(bookingServSpy).checkIfUserCanModifyBooking(any(Booking.class), anyString(), anyBoolean());
        doNothing().when(bookingRepository).delete(any(Booking.class));

        bookingServSpy.deleteBooking(id, username, true);

        verify(bookingRepository, times(1)).delete(any(Booking.class));
    }

    @Test
    void testSaveBlockWhenOverlappingBookingThenThrowGeneralException() {
        BookingRq req = new BookingRq(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 1L);
        String username = "test@test.com";
        User user = new User();
        user.setEmail(username);
        Booking overlappingBooking = new Booking();

        BookingServiceImpl bookingServSpy = spy(bookingService);
        doReturn(Optional.of(overlappingBooking)).when(bookingServSpy).getOverlappingBooking(any(BookingRq.class));

        // Act and Assert
        assertThrows(BookingOverlappingException.class, () -> bookingServSpy.saveBlock(req, username));
    }

    @Test
    void testSaveBlockWhenOverlappingBlockAndManageReturnsThenReturnManaged() {
        BookingRq req = new BookingRq(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 1L);
        String username = "test@test.com";
        User user = new User();
        user.setEmail(username);
        Property property = new Property();
        Booking overlappingBlock = new Booking();
        overlappingBlock.setType(BookingTypeEnum.BLOCK.name());

        BookingServiceImpl bookingServSpy = spy(bookingService);
        doReturn(Optional.empty()).when(bookingServSpy).getOverlappingBooking(any(BookingRq.class));
        doReturn(Optional.of(overlappingBlock)).when(bookingServSpy).getOverlappingBlock(any(BookingRq.class));
        doReturn(bookingDTO).when(bookingServSpy).manageSaveOverlappingBlock(any(), any());

        BookingDTO result = bookingServSpy.saveBlock(req, username);
        assertEquals(result, bookingDTO);
    }

    @Test
    void testSaveBlockWhenOverlappingBlockAndManageReturnsNothingThenSave() {
        BookingRq req = new BookingRq(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 1L);
        String username = "test@test.com";
        User user = new User();
        user.setEmail(username);
        Property property = new Property();
        Booking overlappingBlock = new Booking();

        BookingServiceImpl bookingServSpy = spy(bookingService);
        doReturn(Optional.empty()).when(bookingServSpy).getOverlappingBooking(any(BookingRq.class));
        doReturn(Optional.of(overlappingBlock)).when(bookingServSpy).getOverlappingBlock(any(BookingRq.class));
        doReturn(null).when(bookingServSpy).manageSaveOverlappingBlock(any(BookingRq.class), any(Booking.class));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(propertyRepository.findById(any())).thenReturn(Optional.of(property));

        bookingServSpy.saveBlock(req, username);

        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testSaveBlockWhenUsernameInvalidThenThrowException() {
        BookingRq req = new BookingRq(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 1L);
        String username = "test@test.com";
        User user = new User();
        user.setEmail(username);

        BookingServiceImpl bookingServSpy = spy(bookingService);
        doReturn(Optional.empty()).when(bookingServSpy).getOverlappingBooking(any(BookingRq.class));
        doReturn(Optional.empty()).when(bookingServSpy).getOverlappingBlock(any(BookingRq.class));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> bookingServSpy.saveBlock(req, username));
    }

    @Test
    void testSaveBlockWhenPropertyInvalidThenThrowException() {
        BookingRq req = new BookingRq(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 1L);
        String username = "test@test.com";
        User user = new User();
        user.setEmail(username);
        Property property = new Property();

        BookingServiceImpl bookingServSpy = spy(bookingService);
        doReturn(Optional.empty()).when(bookingServSpy).getOverlappingBooking(any(BookingRq.class));
        doReturn(Optional.empty()).when(bookingServSpy).getOverlappingBlock(any(BookingRq.class));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(propertyRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingServSpy.saveBlock(req, username));
    }

    @Test
    void testSaveBlockWhenAllGoodThenSave() {
        BookingRq req = new BookingRq(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 1L);
        String username = "test@test.com";
        User user = new User();
        user.setEmail(username);
        Property property = new Property();

        BookingServiceImpl bookingServSpy = spy(bookingService);
        doReturn(Optional.empty()).when(bookingServSpy).getOverlappingBooking(any(BookingRq.class));
        doReturn(Optional.empty()).when(bookingServSpy).getOverlappingBlock(any(BookingRq.class));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(propertyRepository.findById(any())).thenReturn(Optional.of(property));

        bookingServSpy.saveBlock(req, username);

        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testUpdateBookingInternalBookWhenOverlappingBookingThenThrowGeneralException() {
        BookingRq req = new BookingRq(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 1L);
        String username = "test@test.com";
        User user = new User();
        user.setEmail(username);
        Booking overlappingBooking = new Booking();

        BookingServiceImpl bookingServSpy = spy(bookingService);
        doReturn(booking).when(bookingServSpy).findById(any(UUID.class), anyBoolean());
        doNothing().when(bookingServSpy).checkBookingRequest(any(BookingRq.class));
        doNothing().when(bookingServSpy).checkIfUserCanModifyBooking(any(Booking.class), anyString(), anyBoolean());
        doReturn(Optional.of(overlappingBooking)).when(bookingServSpy).getOverlappingBooking(any(BookingRq.class));

        // Act and Assert
        assertThrows(BookingOverlappingException.class, () -> bookingServSpy.updateBookingInternal(id, req, username, true));
    }

    @Test
    void testUpdateBookingInternalBookWhenOverlappingBlockThenThrowError() {
        BookingRq req = new BookingRq(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 1L);
        String username = "test@test.com";
        User user = new User();
        user.setEmail(username);
        Booking overlappingBlock = new Booking();
        overlappingBlock.setType(BookingTypeEnum.BLOCK.name());

        BookingServiceImpl bookingServSpy = spy(bookingService);
        doReturn(booking).when(bookingServSpy).findById(any(UUID.class), anyBoolean());
        doNothing().when(bookingServSpy).checkBookingRequest(any(BookingRq.class));
        doNothing().when(bookingServSpy).checkIfUserCanModifyBooking(any(Booking.class), anyString(), anyBoolean());
        doReturn(Optional.empty()).when(bookingServSpy).getOverlappingBooking(any(BookingRq.class));
        doReturn(Optional.of(overlappingBlock)).when(bookingServSpy).getOverlappingBlock(any(BookingRq.class));

        // Act and Assert
        assertThrows(BookingOverlappingException.class, () -> bookingServSpy.updateBookingInternal(id, req, username, true));

    }

    @Test
    void testUpdateBookingInternalBlockWhenOverlappingBlockAndManageReturnsThenReturnManaged() {
        BookingRq req = new BookingRq(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 1L);
        String username = "test@test.com";
        User user = new User();
        user.setEmail(username);
        Booking overlappingBlock = new Booking();
        overlappingBlock.setType(BookingTypeEnum.BLOCK.name());

        BookingServiceImpl bookingServSpy = spy(bookingService);
        doReturn(booking).when(bookingServSpy).findById(any(UUID.class), anyBoolean());
        doNothing().when(bookingServSpy).checkBookingRequest(any(BookingRq.class));
        doNothing().when(bookingServSpy).checkIfUserCanModifyBooking(any(Booking.class), anyString(), anyBoolean());
        doReturn(Optional.empty()).when(bookingServSpy).getOverlappingBooking(any(BookingRq.class));
        doReturn(Optional.of(overlappingBlock)).when(bookingServSpy).getOverlappingBlock(any(BookingRq.class));
        doReturn(bookingDTO).when(bookingServSpy).manageSaveOverlappingBlock(any(), any());

        BookingDTO result = bookingServSpy.updateBookingInternal(id, req, username, false);
        assertEquals(result, bookingDTO);
    }

    @Test
    void testUpdateBookingInternalWhenPropertyInvalidThenThrowException() {
        BookingRq req = new BookingRq(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 1L);
        String username = "test@test.com";
        User user = new User();
        user.setEmail(username);

        BookingServiceImpl bookingServSpy = spy(bookingService);
        doReturn(booking).when(bookingServSpy).findById(any(UUID.class), anyBoolean());
        doNothing().when(bookingServSpy).checkBookingRequest(any(BookingRq.class));
        doNothing().when(bookingServSpy).checkIfUserCanModifyBooking(any(Booking.class), anyString(), anyBoolean());
        doReturn(Optional.empty()).when(bookingServSpy).getOverlappingBooking(any(BookingRq.class));
        doReturn(Optional.empty()).when(bookingServSpy).getOverlappingBlock(any(BookingRq.class));
        when(propertyRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingServSpy.updateBookingInternal(id, req, username, true));
    }

    @Test
    void testUpdateBookingInternalBlockWhenOverlappingBlockAndManageReturnsNothingThenSave() {
        BookingRq req = new BookingRq(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 1L);
        String username = "test@test.com";
        User user = new User();
        user.setEmail(username);
        Property property = new Property();

        BookingServiceImpl bookingServSpy = spy(bookingService);
        doReturn(booking).when(bookingServSpy).findById(any(UUID.class), anyBoolean());
        doNothing().when(bookingServSpy).checkBookingRequest(any(BookingRq.class));
        doNothing().when(bookingServSpy).checkIfUserCanModifyBooking(any(Booking.class), anyString(), anyBoolean());
        doReturn(Optional.empty()).when(bookingServSpy).getOverlappingBooking(any(BookingRq.class));
        doReturn(Optional.empty()).when(bookingServSpy).getOverlappingBlock(any(BookingRq.class));
        when(propertyRepository.findById(any())).thenReturn(Optional.of(property));

        bookingServSpy.updateBookingInternal(id, req, username, false);

        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testManageSaveOverlappingBlockWhenExistingBookThenThrow() {
        BookingRq req = new BookingRq(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 1L);

        Booking existingBooking = new Booking();
        existingBooking.setType(BookingTypeEnum.BOOK.name());

        assertThrows(BookingOverlappingException.class, () -> bookingService.manageSaveOverlappingBlock(req, existingBooking));
    }

    @Test
    void testManageSaveOverlappingBlockWhenUnknownBookingTypeThenThrow() {
        BookingRq req = new BookingRq(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 1L);

        Booking existingBooking = new Booking();
        existingBooking.setType("gsdfsdfg");

        assertThrows(GeneralException.class, () -> bookingService.manageSaveOverlappingBlock(req, existingBooking));
    }

    @Test
    void testManageSaveOverlappingBlockWhenBlockThenMergeCheckinDate() {
        LocalDate checkInDate = LocalDate.now().plusDays(1);
        BookingRq req = new BookingRq(checkInDate, LocalDate.now().plusDays(4), 1L);

        Booking existingBooking = new Booking();
        existingBooking.setType(BookingTypeEnum.BLOCK.name());
        existingBooking.setCheckinDate(LocalDate.now().plusDays(3));
        existingBooking.setCheckoutDate(LocalDate.now().plusDays(4));

        when(bookingRepository.save(MockitoHamcrest.argThat(Matchers.hasProperty("checkinDate", Matchers.is(checkInDate))))).thenReturn(booking);

        BookingDTO booking = bookingService.manageSaveOverlappingBlock(req, existingBooking);

        verify(bookingRepository, times(1)).save(any(Booking.class));
        assertEquals(bookingDTO, booking);
    }

    @Test
    void testManageSaveOverlappingBlockWhenBlockThenMergeCheckoutDate() {
        LocalDate checkOutDate = LocalDate.now().plusDays(6);
        BookingRq req = new BookingRq(LocalDate.now().plusDays(1), checkOutDate, 1L);

        Booking existingBooking = new Booking();
        existingBooking.setType(BookingTypeEnum.BLOCK.name());
        existingBooking.setCheckinDate(LocalDate.now().plusDays(3));
        existingBooking.setCheckoutDate(LocalDate.now().plusDays(4));

        when(bookingRepository.save(MockitoHamcrest.argThat(Matchers.hasProperty("checkoutDate", Matchers.is(checkOutDate))))).thenReturn(booking);

        BookingDTO booking = bookingService.manageSaveOverlappingBlock(req, existingBooking);

        verify(bookingRepository, times(1)).save(any(Booking.class));
        assertEquals(bookingDTO, booking);
    }

    @Test
    void testGetOverlappingBookingWhenNullThenThrow() {
        assertThrows(GeneralException.class, () -> bookingService.getOverlappingBooking(null));
    }

    @Test
    void testGetOverlappingBookingWhenFindEmptyThenReturnEmptyList() {
        BookingRq req = new BookingRq(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 1L);
        String username = "test@test.com";
        User user = new User();
        user.setEmail(username);
        Property property = new Property();

        when(bookingRepository.findAllByTypeAndPropertyIdAndCheckinDateAfterOrderByCheckinDateAsc(anyString(), anyLong(), any(LocalDate.class))).thenReturn(null);
        Optional<Booking> opt = bookingService.getOverlappingBooking(req, true);
        assertTrue(opt.isEmpty());
    }

    @Test
    void testGetOverlappingBlockWhenFindEmptyThenReturnEmptyList() {
        BookingRq req = new BookingRq(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 1L);
        String username = "test@test.com";
        User user = new User();
        user.setEmail(username);
        Property property = new Property();

        when(bookingRepository.findAllByTypeAndPropertyIdAndCheckinDateAfterOrderByCheckinDateAsc(anyString(), anyLong(), any(LocalDate.class))).thenReturn(null);
        Optional<Booking> opt = bookingService.getOverlappingBooking(req, false);
        assertTrue(opt.isEmpty());
    }

    @Test
    void testGetOverlappingBlockWhenAllGoodThenReturnBooking() {
        BookingRq req = new BookingRq(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 1L);
        String username = "test@test.com";
        User user = new User();
        user.setEmail(username);
        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);

        BookingServiceImpl bookingServSpy = spy(bookingService);
        when(bookingRepository.findAllByTypeAndPropertyIdAndCheckinDateAfterOrderByCheckinDateAsc(anyString(), anyLong(), any(LocalDate.class))).thenReturn(bookingList);
        doReturn(true).when(bookingServSpy).hasOverlap(any(Booking.class), any(LocalDate.class), any(LocalDate.class));

        Optional<Booking> opt = bookingServSpy.getOverlappingBooking(req, false);
        assertTrue(opt.isPresent());
        assertEquals(booking, opt.get());
    }

    @Test
    void testCheckBookingRequestWhenNullThenThrow() {
        assertThrows(GeneralException.class, () -> bookingService.checkBookingRequest(null));
    }

    @Test
    void testCheckBookingRequestWhenCheckinNullThenThrow() {
        BookingRq req = new BookingRq(null, LocalDate.now().plusDays(2), 1L);

        assertThrows(GeneralException.class, () -> bookingService.checkBookingRequest(req));
    }

    @Test
    void testCheckBookingRequestWhenCheckoutNullThenThrow() {
        BookingRq req = new BookingRq(LocalDate.now().plusDays(2), null , 1L);

        assertThrows(GeneralException.class, () -> bookingService.checkBookingRequest(req));
    }

    @Test
    void testCheckBookingRequestWhenCheckinPastThenThrow() {
        BookingRq req = new BookingRq(LocalDate.now().minusDays(1), LocalDate.now().plusDays(2), 1L);

        assertThrows(GeneralException.class, () -> bookingService.checkBookingRequest(req));
    }

    @Test
    void testCheckBookingRequestWhenCheckinAfterOutThenThrow() {
        BookingRq req = new BookingRq(LocalDate.now().plusDays(3), LocalDate.now().plusDays(2), 1L);

        assertThrows(GeneralException.class, () -> bookingService.checkBookingRequest(req));
    }

    @Test
    void testCheckIfUserCanModifyBookingWhenNoUsernameThenThrow() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> bookingService.checkIfUserCanModifyBooking(booking, "username", true ));
    }

    @Test
    void testCheckIfUserCanModifyBookingWhenEmptyRolesThenThrow() {
        User user = new User();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        assertThrows(GeneralException.class, () -> bookingService.checkIfUserCanModifyBooking(booking, "username", true ));
    }

    @Test
    void testCheckIfUserCanModifyBookingWhenOwnerButBookingForOtherThenThrow() {
        User user = new User();

        Role role = new Role();
        role.setLabel(RoleEnum.ROLE_OWNER.name());

        List<Role> roles = new ArrayList<>();
        roles.add(role);

        user.setRoleList(roles);

        Property property = new Property();
        User admin = new User();
        admin.setEmail("test");
        property.setAdmin(admin);

        booking.setProperty(property);

        User bookingUser = new User();
        bookingUser.setEmail("testestsetss");

        booking.setUser(bookingUser);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        assertThrows(GeneralException.class, () -> bookingService.checkIfUserCanModifyBooking(booking, "username", true ));
    }

}