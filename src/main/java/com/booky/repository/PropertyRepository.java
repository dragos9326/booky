package com.booky.repository;

import com.booky.model.entity.Booking;
import com.booky.model.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author dragos
 */
public interface PropertyRepository extends JpaRepository<Property, Long> {
}
