# AUCA Library Management System

This repository contains the final project for the Software Testing and Techniques course.

## Student information

- Name: Celine Muhoracyeye
- Student ID: 27381
- Repository: `finalExam_testing_27381`

## Project purpose

The AUCA Library Management System is a Java backend application for managing physical library books, users, memberships, borrowing, geographical locations, shelves, rooms, returns, and late-return fees.

The project focuses on backend business rules, PostgreSQL persistence, and automated testing. A user interface is not required.

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
- Unique membership codes
- Physical-book registration
- Book edition, publication year, and publisher information
- Available, borrowed, and reserved book statuses
- Automatic 14-day borrowing period
- Borrowing records initialized with zero fine
- Book availability validation
- Gold borrowing limit of five books
- Silver borrowing limit of three books
- Striver borrowing limit of two books
- Approved and active membership validation
- Shelf and room registration
- Shelf book category and stock information
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
- The borrowing limit depends on the selected membership type.

## Database

The required PostgreSQL database is:

```text
auca_library_db
```

Hibernate manages the following tables:

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

`MembershipType` is implemented as an enum because Gold, Silver, and Striver are fixed reference values. Each membership type has a stable UUID, daily late-fee rate, and maximum borrowing limit.

Hibernate stores the selected membership type in the `memberships` table. Therefore, this implementation does not require a separate `membership_types` table.

The `Borrower` entity uses the `borrowings` table because every record represents a borrowing transaction.

## Database configuration

The repository provides `hibernate.cfg.xml.example` as a safe configuration template.

Copy:

```text
src/main/resources/hibernate.cfg.xml.example
```

to:

```text
src/main/resources/hibernate.cfg.xml
```

Replace `POSTGRES_PASSWORD` with your local PostgreSQL password.

The real `hibernate.cfg.xml` file is excluded from GitHub because it contains local PostgreSQL credentials.

Create the required database using:

```text
database/create_database.sql
```

The SQL file creates:

```text
auca_library_db
```

## Running the tests

Run all regular tests using:

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

The test suite covers:

- Domain rules
- Location hierarchy
- Service operations
- Hibernate persistence
- User authentication
- Membership registration and approval
- Membership borrowing limits
- Book registration and availability
- Shelf and room organization
- Borrowing and returns
- Late-fee calculations

Run the PostgreSQL connection check separately using:

```powershell
mvn -Dtest=DatabaseConnectionCheck test
```

## Security

- User passwords are hashed using BCrypt.
- Plain-text user passwords are not stored in PostgreSQL.
- PostgreSQL credentials are excluded from GitHub.
- Only `hibernate.cfg.xml.example` is committed.
- Build output is excluded using `.gitignore`.
- Editor-specific settings are excluded using `.gitignore`.
