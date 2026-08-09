package com.auca.library.service;

import com.auca.library.dao.BookDao;
import com.auca.library.dao.RoomDao;
import com.auca.library.dao.ShelfDao;
import com.auca.library.domain.Book;
import com.auca.library.domain.Room;
import com.auca.library.domain.Shelf;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LibraryOrganizationServiceTest {

    private static LibraryOrganizationService
            organizationService;

    private static BookService bookService;

    @BeforeClass
    public static void prepareOrganizationServices() {
        BookDao bookDao = new BookDao();

        organizationService =
                new LibraryOrganizationService(
                        new RoomDao(),
                        new ShelfDao(),
                        bookDao
                );

        bookService = new BookService(bookDao);
    }

    @Test
    public void registerRoomAndShelf_withValidDetails_succeeds() {
        String roomCode = uniqueCode("ROOM");
        String shelfCode = uniqueCode("SHELF");

        Room room = organizationService.registerRoom(
                roomCode,
                "Main Library Room"
        );

        Shelf shelf = organizationService.registerShelf(
                shelfCode,
                "Programming Shelf"
        );

        assertNotNull(room.getRoomId());
        assertNotNull(shelf.getShelfId());

        assertEquals(
                roomCode.toUpperCase(Locale.ROOT),
                room.getRoomCode()
        );

        assertEquals(
                shelfCode.toUpperCase(Locale.ROOT),
                shelf.getShelfCode()
        );
    }

    @Test
    public void assignShelfToRoom_connectsShelfAndRoom() {
        Room room = registerTestRoom();
        Shelf shelf = registerTestShelf();

        Shelf assignedShelf =
                organizationService.assignShelfToRoom(
                        shelf.getShelfId(),
                        room.getRoomId()
                );

        assertNotNull(assignedShelf.getRoom());

        assertEquals(
                room.getRoomId(),
                assignedShelf.getRoom().getRoomId()
        );
    }

    @Test
    public void assignBookToShelf_connectsBookAndShelf() {
        Shelf shelf = registerTestShelf();
        Book book = registerTestBook();

        Book assignedBook =
                organizationService.assignBookToShelf(
                        book.getBookId(),
                        shelf.getShelfId()
                );

        assertNotNull(assignedBook.getShelf());

        assertEquals(
                shelf.getShelfId(),
                assignedBook.getShelf().getShelfId()
        );
    }

    @Test
    public void countBooksInRoom_returnsCorrectNumber() {
        Room room = registerTestRoom();
        Shelf shelf = registerTestShelf();

        organizationService.assignShelfToRoom(
                shelf.getShelfId(),
                room.getRoomId()
        );

        Book firstBook = registerTestBook();
        Book secondBook = registerTestBook();

        organizationService.assignBookToShelf(
                firstBook.getBookId(),
                shelf.getShelfId()
        );

        organizationService.assignBookToShelf(
                secondBook.getBookId(),
                shelf.getShelfId()
        );

        long numberOfBooks =
                organizationService.countBooksInRoom(
                        room.getRoomId()
                );

        assertEquals(2L, numberOfBooks);
    }

    @Test
    public void findRoomWithFewestBooks_returnsEmptyRoom() {
        Room emptyRoom = registerTestRoom();

        Room roomWithBook = registerTestRoom();
        Shelf shelf = registerTestShelf();

        organizationService.assignShelfToRoom(
                shelf.getShelfId(),
                roomWithBook.getRoomId()
        );

        Book book = registerTestBook();

        organizationService.assignBookToShelf(
                book.getBookId(),
                shelf.getShelfId()
        );

        Room roomWithFewestBooks =
                organizationService
                        .findRoomWithFewestBooks();

        assertNotNull(roomWithFewestBooks);

        long fewestBookCount =
                organizationService.countBooksInRoom(
                        roomWithFewestBooks.getRoomId()
                );

        assertEquals(0L, fewestBookCount);

        assertEquals(
                0L,
                organizationService.countBooksInRoom(
                        emptyRoom.getRoomId()
                )
        );
    }

    private static Room registerTestRoom() {
        return organizationService.registerRoom(
                uniqueCode("ROOM"),
                "Test Library Room"
        );
    }

    private static Shelf registerTestShelf() {
        return organizationService.registerShelf(
                uniqueCode("SHELF"),
                "Test Library Shelf"
        );
    }

    private static Book registerTestBook() {
        return bookService.registerBook(
                uniqueCode("BOOK"),
                "9780134685991",
                "Software Testing",
                "Test Author"
        );
    }

    private static String uniqueCode(String prefix) {
        return prefix
                + "-"
                + UUID.randomUUID()
                        .toString()
                        .substring(0, 8);
    }
}