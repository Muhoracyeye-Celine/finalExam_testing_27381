package com.auca.library.service;

import com.auca.library.dao.BorrowingDao;
import com.auca.library.domain.Book;
import com.auca.library.domain.Borrower;
import com.auca.library.domain.Membership;
import com.auca.library.exception.BookNotAvailableException;
import com.auca.library.exception.BorrowingAlreadyReturnedException;
import com.auca.library.exception.BorrowingLimitExceededException;
import com.auca.library.exception.BorrowingNotFoundException;
import com.auca.library.exception.InactiveMembershipException;
import com.auca.library.exception.MembershipNotFoundException;
import com.auca.library.util.LateFeeCalculator;

import java.time.LocalDate;
import java.util.UUID;

public class BorrowingService {

    private static final int DEFAULT_LOAN_PERIOD_DAYS = 14;

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

    public void validateBorrowLimit(UUID readerId) {
        if (readerId == null) {
            throw new IllegalArgumentException(
                    "Reader ID is required."
            );
        }

        Membership membership;

        try {
            membership =
                    membershipService
                            .getMembershipByUserId(readerId);
        } catch (MembershipNotFoundException exception) {
            throw new BorrowingLimitExceededException(
                    "The reader does not have "
                            + "an approved membership."
            );
        }

        LocalDate currentDate = LocalDate.now();

        if (!membership.isApproved()
                || !membership.isActiveOn(currentDate)) {
            throw new BorrowingLimitExceededException(
                    "The reader does not have "
                            + "an approved active membership."
            );
        }

        ensureBorrowingLimitNotReached(membership);
    }

    public Borrower borrowBook(
            UUID readerId,
            UUID bookId
    ) {
        if (bookId == null) {
            throw new IllegalArgumentException(
                    "Book ID is required."
            );
        }

        validateBorrowLimit(readerId);

        LocalDate pickupDate = LocalDate.now();

        LocalDate dueDate =
                pickupDate.plusDays(
                        DEFAULT_LOAN_PERIOD_DAYS
                );

        return borrowBook(
                readerId,
                bookId,
                pickupDate,
                dueDate
        );
    }

    public Borrower borrowBook(
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

        if (!membership.isApproved()
        || !membership.isActiveOn(borrowedDate))  {
            throw new InactiveMembershipException(
        "The membership is not approved "
                + "and active on the borrowing date."
           );
        }

        ensureBorrowingLimitNotReached(membership);

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

        Borrower borrower = new Borrower(
                membership,
                book,
                borrowedDate,
                dueDate
        );

        Borrower savedBorrower =
                borrowingDao.save(borrower);

        bookService.markBookAsBorrowed(book);

        return savedBorrower;
    }

    public int calculateLateFee(UUID borrowerId) {
    Borrower borrower =
            getBorrowingById(borrowerId);

    LocalDate feeCalculationDate;

    if (borrower.isReturned()) {
        feeCalculationDate =
                borrower.getReturnDate();
    } else {
        feeCalculationDate =
                LocalDate.now();
    }

    return LateFeeCalculator.calculateLateFee(
            borrower
                    .getMembership()
                    .getMembershipType(),
            borrower.getDueDate(),
            feeCalculationDate
    );
}

    public Borrower returnBook(
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

        Borrower borrower =
                getBorrowingById(borrowingId);

        if (borrower.isReturned()) {
            throw new BorrowingAlreadyReturnedException(
                    "This borrowing has already been returned."
            );
        }

        if (returnDate.isBefore(
                borrower.getBorrowedDate()
        )) {
            throw new IllegalArgumentException(
                    "Return date cannot be before "
                            + "the borrowed date."
            );
        }

        int lateFee = LateFeeCalculator.calculateLateFee(
                borrower
                        .getMembership()
                        .getMembershipType(),
                borrower.getDueDate(),
                returnDate
        );

        borrower.markAsReturned(returnDate);
        borrower.setLateFeeInRwandanFrancs(lateFee);

        Borrower updatedBorrower =
                borrowingDao.update(borrower);

        bookService.markBookAsAvailable(
                borrower.getBook()
        );

        return updatedBorrower;
    }

    public Borrower getBorrowingById(
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

    private void ensureBorrowingLimitNotReached(
            Membership membership
    ) {
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