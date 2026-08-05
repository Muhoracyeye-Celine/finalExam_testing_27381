package com.auca.library.dao;

import com.auca.library.domain.User;
import com.auca.library.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;
import java.util.UUID;

public class UserDao {

    public User save(User user) {
        Transaction transaction = null;

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(user);

            transaction.commit();

            return user;
        } catch (RuntimeException exception) {
            if (transaction != null) {
                transaction.rollback();
            }

            throw exception;
        }
    }

    public Optional<User> findById(UUID personId) {
        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                            """
                            SELECT libraryUser
                            FROM LibraryUser libraryUser
                            JOIN FETCH libraryUser.village
                            WHERE libraryUser.personId = :personId
                            """,
                            User.class
                    )
                    .setParameter("personId", personId)
                    .uniqueResultOptional();
        }
    }

    public Optional<User> findByUsername(String username) {
        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                            """
                            FROM LibraryUser libraryUser
                            WHERE LOWER(libraryUser.username)
                                  = LOWER(:username)
                            """,
                            User.class
                    )
                    .setParameter("username", username)
                    .uniqueResultOptional();
        }
    }
}