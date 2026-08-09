# AUCA Library Management System

This repository contains the final project for the Software Testing and Techniques course.

## Student information

- Name: Celine Muhoracyeye
- Student ID: 27381
- Repository: `finalExam_testing_27381`

## Project purpose

The AUCA Library Management System is a Java backend application for managing physical library books, users, memberships, borrowing, geographical locations, shelves, rooms, returns, and late-return fees.

The project focuses on backend business rules and automated testing.

## Technologies

- Java 21
- Maven
- Hibernate ORM 6.5
- PostgreSQL
- JUnit 4
- BCrypt password hashing
- Git and GitHub

## Architecture

The project separates responsibilities into the following packages:

- `domain`: entities and enums
- `dao`: Hibernate database operations
- `service`: business rules and validation
- `exception`: application-specific exceptions
- `util`: Hibernate, password, and late-fee utilities

## Implemented features

- Province, District, Sector, Cell, and Village hierarchy
- Validation of location parent relationships
- Province lookup using a village ID
- Province lookup using a person ID
- User self-registration for non-librarian users
- BCrypt password hashing
- User authentication
- Gold, Silver, and Striver memberships
- Stable UUID identification for membership types
- Pending, approved, and rejected membership statuses
- Membership approval and rejection
- Membership registration and expiration dates
- Physical-book registration
- Available, borrowed, and reserved book statuses
- Automatic 14-day borrowing period
- Borrowing records initialized with zero fine
- Book availability validation
- Gold borrowing limit of five books
- Silver borrowing limit of three books
- Striver borrowing limit of two books
- Approved-membership validation
- Shelf and room registration
- Assignment of shelves to rooms
- Assignment of books to shelves
- Automatic shelf available-stock updates
- Counting books across all shelves in a room
- Finding the room with the fewest books
- Book returns
- Prevention of duplicate returns
- Membership-based late-fee calculation
- Late-fee calculation for returned and unreturned books
- Automated unit and PostgreSQL integration tests

## Membership rules

| Membership | Late fee per day | Maximum books |
|---|---:|---:|
| Gold | 50 Rwf | 5 |
| Silver | 30 Rwf | 3 |
| Striver | 10 Rwf | 2 |

A newly registered membership starts with the `PENDING` status. It must be changed to `APPROVED` before the user can borrow books.

## Borrowing rules

- Only an approved and active member can borrow.
- The physical book must be available.
- The pickup date is the current date.
- The due date is 14 days after the pickup date.
- A new borrowing record starts with a fine of zero.
- The book status changes to `BORROWED`.
- Returning a book changes its status back to `AVAILABLE`.
- An unreturned overdue book calculates its fee against the current date.

## Database

The required PostgreSQL database is:

```text
auca_library_db
```

Hibernate manages these tables:

```text
locations
library_users
memberships
rooms
shelves
books
borrowings
```

`Person` is a mapped superclass and does not have its own database table.

## Database configuration

The repository provides hibernate.cfg.xml.example as a safe configuration template.

Create the database using:

```text
database/create_database.sql
```

## Running the tests

Run all regular tests with:

```powershell
mvn clean test
```

Expected result:

```text
Tests run: 63
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

The test suite covers domain rules, service operations, Hibernate persistence, membership limits, organization of books, borrowing, returns, and late fees.

To run the PostgreSQL connection check separately:

```powershell
mvn -Dtest=DatabaseConnectionCheck test
```

## Security

- User passwords are hashed with BCrypt.
- Plain-text passwords are not saved in PostgreSQL.
- PostgreSQL credentials are excluded from GitHub.
- Only `hibernate.cfg.xml.example` is committed.
- Build output and editor settings are excluded using `.gitignore`.
