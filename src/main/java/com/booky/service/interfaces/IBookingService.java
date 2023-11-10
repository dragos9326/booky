package com.booky.service.interfaces;

import com.booky.model.dto.BookingDTO;
import com.booky.model.dto.GeneralResponse;
import com.booky.model.dto.BookingRq;

import java.util.List;
import java.util.UUID;

/**
 * @author dragos
 */
public interface IBookingService {

    BookingDTO findBlockById(UUID id);

    BookingDTO findBookingById(UUID id);

    List<BookingDTO> getAllBookings();

    List<BookingDTO> getAllBlocksByProperty(Long propertyId);

    BookingDTO saveBooking(BookingRq req, String username);

    GeneralResponse deleteBooking(UUID bookingId, String username, boolean isBooking);

    BookingDTO updateBooking(UUID bookingId, BookingRq req, String username);

    BookingDTO saveBlock(BookingRq req, String username);

    BookingDTO updateBlock(UUID bookingId, BookingRq req, String username);
}
