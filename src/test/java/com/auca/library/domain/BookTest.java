package com.auca.library.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BookTest {

    @Test
    public void newPhysicalBook_isAvailable() {
        Book book = createTestBook();

        assertNotNull(book.getBookId());

        assertEquals(
                BookStatus.AVAILABLE,
                book.getBookStatus()
        );

        assertTrue(book.isAvailable());
    }

    @Test
    public void bookStatus_canChangeWhenBorrowedAndReturned() {
        Book book = createTestBook();

        book.markAsBorrowed();

        assertEquals(
                BookStatus.BORROWED,
                book.getBookStatus()
        );

        book.markAsAvailable();

        assertEquals(
                BookStatus.AVAILABLE,
                book.getBookStatus()
        );
    }

    private Book createTestBook() {
        return new Book(
                "BOOK-001",
                "9780134685991",
                "Effective Java",
                "Joshua Bloch"
        );
    }
}