package com.booky.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tbl_roles")
public class Role {

    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    private String label;

    @ManyToMany(mappedBy = "roleList")
    private List<User> users;
}