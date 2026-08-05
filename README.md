# AUCA Library Management System

This repository contains the final project for the Software Testing and Techniques course.

## Student information

- Name: Celine Muhoracyeye
- Student ID: 27381
- Repository: `finalExam_testing_27381`

## Project purpose

The AUCA Library Management System is a Java backend application for managing physical library books, users, memberships, borrowing, library locations, shelves, rooms, and late-return fees.

The project focuses on implementing business rules and testing them with JUnit. It does not require a graphical user interface.

## Technologies

- Java 21
- Maven
- Hibernate ORM 6.5
- PostgreSQL
- JUnit 4
- BCrypt password hashing
- Git and GitHub

## Completed work

- Created the Maven project structure.
- Created the PostgreSQL database named `auca_library_db`.
- Configured Hibernate and PostgreSQL.
- Created the Hibernate session factory.
- Created and tested the database connection.
- Implemented the location hierarchy:
  - Province
  - District
  - Sector
  - Cell
  - Village
- Enforced the correct parent relationship for every location type.
- Implemented province lookup using a village ID.
- Created `Person` as a mapped superclass.
- Created the library `User` entity.
- Implemented secure BCrypt password hashing.
- Implemented user registration.
- Implemented province lookup using a person ID.
- Implemented user authentication.
- Added automated unit and integration tests.
- All 16 current tests pass successfully.

## Current requirement progress

| Requirement | Status |
|---|---|
| Create and test the location hierarchy | Completed |
| Find province name using village ID | Completed |
| Find province name using person ID | Completed |
| Authenticate a user | Completed |
| Register a membership | Pending |
| Borrow a book | Pending |
| Validate membership borrowing limits | Pending |
| Assign a book to a shelf | Pending |
| Assign a shelf to a room | Pending |
| Count books in a room | Pending |
| Find the room with the fewest books | Pending |
| Calculate late-return fees | Pending |

## Package structure

```text
database/
└── create_database.sql

src/main/java/com/auca/library/
├── LibraryApplication.java
├── dao/
│   ├── LocationDao.java
│   └── UserDao.java
├── domain/
│   ├── Gender.java
│   ├── Location.java
│   ├── LocationType.java
│   ├── Person.java
│   ├── Role.java
│   └── User.java
├── exception/
│   ├── DuplicateLocationCodeException.java
│   ├── DuplicateUsernameException.java
│   ├── InvalidLocationHierarchyException.java
│   ├── LocationNotFoundException.java
│   └── UserNotFoundException.java
├── service/
│   ├── LocationService.java
│   └── UserService.java
└── util/
    ├── HibernateUtil.java
    └── PasswordUtil.java

src/main/resources/
├── hibernate.cfg.xml
└── hibernate.cfg.xml.example

src/test/java/com/auca/library/
├── domain/
│   └── LocationTypeTest.java
├── service/
│   ├── LocationServiceTest.java
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

The real `hibernate.cfg.xml` file contains local database credentials and is excluded from Git by `.gitignore`.

The repository provides `hibernate.cfg.xml.example` as a safe configuration template. Anyone cloning the project must copy it to `hibernate.cfg.xml` and enter their own PostgreSQL username and password.

## Running the tests

Run all automated tests with:

```bash
mvn clean test
```

The expected current result is:

```text
Tests run: 16, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

To test only the PostgreSQL and Hibernate connection, run:

```bash
mvn -Dtest=DatabaseConnectionCheck test
```

## Security

User passwords are never stored as plain text. Passwords are hashed and verified using BCrypt.