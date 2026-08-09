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
    public void assignShelfToRoom_updatesShelfRoomId() {
        Room room = registerTestRoom();
        Shelf shelf = registerTestShelf();

        organizationService.assignShelfToRoom(
                shelf.getShelfId(),
                room.getRoomId()
        );

        Shelf updatedShelf =
                organizationService.getShelfById(
                        shelf.getShelfId()
                );

        assertNotNull(updatedShelf.getRoom());

        assertEquals(
                room.getRoomId(),
                updatedShelf.getRoom().getRoomId()
        );
    }

    @Test
    public void assignBookToShelf_updatesBookShelfId() {
        Shelf shelf = registerTestShelf();
        Book book = registerTestBook();

        organizationService.assignBookToShelf(
                book.getBookId(),
                shelf.getShelfId()
        );

        Book updatedBook =
                bookService.getBookById(book.getBookId());

        assertNotNull(updatedBook.getShelf());

        assertEquals(
                shelf.getShelfId(),
                updatedBook.getShelf().getShelfId()
        );
    }

    @Test
    public void assignBookToShelf_incrementsShelfAvailableStock() {
        Shelf shelf = registerTestShelf();
        Book book = registerTestBook();

        int stockBeforeAssignment =
                shelf.getAvailableStock();

        organizationService.assignBookToShelf(
                book.getBookId(),
                shelf.getShelfId()
        );

        Shelf updatedShelf =
                organizationService.getShelfById(
                        shelf.getShelfId()
                );

        assertEquals(
                stockBeforeAssignment + 1,
                updatedShelf.getAvailableStock()
        );
    }

   @Test
public void roomWithMultipleShelves_sumsBookCountsAcrossShelves() {
    Room room = registerTestRoom();

    Shelf firstShelf = registerTestShelf();
    Shelf secondShelf = registerTestShelf();

    organizationService.assignShelfToRoom(
            firstShelf.getShelfId(),
            room.getRoomId()
    );

    organizationService.assignShelfToRoom(
            secondShelf.getShelfId(),
            room.getRoomId()
    );

    Book firstBook = registerTestBook();
    Book secondBook = registerTestBook();
    Book thirdBook = registerTestBook();

    organizationService.assignBookToShelf(
            firstBook.getBookId(),
            firstShelf.getShelfId()
    );

    organizationService.assignBookToShelf(
            secondBook.getBookId(),
            firstShelf.getShelfId()
    );

    organizationService.assignBookToShelf(
            thirdBook.getBookId(),
            secondShelf.getShelfId()
    );

    int numberOfBooks =
            organizationService.countBooksInRoom(
                    room.getRoomId()
            );

    assertEquals(3, numberOfBooks);
}

    @Test
    public void multipleRooms_returnsRoomWithLowestBookCount() {
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

    @Test
public void roomWithNoShelves_returnsZero() {
    Room emptyRoom = registerTestRoom();

    int numberOfBooks =
            organizationService.countBooksInRoom(
                    emptyRoom.getRoomId()
            );

    assertEquals(0, numberOfBooks);
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