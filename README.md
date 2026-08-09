# AUCA Library Management System

This repository contains the final project for the Software Testing and Techniques course.

## Student information

- Name: Celine Muhoracyeye
- Student ID: 27381
- Repository: `finalExam_testing_27381`

## Project purpose

The AUCA Library Management System is a Java backend application for managing physical library books, users, memberships, borrowing, location hierarchies, shelves, rooms, book returns, and late-return fees.

The project focuses on implementing and testing business rules. It does not require a graphical user interface.

## Technologies

- Java 21
- Maven
- Hibernate ORM 6.5
- PostgreSQL
- JUnit 4
- BCrypt password hashing
- Git and GitHub

## Architecture

The project separates responsibilities into these packages:

- `domain`: database entities and enums
- `dao`: PostgreSQL database operations
- `service`: business rules and validation
- `exception`: application-specific errors
- `util`: Hibernate, password, and fee utilities

## Completed features

- PostgreSQL and Hibernate configuration
- Province, District, Sector, Cell, and Village hierarchy
- Validation of location parent relationships
- Province lookup using a village ID
- Province lookup using a person ID
- User registration
- BCrypt password hashing
- User authentication
- Gold, Silver, and Striver membership registration
- Membership date validation
- Physical-book registration
- Physical-book availability tracking
- Book borrowing
- Active-membership validation
- Membership borrowing-limit validation
- Room registration
- Shelf registration
- Assignment of shelves to rooms
- Assignment of books to shelves
- Counting books inside a room
- Finding a room with the fewest books
- Returning borrowed books
- Restoring returned books to available status
- Preventing duplicate book returns
- Membership-based late-fee calculation
- Automated unit and integration testing

## Membership rules

| Membership | Late fee per day | Maximum borrowed books |
|---|---:|---:|
| Gold | 50 Rwf | 5 |
| Silver | 30 Rwf | 3 |
| Striver | 10 Rwf | 2 |

All new borrowing records begin with a late fee of zero. A fee is charged only when a book is returned after its due date.

## Database tables

Hibernate manages these PostgreSQL tables:

```text
locations
library_users
memberships
rooms
shelves
books
borrowings
```

Relationships include:

- A user belongs to a village.
- A membership belongs to a user.
- A shelf may belong to a room.
- A physical book may belong to a shelf.
- A borrowing belongs to a membership and a physical book.

## Package structure

```text
database/
└── create_database.sql

src/main/java/com/auca/library/
├── LibraryApplication.java
├── dao/
│   ├── BookDao.java
│   ├── BorrowingDao.java
│   ├── LocationDao.java
│   ├── MembershipDao.java
│   ├── RoomDao.java
│   ├── ShelfDao.java
│   └── UserDao.java
├── domain/
│   ├── Book.java
│   ├── BookStatus.java
│   ├── Borrowing.java
│   ├── Gender.java
│   ├── Location.java
│   ├── LocationType.java
│   ├── Membership.java
│   ├── MembershipType.java
│   ├── Person.java
│   ├── Role.java
│   ├── Room.java
│   ├── Shelf.java
│   └── User.java
├── exception/
│   ├── BookNotAvailableException.java
│   ├── BookNotFoundException.java
│   ├── BorrowingAlreadyReturnedException.java
│   ├── BorrowingLimitExceededException.java
│   ├── BorrowingNotFoundException.java
│   ├── DuplicateBookCodeException.java
│   ├── DuplicateLocationCodeException.java
│   ├── DuplicateMembershipException.java
│   ├── DuplicateRoomCodeException.java
│   ├── DuplicateShelfCodeException.java
│   ├── DuplicateUsernameException.java
│   ├── InactiveMembershipException.java
│   ├── InvalidLocationHierarchyException.java
│   ├── LocationNotFoundException.java
│   ├── MembershipNotFoundException.java
│   ├── RoomNotFoundException.java
│   ├── ShelfNotFoundException.java
│   └── UserNotFoundException.java
├── service/
│   ├── BookService.java
│   ├── BorrowingService.java
│   ├── LibraryOrganizationService.java
│   ├── LocationService.java
│   ├── MembershipService.java
│   └── UserService.java
└── util/
    ├── HibernateUtil.java
    ├── LateFeeCalculator.java
    └── PasswordUtil.java

src/main/resources/
├── hibernate.cfg.xml
└── hibernate.cfg.xml.example

src/test/java/com/auca/library/
├── domain/
│   ├── BookTest.java
│   ├── LocationTypeTest.java
│   └── MembershipTypeTest.java
├── service/
│   ├── BookServiceTest.java
│   ├── BorrowingServiceTest.java
│   ├── LibraryOrganizationServiceTest.java
│   ├── LocationServiceTest.java
│   ├── MembershipServiceTest.java
│   └── UserServiceTest.java
└── util/
    ├── DatabaseConnectionCheck.java
    ├── LateFeeCalculatorTest.java
    └── PasswordUtilTest.java
```

## Database configuration

The required PostgreSQL database name is:

```text
auca_library_db
```

The repository provides `hibernate.cfg.xml.example` as a safe template.

## Running the tests

Run all automated tests with:

```bash
mvn clean test
```

The expected final result is:

```text
Tests run: 45, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

The automated suite contains unit tests and PostgreSQL integration tests.

To test only the PostgreSQL and Hibernate connection, run:

```bash
mvn -Dtest=DatabaseConnectionCheck test
```

## Security

- Passwords are never stored as plain text.
- Passwords are hashed and verified using BCrypt.
- PostgreSQL credentials are excluded from GitHub.
- The repository contains only a safe Hibernate configuration example.