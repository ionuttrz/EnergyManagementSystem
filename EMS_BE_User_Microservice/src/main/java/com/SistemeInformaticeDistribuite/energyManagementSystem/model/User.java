package com.SistemeInformaticeDistribuite.energyManagementSystem.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data     //no need for constructor, getters and setters
@Entity
@Table(name = "user")
public class User {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @ManyToMany
    private Set<Role> roles;
}
