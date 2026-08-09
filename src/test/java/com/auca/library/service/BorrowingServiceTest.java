package com.auca.library.service;

import com.auca.library.dao.BookDao;
import com.auca.library.dao.BorrowingDao;
import com.auca.library.dao.LocationDao;
import com.auca.library.dao.MembershipDao;
import com.auca.library.dao.UserDao;
import com.auca.library.domain.Book;
import com.auca.library.domain.BookStatus;
import com.auca.library.domain.Borrower;
import com.auca.library.domain.Gender;
import com.auca.library.domain.Location;
import com.auca.library.domain.LocationType;
import com.auca.library.domain.MembershipType;
import com.auca.library.domain.Role;
import com.auca.library.domain.User;
import com.auca.library.exception.BookNotAvailableException;
import com.auca.library.exception.BorrowingLimitExceededException;
import com.auca.library.exception.InactiveMembershipException;
import com.auca.library.domain.Membership;

import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.auca.library.exception.BorrowingAlreadyReturnedException;

public class BorrowingServiceTest {

    private static UserService userService;
    private static MembershipService membershipService;
    private static BookService bookService;
    private static BorrowingService borrowingService;
    private static Location testVillage;

    @BeforeClass
    public static void prepareBorrowingServices() {
        LocationService locationService =
                new LocationService(new LocationDao());

        userService = new UserService(
                new UserDao(),
                locationService
        );

        membershipService = new MembershipService(
                new MembershipDao(),
                userService
        );

        bookService = new BookService(new BookDao());

        borrowingService = new BorrowingService(
                new BorrowingDao(),
                membershipService,
                bookService
        );

        Location province = locationService.createLocation(
                new Location(
                        uniqueCode("BP"),
                        "Borrowing Test Province",
                        LocationType.PROVINCE
                ),
                null
        );

        Location district = locationService.createLocation(
                new Location(
                        uniqueCode("BD"),
                        "Borrowing Test District",
                        LocationType.DISTRICT
                ),
                province.getLocationId()
        );

        Location sector = locationService.createLocation(
                new Location(
                        uniqueCode("BS"),
                        "Borrowing Test Sector",
                        LocationType.SECTOR
                ),
                district.getLocationId()
        );

        Location cell = locationService.createLocation(
                new Location(
                        uniqueCode("BC"),
                        "Borrowing Test Cell",
                        LocationType.CELL
                ),
                sector.getLocationId()
        );

        testVillage = locationService.createLocation(
                new Location(
                        uniqueCode("BV"),
                        "Borrowing Test Village",
                        LocationType.VILLAGE
                ),
                cell.getLocationId()
        );
    }

    @Test
    public void borrowBook_withValidMembership_succeeds() {
        User libraryUser = registerActiveMember(
                MembershipType.GOLD
        );

        Book book = registerTestBook();

        LocalDate borrowedDate = LocalDate.now();
        LocalDate dueDate = borrowedDate.plusDays(14);

        Borrower borrowing = borrowingService.borrowBook(
                libraryUser.getPersonId(),
                book.getBookId(),
                borrowedDate,
                dueDate
        );

        assertNotNull(borrowing.getBorrowingId());

        assertEquals(
                book.getBookId(),
                borrowing.getBook().getBookId()
        );

        assertEquals(
                0,
                borrowing.getLateFeeInRwandanFrancs()
        );

        assertFalse(borrowing.isReturned());

        assertEquals(
                BookStatus.BORROWED,
                borrowing.getBook().getBookStatus()
        );
    }

    @Test(expected = BookNotAvailableException.class)
    public void borrowBook_whenBookIsAlreadyBorrowed_throwsException() {
        User firstMember = registerActiveMember(
                MembershipType.GOLD
        );

        User secondMember = registerActiveMember(
                MembershipType.GOLD
        );

        Book book = registerTestBook();

        borrowingService.borrowBook(
                firstMember.getPersonId(),
                book.getBookId(),
                LocalDate.now(),
                LocalDate.now().plusDays(14)
        );

        borrowingService.borrowBook(
                secondMember.getPersonId(),
                book.getBookId(),
                LocalDate.now(),
                LocalDate.now().plusDays(14)
        );
    }

    @Test(expected = InactiveMembershipException.class)
    public void borrowBook_withExpiredMembership_throwsException() {
        User libraryUser = registerMember(
                MembershipType.SILVER,
                LocalDate.now().minusYears(2),
                LocalDate.now().minusYears(1)
        );

        Book book = registerTestBook();

        borrowingService.borrowBook(
                libraryUser.getPersonId(),
                book.getBookId(),
                LocalDate.now(),
                LocalDate.now().plusDays(14)
        );
    }

    @Test(expected = BorrowingLimitExceededException.class)
    public void goldMembership_cannotBorrowMoreThanFiveBooks() {
        borrowMaximumBooksAndAttemptOneMore(
                MembershipType.GOLD
        );
    }

    @Test(expected = BorrowingLimitExceededException.class)
    public void silverMembership_cannotBorrowMoreThanThreeBooks() {
        borrowMaximumBooksAndAttemptOneMore(
                MembershipType.SILVER
        );
    }

    @Test(expected = BorrowingLimitExceededException.class)
    public void striverMembership_cannotBorrowMoreThanTwoBooks() {
        borrowMaximumBooksAndAttemptOneMore(
                MembershipType.STRIVER
        );
    }

    @Test
public void returnBook_threeDaysLate_calculatesFeeAndMakesBookAvailable() {
    LocalDate currentDate = LocalDate.now();

    User libraryUser = registerMember(
            MembershipType.GOLD,
            currentDate.minusYears(1),
            currentDate.plusYears(1)
    );

    Book book = registerTestBook();

    LocalDate borrowedDate =
            currentDate.minusDays(20);

    LocalDate dueDate =
            currentDate.minusDays(3);

    Borrower borrowing = borrowingService.borrowBook(
            libraryUser.getPersonId(),
            book.getBookId(),
            borrowedDate,
            dueDate
    );

    Borrower returnedBorrowing =
            borrowingService.returnBook(
                    borrowing.getBorrowingId(),
                    currentDate
            );

    assertTrue(returnedBorrowing.isReturned());

    assertEquals(
            currentDate,
            returnedBorrowing.getReturnDate()
    );

    assertEquals(
            150,
            returnedBorrowing
                    .getLateFeeInRwandanFrancs()
    );

    Book availableBook =
            bookService.getBookById(book.getBookId());

    assertEquals(
            BookStatus.AVAILABLE,
            availableBook.getBookStatus()
    );
}

@Test(expected = BorrowingAlreadyReturnedException.class)
public void returnBook_whenAlreadyReturned_throwsException() {
    User libraryUser = registerActiveMember(
            MembershipType.SILVER
    );

    Book book = registerTestBook();

    LocalDate borrowedDate = LocalDate.now();
    LocalDate dueDate = borrowedDate.plusDays(14);

    Borrower borrowing = borrowingService.borrowBook(
            libraryUser.getPersonId(),
            book.getBookId(),
            borrowedDate,
            dueDate
    );

    borrowingService.returnBook(
            borrowing.getBorrowingId(),
            borrowedDate
    );

    borrowingService.returnBook(
            borrowing.getBorrowingId(),
            borrowedDate
    );
}


    @Test
public void borrowBook_availableBook_createsBorrowerRecordWithZeroFine() {
    User libraryUser =
            registerActiveMember(MembershipType.GOLD);

    Book book = registerTestBook();

    Borrower borrower = borrowingService.borrowBook(
            libraryUser.getPersonId(),
            book.getBookId()
    );

    assertNotNull(borrower.getBorrowerId());

    assertEquals(
            book.getBookId(),
            borrower.getBook().getBookId()
    );

    assertEquals(0, borrower.getFine());
}

@Test
public void borrowBook_setsBookStatusToBorrowed() {
    User libraryUser =
            registerActiveMember(MembershipType.SILVER);

    Book book = registerTestBook();

    borrowingService.borrowBook(
            libraryUser.getPersonId(),
            book.getBookId()
    );

    Book updatedBook =
            bookService.getBookById(book.getBookId());

    assertEquals(
            BookStatus.BORROWED,
            updatedBook.getBookStatus()
    );
}

@Test
public void borrowBook_dueDateIsPickupDatePlusLoanPeriod() {
    User libraryUser =
            registerActiveMember(MembershipType.STRIVER);

    Book book = registerTestBook();

    Borrower borrower = borrowingService.borrowBook(
            libraryUser.getPersonId(),
            book.getBookId()
    );

    LocalDate expectedDueDate =
            borrower.getPickupDate().plusDays(14);

    assertEquals(
            expectedDueDate,
            borrower.getDueDate()
    );
}


@Test
public void goldMember_withFourActiveBorrows_canBorrowAFifth() {
    User libraryUser =
            registerActiveMember(MembershipType.GOLD);

    borrowBooks(libraryUser, 4);

    Book fifthBook = registerTestBook();

    Borrower fifthBorrower =
            borrowingService.borrowBook(
                    libraryUser.getPersonId(),
                    fifthBook.getBookId()
            );

    assertNotNull(fifthBorrower.getBorrowerId());
}

@Test(expected = BorrowingLimitExceededException.class)
public void goldMember_withFiveActiveBorrows_cannotBorrowASixth() {
    User libraryUser =
            registerActiveMember(MembershipType.GOLD);

    borrowBooks(libraryUser, 5);

    borrowingService.validateBorrowLimit(
            libraryUser.getPersonId()
    );
}

@Test(expected = BorrowingLimitExceededException.class)
public void silverMember_withThreeActiveBorrows_isBlocked() {
    User libraryUser =
            registerActiveMember(MembershipType.SILVER);

    borrowBooks(libraryUser, 3);

    borrowingService.validateBorrowLimit(
            libraryUser.getPersonId()
    );
}

@Test(expected = BorrowingLimitExceededException.class)
public void striverMember_withTwoActiveBorrows_isBlocked() {
    User libraryUser =
            registerActiveMember(MembershipType.STRIVER);

    borrowBooks(libraryUser, 2);

    borrowingService.validateBorrowLimit(
            libraryUser.getPersonId()
    );
}

@Test(expected = BorrowingLimitExceededException.class)
public void userWithoutApprovedMembership_isBlocked() {
    User libraryUser =
            registerStudentWithoutMembership();

    membershipService.registerMembership(
            libraryUser.getPersonId(),
            MembershipType.GOLD.getMembershipTypeId()
    );

    borrowingService.validateBorrowLimit(
            libraryUser.getPersonId()
    );
}

private static void borrowBooks(
        User libraryUser,
        int numberOfBooks
) {
    for (int bookNumber = 0;
         bookNumber < numberOfBooks;
         bookNumber++) {

        Book book = registerTestBook();

        borrowingService.borrowBook(
                libraryUser.getPersonId(),
                book.getBookId()
        );
    }
}

private static User registerStudentWithoutMembership() {
    return userService.registerUser(
            "Celine",
            "Muhoracyeye",
            Gender.FEMALE,
            "0788000000",
            uniqueUsername(),
            "Password123!",
            Role.STUDENT,
            testVillage.getLocationId()
    );
}



    private static void borrowMaximumBooksAndAttemptOneMore(
            MembershipType membershipType
    ) {
        User libraryUser =
                registerActiveMember(membershipType);

        int maximumBorrowedBooks =
                membershipType.getMaximumBorrowedBooks();

        for (int bookNumber = 0;
             bookNumber < maximumBorrowedBooks;
             bookNumber++) {

            Book book = registerTestBook();

            borrowingService.borrowBook(
                    libraryUser.getPersonId(),
                    book.getBookId(),
                    LocalDate.now(),
                    LocalDate.now().plusDays(14)
            );
        }

        Book additionalBook = registerTestBook();

        borrowingService.borrowBook(
                libraryUser.getPersonId(),
                additionalBook.getBookId(),
                LocalDate.now(),
                LocalDate.now().plusDays(14)
        );
    }

    @Test
public void returnedOnDueDate_feeIsZero() {
    LocalDate dueDate = LocalDate.now().minusDays(1);

    Borrower borrower =
            createBorrowerForLateFeeTest(
                    MembershipType.GOLD,
                    dueDate
            );

    borrowingService.returnBook(
            borrower.getBorrowerId(),
            dueDate
    );

    assertEquals(
            0,
            borrowingService.calculateLateFee(
                    borrower.getBorrowerId()
            )
    );
}

@Test
public void goldMember_returnedThreeDaysLate_feeIs150() {
    LocalDate returnDate = LocalDate.now();
    LocalDate dueDate = returnDate.minusDays(3);

    Borrower borrower =
            createBorrowerForLateFeeTest(
                    MembershipType.GOLD,
                    dueDate
            );

    borrowingService.returnBook(
            borrower.getBorrowerId(),
            returnDate
    );

    assertEquals(
            150,
            borrowingService.calculateLateFee(
                    borrower.getBorrowerId()
            )
    );
}

@Test
public void silverMember_returnedFiveDaysLate_feeIs150() {
    LocalDate returnDate = LocalDate.now();
    LocalDate dueDate = returnDate.minusDays(5);

    Borrower borrower =
            createBorrowerForLateFeeTest(
                    MembershipType.SILVER,
                    dueDate
            );

    borrowingService.returnBook(
            borrower.getBorrowerId(),
            returnDate
    );

    assertEquals(
            150,
            borrowingService.calculateLateFee(
                    borrower.getBorrowerId()
            )
    );
}

@Test
public void striverMember_returnedOneDayLate_feeIs10() {
    LocalDate returnDate = LocalDate.now();
    LocalDate dueDate = returnDate.minusDays(1);

    Borrower borrower =
            createBorrowerForLateFeeTest(
                    MembershipType.STRIVER,
                    dueDate
            );

    borrowingService.returnBook(
            borrower.getBorrowerId(),
            returnDate
    );

    assertEquals(
            10,
            borrowingService.calculateLateFee(
                    borrower.getBorrowerId()
            )
    );
}

@Test
public void notYetReturned_feeIsComputedAgainstToday() {
    LocalDate dueDate = LocalDate.now().minusDays(2);

    Borrower borrower =
            createBorrowerForLateFeeTest(
                    MembershipType.GOLD,
                    dueDate
            );

    assertFalse(borrower.isReturned());

    assertEquals(
            100,
            borrowingService.calculateLateFee(
                    borrower.getBorrowerId()
            )
    );
}

private static Borrower createBorrowerForLateFeeTest(
        MembershipType membershipType,
        LocalDate dueDate
) {
    LocalDate currentDate = LocalDate.now();

    User libraryUser = registerMember(
            membershipType,
            currentDate.minusYears(1),
            currentDate.plusYears(1)
    );

    Book book = registerTestBook();

    LocalDate pickupDate =
            dueDate.minusDays(14);

    return borrowingService.borrowBook(
            libraryUser.getPersonId(),
            book.getBookId(),
            pickupDate,
            dueDate
    );
}

    private static User registerActiveMember(
            MembershipType membershipType
    ) {
        return registerMember(
                membershipType,
                LocalDate.now(),
                LocalDate.now().plusYears(1)
        );
    }

    private static User registerMember(
            MembershipType membershipType,
            LocalDate registrationDate,
            LocalDate expirationDate
    ) {
        User libraryUser = userService.registerUser(
                "Celine",
                "Muhoracyeye",
                Gender.FEMALE,
                "0788000000",
                uniqueUsername(),
                "Password123!",
                Role.STUDENT,
                testVillage.getLocationId()
        );

        Membership membership =
        membershipService.registerMembership(
                libraryUser.getPersonId(),
                membershipType,
                registrationDate,
                expirationDate
        );

membershipService.approveMembership(
        membership.getMembershipId()
);

return libraryUser;
    }

    private static Book registerTestBook() {
        return bookService.registerBook(
                uniqueBookCode(),
                "9780134685991",
                "Software Engineering",
                "Ian Sommerville"
        );
    }

    private static String uniqueCode(String prefix) {
        return prefix
                + "-"
                + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }

    private static String uniqueUsername() {
        return "borrower_"
                + UUID.randomUUID()
                        .toString()
                        .substring(0, 8);
    }

    private static String uniqueBookCode() {
        return "borrow-"
                + UUID.randomUUID()
                        .toString()
                        .substring(0, 8);
    }
}