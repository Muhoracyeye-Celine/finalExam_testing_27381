package com.auca.library.dao;

import com.auca.library.domain.Shelf;
import com.auca.library.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;
import java.util.UUID;

public class ShelfDao {

    public Shelf save(Shelf shelf) {
        Transaction transaction = null;

        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            transaction = session.beginTransaction();
            session.persist(shelf);
            transaction.commit();

            return shelf;
        } catch (RuntimeException exception) {
            if (transaction != null
                    && transaction.isActive()) {
                transaction.rollback();
            }

            throw exception;
        }
    }

    public Shelf update(Shelf shelf) {
        Transaction transaction = null;

        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            transaction = session.beginTransaction();

            Shelf updatedShelf = session.merge(shelf);

            transaction.commit();
            return updatedShelf;
        } catch (RuntimeException exception) {
            if (transaction != null
                    && transaction.isActive()) {
                transaction.rollback();
            }

            throw exception;
        }
    }

    public Optional<Shelf> findById(UUID shelfId) {
        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            return session.createQuery(
                            """
                            SELECT shelf
                            FROM Shelf shelf
                            LEFT JOIN FETCH shelf.room
                            WHERE shelf.shelfId = :shelfId
                            """,
                            Shelf.class
                    )
                    .setParameter("shelfId", shelfId)
                    .uniqueResultOptional();
        }
    }

    public Optional<Shelf> findByCode(String shelfCode) {
        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            return session.createQuery(
                            """
                            SELECT shelf
                            FROM Shelf shelf
                            LEFT JOIN FETCH shelf.room
                            WHERE LOWER(shelf.shelfCode) =
                                  LOWER(:shelfCode)
                            """,
                            Shelf.class
                    )
                    .setParameter("shelfCode", shelfCode)
                    .uniqueResultOptional();
        }
    }
}