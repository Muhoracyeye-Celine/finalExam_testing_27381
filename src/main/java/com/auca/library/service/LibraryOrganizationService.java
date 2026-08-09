package com.auca.library.service;

import com.auca.library.dao.BookDao;
import com.auca.library.dao.RoomDao;
import com.auca.library.dao.ShelfDao;
import com.auca.library.domain.Book;
import com.auca.library.domain.Room;
import com.auca.library.domain.Shelf;
import com.auca.library.exception.BookNotFoundException;
import com.auca.library.exception.DuplicateRoomCodeException;
import com.auca.library.exception.DuplicateShelfCodeException;
import com.auca.library.exception.RoomNotFoundException;
import com.auca.library.exception.ShelfNotFoundException;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class LibraryOrganizationService {

    private final RoomDao roomDao;
    private final ShelfDao shelfDao;
    private final BookDao bookDao;

    public LibraryOrganizationService(
            RoomDao roomDao,
            ShelfDao shelfDao,
            BookDao bookDao
    ) {
        this.roomDao = roomDao;
        this.shelfDao = shelfDao;
        this.bookDao = bookDao;
    }

    public Room registerRoom(
            String roomCode,
            String roomName
    ) {
        requireText(roomCode, "Room code");
        requireText(roomName, "Room name");

        String normalizedRoomCode =
                roomCode.trim().toUpperCase(Locale.ROOT);

        if (roomDao
                .findByCode(normalizedRoomCode)
                .isPresent()) {
            throw new DuplicateRoomCodeException(
                    "Room code already exists: "
                            + normalizedRoomCode
            );
        }

        Room room = new Room(
                normalizedRoomCode,
                roomName.trim()
        );

        return roomDao.save(room);
    }

    public Shelf registerShelf(
            String shelfCode,
            String shelfName
    ) {
        requireText(shelfCode, "Shelf code");
        requireText(shelfName, "Shelf name");

        String normalizedShelfCode =
                shelfCode.trim().toUpperCase(Locale.ROOT);

        if (shelfDao
                .findByCode(normalizedShelfCode)
                .isPresent()) {
            throw new DuplicateShelfCodeException(
                    "Shelf code already exists: "
                            + normalizedShelfCode
            );
        }

        Shelf shelf = new Shelf(
                normalizedShelfCode,
                shelfName.trim()
        );

        return shelfDao.save(shelf);
    }

    public Shelf assignShelfToRoom(
            UUID shelfId,
            UUID roomId
    ) {
        Shelf shelf = getShelfById(shelfId);
        Room room = getRoomById(roomId);

        shelf.assignToRoom(room);

        return shelfDao.update(shelf);
    }

    public Book assignBookToShelf(
            UUID bookId,
            UUID shelfId
    ) {
        Book book = getBookById(bookId);
        Shelf shelf = getShelfById(shelfId);

        book.assignToShelf(shelf);

        return bookDao.update(book);
    }

    public long countBooksInRoom(UUID roomId) {
        Room room = getRoomById(roomId);

        return roomDao.countBooksByRoomId(
                room.getRoomId()
        );
    }

    public Room findRoomWithFewestBooks() {
        List<Room> rooms = roomDao.findAll();

        if (rooms.isEmpty()) {
            throw new RoomNotFoundException(
                    "No library rooms were found."
            );
        }

        Room roomWithFewestBooks = rooms.get(0);

        long fewestBookCount =
                roomDao.countBooksByRoomId(
                        roomWithFewestBooks.getRoomId()
                );

        for (Room currentRoom : rooms) {
            long currentBookCount =
                    roomDao.countBooksByRoomId(
                            currentRoom.getRoomId()
                    );

            if (currentBookCount < fewestBookCount) {
                roomWithFewestBooks = currentRoom;
                fewestBookCount = currentBookCount;
            }
        }

        return roomWithFewestBooks;
    }

    public Room getRoomById(UUID roomId) {
        if (roomId == null) {
            throw new IllegalArgumentException(
                    "Room ID is required."
            );
        }

        return roomDao.findById(roomId)
                .orElseThrow(() ->
                        new RoomNotFoundException(
                                "Room was not found."
                        )
                );
    }

    public Shelf getShelfById(UUID shelfId) {
        if (shelfId == null) {
            throw new IllegalArgumentException(
                    "Shelf ID is required."
            );
        }

        return shelfDao.findById(shelfId)
                .orElseThrow(() ->
                        new ShelfNotFoundException(
                                "Shelf was not found."
                        )
                );
    }

    private Book getBookById(UUID bookId) {
        if (bookId == null) {
            throw new IllegalArgumentException(
                    "Book ID is required."
            );
        }

        return bookDao.findById(bookId)
                .orElseThrow(() ->
                        new BookNotFoundException(
                                "Book was not found."
                        )
                );
    }

    private void requireText(
            String value,
            String fieldName
    ) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " is required."
            );
        }
    }
}