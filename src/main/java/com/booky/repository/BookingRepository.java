package com.booky.repository;

import com.booky.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @author dragos
 */
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findAllByType(String type);

    List<Booking> findAllByTypeAndPropertyId(String type, Long propertyId);

    List<Booking> findAllByTypeAndPropertyIdAndCheckinDateAfterOrderByCheckinDateAsc(String type, Long propertyId, LocalDate date);
}
