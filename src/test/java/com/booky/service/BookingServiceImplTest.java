package com.booky.service;

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
    void testSaveBookingWhenOverlappingBookingThenThrowGeneralException() {
        BookingRq req = new BookingRq(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 1L);
        String username = "test@test.com";
        User user = new User();
        user.setEmail(username);
        Booking overlappingBooking = new Booking();

        BookingServiceImpl bookingServSpy = spy(bookingService);
        doReturn(Optional.of(overlappingBooking)).when(bookingServSpy).getOverlappingBooking(any(BookingRq.class));

        // Act and Assert
        assertThrows(GeneralException.class, () -> bookingServSpy.saveBooking(req, username));
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

        assertThrows(GeneralException.class, () -> bookingServSpy.saveBooking(req, username));
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

}