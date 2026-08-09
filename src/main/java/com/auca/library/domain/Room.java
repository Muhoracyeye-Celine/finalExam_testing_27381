package com.auca.library.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @Column(
            name = "room_id",
            nullable = false,
            updatable = false
    )
    private UUID roomId;

    @Column(
            name = "room_code",
            nullable = false,
            unique = true,
            length = 30
    )
    private String roomCode;

    @Column(
            name = "room_name",
            nullable = false,
            length = 100
    )
    private String roomName;

    protected Room() {
        // Required by Hibernate.
    }

    public Room(String roomCode, String roomName) {
        this.roomId = UUID.randomUUID();
        this.roomCode = roomCode;
        this.roomName = roomName;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}