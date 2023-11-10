package com.booky.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author dragos
 * Booking class mapped to bookings table
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="bookings")
@Entity(name = "Booking")
public class Booking {

    @Id
    @Column(name = "BOOKING_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "TYPE", nullable = false)
    private String type;

    @Column(name = "CHECKIN_DATE")
    private LocalDate checkinDate;

    @Column(name = "CHECKOUT_DATE")
    private LocalDate checkoutDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROPERTY_ID", referencedColumnName = "PROPERTY_ID", insertable = true, updatable = true)
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", insertable = true, updatable = true)
    private User user;

    @Column(name = "TM_CREATED", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "TM_LAST_UPDATE", nullable = false)
    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;

    @Column(name = "LAST_UPDATE_BY", nullable = false)
    private String lastUpdateBy;

}
