package com.auca.library.dao;

import com.auca.library.domain.Borrowing;
import com.auca.library.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;
import java.util.UUID;

public class BorrowingDao {

    public Borrowing save(Borrowing borrowing) {
        Transaction transaction = null;

        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            transaction = session.beginTransaction();
            session.persist(borrowing);
            transaction.commit();

            return borrowing;
        } catch (RuntimeException exception) {
            if (transaction != null
                    && transaction.isActive()) {
                transaction.rollback();
            }

            throw exception;
        }
    }

    public Borrowing update(Borrowing borrowing) {
        Transaction transaction = null;

        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            transaction = session.beginTransaction();

            Borrowing updatedBorrowing =
                    session.merge(borrowing);

            transaction.commit();
            return updatedBorrowing;
        } catch (RuntimeException exception) {
            if (transaction != null
                    && transaction.isActive()) {
                transaction.rollback();
            }

            throw exception;
        }
    }

    public Optional<Borrowing> findById(
            UUID borrowingId
    ) {
        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            return session.createQuery(
                            """
                            SELECT borrowing
                            FROM Borrowing borrowing
                            JOIN FETCH borrowing.membership
                            JOIN FETCH borrowing.book
                            WHERE borrowing.borrowingId =
                                  :borrowingId
                            """,
                            Borrowing.class
                    )
                    .setParameter(
                            "borrowingId",
                            borrowingId
                    )
                    .uniqueResultOptional();
        }
    }

    public long countActiveBorrowingsByMembershipId(
            UUID membershipId
    ) {
        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            return session.createQuery(
                            """
                            SELECT COUNT(borrowing)
                            FROM Borrowing borrowing
                            WHERE borrowing.membership.membershipId =
                                  :membershipId
                              AND borrowing.returnDate IS NULL
                            """,
                            Long.class
                    )
                    .setParameter(
                            "membershipId",
                            membershipId
                    )
                    .getSingleResult();
        }
    }

    public Optional<Borrowing> findActiveByBookId(
            UUID bookId
    ) {
        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            return session.createQuery(
                            """
                            SELECT borrowing
                            FROM Borrowing borrowing
                            JOIN FETCH borrowing.membership
                            JOIN FETCH borrowing.book
                            WHERE borrowing.book.bookId = :bookId
                              AND borrowing.returnDate IS NULL
                            """,
                            Borrowing.class
                    )
                    .setParameter("bookId", bookId)
                    .uniqueResultOptional();
        }
    }
}