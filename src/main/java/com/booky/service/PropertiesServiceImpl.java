package com.booky.service;

import com.booky.exceptions.GeneralException;
import com.booky.mapper.BookingMapper;
import com.booky.model.dto.BookingDTO;
import com.booky.model.entity.*;
import com.booky.repository.PropertyRepository;
import com.booky.service.interfaces.IPropertiesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author dragos
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PropertiesServiceImpl implements IPropertiesService {

    private final PropertyRepository propertyRepository;

    @Override
    public List<BookingDTO> getAllBookingsAndBlocks(Long id) {
        Optional<Property> propOpt = propertyRepository.findById(id);
        if(propOpt.isEmpty()){
            throw new GeneralException("Couldn't find property");
        }
        Property prop = propOpt.get();
        return prop.getBookings().stream().filter(b -> b.getCheckinDate().isAfter(LocalDate.now().minusDays(1))).map(BookingMapper.INSTANCE::map).toList();
    }
}
