package com.auca.library.dao;

import com.auca.library.domain.Book;
import com.auca.library.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;
import java.util.UUID;

public class BookDao {

    public Book save(Book book) {
        Transaction transaction = null;

        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();

            return book;
        } catch (RuntimeException exception) {
            if (transaction != null
                    && transaction.isActive()) {
                transaction.rollback();
            }

            throw exception;
        }
    }

    public Book update(Book book) {
        Transaction transaction = null;

        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            transaction = session.beginTransaction();

            Book updatedBook = session.merge(book);

            transaction.commit();
            return updatedBook;
        } catch (RuntimeException exception) {
            if (transaction != null
                    && transaction.isActive()) {
                transaction.rollback();
            }

            throw exception;
        }
    }

    public Optional<Book> findById(UUID bookId) {
        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            return Optional.ofNullable(
                    session.find(Book.class, bookId)
            );
        }
    }

    public Optional<Book> findByCode(String bookCode) {
        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            return session.createQuery(
                            """
                            FROM Book book
                            WHERE LOWER(book.bookCode) =
                                  LOWER(:bookCode)
                            """,
                            Book.class
                    )
                    .setParameter("bookCode", bookCode)
                    .uniqueResultOptional();
        }
    }
}