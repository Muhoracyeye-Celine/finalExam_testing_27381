package com.auca.library.domain;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "shelves")
public class Shelf {
    @Column(
        name = "available_stock",
        nullable = false,
        columnDefinition = "integer default 0"
)
private int availableStock;

    @Id
    @Column(
            name = "shelf_id",
            nullable = false,
            updatable = false
    )
    private UUID shelfId;

    @Column(
            name = "shelf_code",
            nullable = false,
            unique = true,
            length = 30
    )
    private String shelfCode;

    @Column(
            name = "shelf_name",
            nullable = false,
            length = 100
    )
    private String shelfName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    protected Shelf() {
        // Required by Hibernate.
    }

    public Shelf(String shelfCode, String shelfName) {
        this.shelfId = UUID.randomUUID();
        this.shelfCode = shelfCode;
        this.shelfName = shelfName;
        this.availableStock = 0;
        this.room = null;
    }

    public UUID getShelfId() {
        return shelfId;
    }

    public String getShelfCode() {
        return shelfCode;
    }

    public void setShelfCode(String shelfCode) {
        this.shelfCode = shelfCode;
    }

    public String getShelfName() {
        return shelfName;
    }

    public void setShelfName(String shelfName) {
        this.shelfName = shelfName;
    }

    public int getAvailableStock() {
    return availableStock;
}

public void incrementAvailableStock() {
    availableStock++;
}

    public Room getRoom() {
        return room;
    }

    public void assignToRoom(Room room) {
        this.room = room;
    }
}