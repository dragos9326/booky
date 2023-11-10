package com.booky.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author dragos
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRq {
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private Long propertyId;
}
