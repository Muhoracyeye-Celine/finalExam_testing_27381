package com.auca.library.dao;

import com.auca.library.domain.Membership;
import com.auca.library.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;
import java.util.UUID;

public class MembershipDao {

    public Membership save(Membership membership) {
        Transaction transaction = null;

        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            transaction = session.beginTransaction();
            session.persist(membership);
            transaction.commit();

            return membership;
        } catch (RuntimeException exception) {
            if (transaction != null
                    && transaction.isActive()) {
                transaction.rollback();
            }

            throw exception;
        }
        
    }
    public Membership update(Membership membership) {
    if (membership == null) {
        throw new IllegalArgumentException(
                "Membership is required."
        );
    }

    Transaction transaction = null;

    try (Session session = HibernateUtil
            .getSessionFactory()
            .openSession()) {

        transaction = session.beginTransaction();

        Membership updatedMembership =
                session.merge(membership);

        transaction.commit();

        return updatedMembership;
    } catch (RuntimeException exception) {
        if (transaction != null
                && transaction.isActive()) {
            transaction.rollback();
        }

        throw exception;
    }
}

    public Optional<Membership> findById(
            UUID membershipId
    ) {
        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            return session.createQuery(
                            """
                            SELECT membership
                            FROM Membership membership
                            JOIN FETCH membership.libraryUser
                            WHERE membership.membershipId =
                                  :membershipId
                            """,
                            Membership.class
                    )
                    .setParameter(
                            "membershipId",
                            membershipId
                    )
                    .uniqueResultOptional();
        }
    }

    public Optional<Membership> findByUserId(
            UUID personId
    ) {
        try (Session session = HibernateUtil
                .getSessionFactory()
                .openSession()) {

            return session.createQuery(
                            """
                            SELECT membership
                            FROM Membership membership
                            JOIN FETCH membership.libraryUser
                            WHERE membership.libraryUser.personId =
                                  :personId
                            """,
                            Membership.class
                    )
                    .setParameter("personId", personId)
                    .uniqueResultOptional();
        }
    }
}