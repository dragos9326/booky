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

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author dragos
 * Property class mapped to properties table
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="properties")
@Entity(name = "Property")
public class Property {

    @Id
    @Column(name = "PROPERTY_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auto_increment_property_id")
    @SequenceGenerator(name = "auto_increment_property_id", sequenceName = "auto_increment_property_id", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column(name = "CAPACITY")
    private Integer capacity;

    @Column(name = "ADDRESS")
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADMIN_USER_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false)
    private User admin;

    @Column(name = "TM_CREATED", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "TM_LAST_UPDATE", nullable = false)
    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;

    @Column(name = "LAST_UPDATE_BY", nullable = false)
    private String lastUpdateBy;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "property")
    @PrimaryKeyJoinColumn
    private List<Booking> bookings;

}
