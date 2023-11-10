package com.booky.model.dto;

import com.booky.model.entity.Property;
import com.booky.model.entity.User;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author dragos
 * Booking DTO mapped to {@link com.booky.model.entity.Booking} entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {

    private UUID id;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private String type;
    private Integer capacity;
    private String address;
    private String username;
    private String email;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String lastUpdateBy;

}
