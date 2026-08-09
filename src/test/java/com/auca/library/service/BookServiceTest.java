package com.auca.library.service;

import com.auca.library.dao.BookDao;
import com.auca.library.domain.Book;
import com.auca.library.domain.BookStatus;
import com.auca.library.exception.DuplicateBookCodeException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BookServiceTest {

    private static BookService bookService;

    @BeforeClass
    public static void prepareBookService() {
        bookService = new BookService(new BookDao());
    }

    @Test
    public void registerBook_withValidDetails_succeeds() {
        String bookCode = uniqueBookCode();

        Book registeredBook = bookService.registerBook(
                bookCode,
                "9780134685991",
                "Effective Java",
                "Joshua Bloch"
        );

        assertNotNull(registeredBook.getBookId());

        assertEquals(
                bookCode.toUpperCase(Locale.ROOT),
                registeredBook.getBookCode()
        );

        assertEquals(
                BookStatus.AVAILABLE,
                registeredBook.getBookStatus()
        );
    }

    @Test(expected = DuplicateBookCodeException.class)
    public void registerBook_withDuplicateCode_throwsException() {
        String bookCode = uniqueBookCode();

        bookService.registerBook(
                bookCode,
                "9780132350884",
                "Clean Code",
                "Robert Martin"
        );

        bookService.registerBook(
                bookCode,
                "9780134494166",
                "Clean Architecture",
                "Robert Martin"
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerBook_withBlankTitle_throwsException() {
        bookService.registerBook(
                uniqueBookCode(),
                "9780134685991",
                "   ",
                "Joshua Bloch"
        );
    }

    private static String uniqueBookCode() {
        return "book-"
                + UUID.randomUUID()
                        .toString()
                        .substring(0, 8);
    }
}