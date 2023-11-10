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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dragos
 * User class mapped to users table
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Entity(name = "User")
public class User {

    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auto_increment_user_id")
    @SequenceGenerator(name = "auto_increment_user_id", sequenceName = "auto_increment_user_id", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "NAME")
    private String name;

    @Column(name = "GOV_ID")
    private String governmentId;

    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateOfBirth;

    @Column(name = "TM_CREATED", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "TM_LAST_UPDATE", nullable = false)
    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;

    @Column(name = "LAST_UPDATE_BY", nullable = false)
    private String lastUpdateBy;

    @ManyToMany
    @JoinTable(name = "User_role" ,joinColumns =
    @JoinColumn ( name = " user_id" ), inverseJoinColumns =
    @JoinColumn(name = "role_id"))
    List<Role> roleList;
}
