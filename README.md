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
- Java 21, Maven, Hibernate ORM, PostgreSQL and JUnit configured.
- PostgreSQL database `auca_library_db` created.
- Hibernate database configuration added.
- `HibernateUtil` created to manage the Hibernate SessionFactory.
- Database connection check completed successfully.
- Location hierarchy types created.
- Initial JUnit tests added for location hierarchy rules.

## Package structure

```text
database/
└── create_database.sql

src/main/java/com/auca/library/
├── LibraryApplication.java
├── domain/
│   └── LocationType.java
└── util/
    └── HibernateUtil.java

src/main/resources/
├── hibernate.cfg.xml
└── hibernate.cfg.xml.example

src/test/java/com/auca/library/
├── domain/
│   └── LocationTypeTest.java
└── util/
    └── DatabaseConnectionCheck.java
```

The local `hibernate.cfg.xml` contains private credentials and is
excluded from GitHub through `.gitignore`. The
`hibernate.cfg.xml.example` file provides a safe configuration template.


## Running the tests

Run the normal unit tests:

```bash
mvn clean test
```

Run the PostgreSQL connection check separately:

```bash
mvn -Dtest=DatabaseConnectionCheck test
```

