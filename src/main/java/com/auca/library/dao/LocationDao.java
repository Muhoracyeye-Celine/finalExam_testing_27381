package com.auca.library.dao;

import com.auca.library.domain.Location;
import com.auca.library.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;
import java.util.UUID;

public class LocationDao {

    public Location save(Location location) {
        Transaction transaction = null;

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(location);

            transaction.commit();

            return location;
        } catch (RuntimeException exception) {
            if (transaction != null) {
                transaction.rollback();
            }

            throw exception;
        }
    }

    public Optional<Location> findById(UUID locationId) {
        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            Location location =
                    session.get(Location.class, locationId);

            return Optional.ofNullable(location);
        }
    }

    public Optional<Location> findByCode(String locationCode) {
        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                            """
                            FROM Location location
                            WHERE LOWER(location.locationCode)
                                  = LOWER(:locationCode)
                            """,
                            Location.class
                    )
                    .setParameter("locationCode", locationCode)
                    .uniqueResultOptional();
        }
    }
}