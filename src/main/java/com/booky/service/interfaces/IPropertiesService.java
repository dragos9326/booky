package com.booky.service.interfaces;

import com.booky.model.dto.BookingDTO;
import com.booky.model.dto.BookingRq;
import com.booky.model.dto.GeneralResponse;

import java.util.List;
import java.util.UUID;

/**
 * @author dragos
 */
public interface IPropertiesService {

    List<BookingDTO> getAllBookingsAndBlocks(Long id);

}
