package com.booky.mapper;

import com.booky.model.dto.BookingDTO;
import com.booky.model.dto.BookingRq;
import com.booky.model.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookingMapper {

    public BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);


    /**
     * @author dragos
     * Mapper Booking Entity to Booking DTO
     * @param {@link Booking}
     * @return {@link BookingDTO}
     */
    @Mapping(source = "user.email", target = "username")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.name", target = "name")
    @Mapping(source = "property.capacity", target = "capacity")
    @Mapping(source = "property.address", target = "address")
    BookingDTO map(Booking entity);

    Booking mapCreateReq(BookingRq req);
}
