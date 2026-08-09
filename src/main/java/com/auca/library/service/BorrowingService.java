package com.auca.library.service;

import com.auca.library.dao.BorrowingDao;
import com.auca.library.domain.Book;
import com.auca.library.domain.Borrowing;
import com.auca.library.domain.Membership;
import com.auca.library.exception.BookNotAvailableException;
import com.auca.library.exception.BorrowingLimitExceededException;
import com.auca.library.exception.InactiveMembershipException;
import com.auca.library.exception.BorrowingAlreadyReturnedException;
import com.auca.library.exception.BorrowingNotFoundException;
import com.auca.library.util.LateFeeCalculator;

import java.time.LocalDate;
import java.util.UUID;

public class BorrowingService {

    private final BorrowingDao borrowingDao;
    private final MembershipService membershipService;
    private final BookService bookService;

    public BorrowingService(
            BorrowingDao borrowingDao,
            MembershipService membershipService,
            BookService bookService
    ) {
        this.borrowingDao = borrowingDao;
        this.membershipService = membershipService;
        this.bookService = bookService;
    }

    public Borrowing borrowBook(
            UUID personId,
            UUID bookId,
            LocalDate borrowedDate,
            LocalDate dueDate
    ) {
        validateBorrowingDetails(
                personId,
                bookId,
                borrowedDate,
                dueDate
        );

        Membership membership =
                membershipService
                        .getMembershipByUserId(personId);

        if (!membership.isActiveOn(borrowedDate)) {
            throw new InactiveMembershipException(
                    "The membership is not active "
                            + "on the borrowing date."
            );
        }

        long activeBorrowings =
                borrowingDao
                        .countActiveBorrowingsByMembershipId(
                                membership.getMembershipId()
                        );

        int maximumBorrowedBooks =
                membership
                        .getMembershipType()
                        .getMaximumBorrowedBooks();

        if (activeBorrowings >= maximumBorrowedBooks) {
            throw new BorrowingLimitExceededException(
                    "The membership borrowing limit "
                            + "has been reached."
            );
        }

        Book book = bookService.getBookById(bookId);

        boolean bookHasActiveBorrowing =
                borrowingDao
                        .findActiveByBookId(bookId)
                        .isPresent();

        if (!book.isAvailable()
                || bookHasActiveBorrowing) {
            throw new BookNotAvailableException(
                    "The book is already borrowed."
            );
        }

        Borrowing borrowing = new Borrowing(
                membership,
                book,
                borrowedDate,
                dueDate
        );

        Borrowing savedBorrowing =
                borrowingDao.save(borrowing);

        bookService.markBookAsBorrowed(book);

        return savedBorrowing;
    }


    public Borrowing returnBook(
        UUID borrowingId,
        LocalDate returnDate
) {
    if (borrowingId == null) {
        throw new IllegalArgumentException(
                "Borrowing ID is required."
        );
    }

    if (returnDate == null) {
        throw new IllegalArgumentException(
                "Return date is required."
        );
    }

    Borrowing borrowing =
            getBorrowingById(borrowingId);

    if (borrowing.isReturned()) {
        throw new BorrowingAlreadyReturnedException(
                "This borrowing has already been returned."
        );
    }

    if (returnDate.isBefore(
            borrowing.getBorrowedDate()
    )) {
        throw new IllegalArgumentException(
                "Return date cannot be before "
                        + "the borrowed date."
        );
    }

    int lateFee = LateFeeCalculator.calculateLateFee(
            borrowing
                    .getMembership()
                    .getMembershipType(),
            borrowing.getDueDate(),
            returnDate
    );

    borrowing.markAsReturned(returnDate);
    borrowing.setLateFeeInRwandanFrancs(lateFee);

    Borrowing updatedBorrowing =
            borrowingDao.update(borrowing);

    bookService.markBookAsAvailable(
            borrowing.getBook()
    );

    return updatedBorrowing;
}

public Borrowing getBorrowingById(
        UUID borrowingId
) {
    if (borrowingId == null) {
        throw new IllegalArgumentException(
                "Borrowing ID is required."
        );
    }

    return borrowingDao.findById(borrowingId)
            .orElseThrow(() ->
                    new BorrowingNotFoundException(
                            "Borrowing record was not found."
                    )
            );
}

    private void validateBorrowingDetails(
            UUID personId,
            UUID bookId,
            LocalDate borrowedDate,
            LocalDate dueDate
    ) {
        if (personId == null) {
            throw new IllegalArgumentException(
                    "Person ID is required."
            );
        }

        if (bookId == null) {
            throw new IllegalArgumentException(
                    "Book ID is required."
            );
        }

        if (borrowedDate == null) {
            throw new IllegalArgumentException(
                    "Borrowed date is required."
            );
        }

        if (dueDate == null) {
            throw new IllegalArgumentException(
                    "Due date is required."
            );
        }

        if (dueDate.isBefore(borrowedDate)) {
            throw new IllegalArgumentException(
                    "Due date cannot be before "
                            + "the borrowed date."
            );
        }
    }
}