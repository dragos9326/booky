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
class PropertiesServiceImplTest {

    @Mock
    private PropertyRepository propertyRepository;

    @InjectMocks
    private PropertiesServiceImpl propertiesService;

    @Test
    void testGetAllBookingsAndBlocksWhenNotFoundThenThrow() {
        when(propertyRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GeneralException.class, () -> propertiesService.getAllBookingsAndBlocks(1L));
    }

    @Test
    void testGetAllBookingsAndBlocksWhenFoundThenReturnList() {
        Property property = new Property();

        Booking book = new Booking();
        book.setCheckinDate(LocalDate.now().plusDays(1));
        Booking book2 = new Booking();
        book2.setCheckinDate(LocalDate.now().plusDays(1));

        List<Booking> bookings = new ArrayList<>();
        bookings.add(book);
        bookings.add(book2);

        property.setBookings(bookings);

        when(propertyRepository.findById(anyLong())).thenReturn(Optional.of(property));

        List<BookingDTO> result = propertiesService.getAllBookingsAndBlocks(1L);
        assertEquals(2, result.size());
    }

    @Test
    void testGetAllBookingsAndBlocksWhenFoundOnlyOnInFutureThenReturnList() {
        Property property = new Property();

        Booking book = new Booking();
        book.setCheckinDate(LocalDate.now().plusDays(1));
        Booking book2 = new Booking();
        book2.setCheckinDate(LocalDate.now().minusDays(1));

        List<Booking> bookings = new ArrayList<>();
        bookings.add(book);
        bookings.add(book2);

        property.setBookings(bookings);

        when(propertyRepository.findById(anyLong())).thenReturn(Optional.of(property));

        List<BookingDTO> result = propertiesService.getAllBookingsAndBlocks(1L);
        assertEquals(1, result.size());
    }

}