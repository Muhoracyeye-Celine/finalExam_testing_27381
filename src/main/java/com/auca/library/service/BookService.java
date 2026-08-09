package com.auca.library.service;

import com.auca.library.dao.BookDao;
import com.auca.library.domain.Book;
import com.auca.library.exception.BookNotFoundException;
import com.auca.library.exception.DuplicateBookCodeException;
import com.auca.library.exception.BookNotAvailableException;

import java.util.Locale;
import java.util.UUID;

public class BookService {

    private final BookDao bookDao;

    public BookService(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public Book registerBook(
            String bookCode,
            String isbn,
            String title,
            String authorName
    ) {
        
        validateBookDetails(
                bookCode,
                isbn,
                title,
                authorName
        );

        String normalizedBookCode =
                bookCode.trim().toUpperCase(Locale.ROOT);

        if (bookDao
                .findByCode(normalizedBookCode)
                .isPresent()) {
            throw new DuplicateBookCodeException(
                    "Book code already exists: "
                            + normalizedBookCode
            );
        }

        Book book = new Book(
                normalizedBookCode,
                isbn.trim(),
                title.trim(),
                authorName.trim()
        );

        return bookDao.save(book);
    }

    public Book getBookById(UUID bookId) {
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

    public Book getBookByCode(String bookCode) {
        if (bookCode == null || bookCode.isBlank()) {
            throw new IllegalArgumentException(
                    "Book code is required."
            );
        }

        return bookDao.findByCode(bookCode.trim())
                .orElseThrow(() ->
                        new BookNotFoundException(
                                "Book was not found."
                        )
                );
    }

    public Book markBookAsBorrowed(Book book) {
    if (book == null) {
        throw new IllegalArgumentException(
                "Book is required."
        );
    }

    if (!book.isAvailable()) {
        throw new BookNotAvailableException(
                "The book is not available for borrowing."
        );
    }

    book.markAsBorrowed();
    return bookDao.update(book);
}

public Book markBookAsAvailable(Book book) {
    if (book == null) {
        throw new IllegalArgumentException(
                "Book is required."
        );
    }

    book.markAsAvailable();
    return bookDao.update(book);
}

    private void validateBookDetails(
            String bookCode,
            String isbn,
            String title,
            String authorName
    ) {
        requireText(bookCode, "Book code");
        requireText(isbn, "ISBN");
        requireText(title, "Book title");
        requireText(authorName, "Author name");
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