# AUCA Library Management System

This repository contains the final project for the Software Testing and Techniques course.

## Student information

- Name: Celine Muhoracyeye
- Student ID: 27381
- Repository: `finalExam_testing_27381`

## Project purpose

The AUCA Library Management System is a Java backend application for managing physical library books, users, memberships, borrowing, library locations, shelves, rooms, and late-return fees.

The project focuses on implementing and testing business rules. It does not require a graphical user interface.

## Technologies

- Java 21
- Maven
- Hibernate ORM 6.5
- PostgreSQL
- JUnit 4
- BCrypt password hashing
- Git and GitHub

## Completed work

- Created and configured the Maven project.
- Created the PostgreSQL database named `auca_library_db`.
- Configured Hibernate and PostgreSQL.
- Created and tested the Hibernate session factory.
- Implemented the complete location hierarchy:
  - Province
  - District
  - Sector
  - Cell
  - Village
- Implemented province lookup using a village ID.
- Created `Person` as a mapped superclass.
- Created the library `User` entity.
- Implemented user registration and authentication.
- Implemented BCrypt password hashing.
- Implemented province lookup using a person ID.
- Implemented Gold, Silver, and Striver membership rules.
- Implemented membership registration and validation.
- Created physical library-book records.
- Implemented book registration and availability status.
- Implemented book borrowing.
- Enforced active-membership validation.
- Prevented unavailable books from being borrowed.
- Enforced membership borrowing limits:
  - Gold: maximum 5 books
  - Silver: maximum 3 books
  - Striver: maximum 2 books
- Set the initial late-return fee to zero.
- Added automated unit and integration tests.
- All 34 current tests pass successfully.

## Membership rules

| Membership | Late fee per day | Maximum borrowed books |
|---|---:|---:|
| Gold | 50 Rwf | 5 |
| Silver | 30 Rwf | 3 |
| Striver | 10 Rwf | 2 |


## Current database tables

Hibernate currently manages these tables:

```text
locations
library_users
memberships
books
borrowings
```

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
│   └── User.java
├── exception/
│   ├── BookNotAvailableException.java
│   ├── BookNotFoundException.java
│   ├── BorrowingLimitExceededException.java
│   ├── DuplicateBookCodeException.java
│   ├── DuplicateLocationCodeException.java
│   ├── DuplicateMembershipException.java
│   ├── DuplicateUsernameException.java
│   ├── InactiveMembershipException.java
│   ├── InvalidLocationHierarchyException.java
│   ├── LocationNotFoundException.java
│   ├── MembershipNotFoundException.java
│   └── UserNotFoundException.java
├── service/
│   ├── BookService.java
│   ├── BorrowingService.java
│   ├── LocationService.java
│   ├── MembershipService.java
│   └── UserService.java
└── util/
    ├── HibernateUtil.java
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
│   ├── LocationServiceTest.java
│   ├── MembershipServiceTest.java
│   └── UserServiceTest.java
└── util/
    ├── DatabaseConnectionCheck.java
    └── PasswordUtilTest.java
```

## Database configuration

The required PostgreSQL database name is:

```text
auca_library_db
```

The repository provides `hibernate.cfg.xml.example` as a safe configuration template. Anyone cloning the project must copy it to `hibernate.cfg.xml` and enter their own PostgreSQL username and password.

## Running the tests

Run all automated tests with:

```bash
mvn clean test
```

The expected current result is:

```text
Tests run: 34, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

To test only the PostgreSQL and Hibernate connection, run:

```bash
mvn -Dtest=DatabaseConnectionCheck test
```

## Security

User passwords are never stored as plain text. Passwords are hashed and verified using BCrypt.