# AUCA Library Management System

This repository contains the final project for the Software Testing and
Techniques course.

## Student

- Name: Celine Muhoracyeye
- Student ID: 27381
- Repository: `finalExam_testing_27381`

## Project purpose

The application manages physical library books, users, memberships, borrowing,
return dates, shelves, rooms, and late-return fees. It is a backend project and does not require a user interface.

## Technologies

- Java 21
- Maven
- Hibernate ORM 6.5
- PostgreSQL
- JUnit 4

## Current progress

- Maven project structure created.
- Core package name created: `com.auca.library`.
- Location hierarchy types created.
- Initial JUnit tests added for the location hierarchy rules.

## Package structure

```text
src/main/java/com/auca/library/
├── LibraryApplication.java
└── domain/
    └── LocationType.java

src/test/java/com/auca/library/domain/
└── LocationTypeTest.java
```

Additional packages will be added as the project grows:

- `domain` for entities and enums
- `dao` for database operations
- `service` for business rules
- `util` for Hibernate configuration

## Running the tests

```bash
mvn clean test
```

## Database

The required PostgreSQL database name is:

```text
auca_library_db
```

