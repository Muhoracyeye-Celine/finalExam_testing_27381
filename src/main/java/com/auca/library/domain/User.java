package com.auca.library.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "LibraryUser")
@Table(name = "library_users")
public class User extends Person {

    @Column(
            name = "user_name",
            nullable = false,
            unique = true,
            length = 50
    )
    private String username;

    @Column(
            name = "password",
            nullable = false,
            length = 100
    )
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "role",
            nullable = false,
            length = 20
    )
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "village_id",
            nullable = false
    )
    private Location village;

    protected User() {
    
    }

    public User(
            String firstName,
            String lastName,
            Gender gender,
            String phoneNumber,
            String username,
            String passwordHash,
            Role role,
            Location village
    ) {
        super(
                firstName,
                lastName,
                gender,
                phoneNumber
        );

        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.village = village;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Location getVillage() {
        return village;
    }

    public void setVillage(Location village) {
        this.village = village;
    }
}