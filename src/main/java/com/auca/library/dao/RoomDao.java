package com.auca.library.dao;

import com.auca.library.domain.Room;
import com.auca.library.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RoomDao {

    public Room save(Room room) {
        Transaction transaction = null;

        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            transaction = session.beginTransaction();
            session.persist(room);
            transaction.commit();

            return room;
        } catch (RuntimeException exception) {
            if (transaction != null
                    && transaction.isActive()) {
                transaction.rollback();
            }

            throw exception;
        }
    }

    public Optional<Room> findById(UUID roomId) {
        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            return Optional.ofNullable(
                    session.find(Room.class, roomId)
            );
        }
    }

    public Optional<Room> findByCode(String roomCode) {
        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            return session.createQuery(
                            """
                            FROM Room room
                            WHERE LOWER(room.roomCode) =
                                  LOWER(:roomCode)
                            """,
                            Room.class
                    )
                    .setParameter("roomCode", roomCode)
                    .uniqueResultOptional();
        }
    }

    public List<Room> findAll() {
        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            return session.createQuery(
                            """
                            FROM Room room
                            ORDER BY room.roomCode
                            """,
                            Room.class
                    )
                    .getResultList();
        }
    }

    public long countBooksByRoomId(UUID roomId) {
        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            return session.createQuery(
                            """
                            SELECT COUNT(book)
                            FROM Book book
                            WHERE book.shelf.room.roomId =
                                  :roomId
                            """,
                            Long.class
                    )
                    .setParameter("roomId", roomId)
                    .getSingleResult();
        }
    }
}