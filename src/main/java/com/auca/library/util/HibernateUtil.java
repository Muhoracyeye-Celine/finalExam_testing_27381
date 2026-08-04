package com.auca.library.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class HibernateUtil {

    private static final SessionFactory sessionFactory =
            createSessionFactory();

    private HibernateUtil() {
        // Prevents creating unnecessary HibernateUtil objects.
    }

    private static SessionFactory createSessionFactory() {
        try {
            Configuration configuration = new Configuration();

            configuration.configure("hibernate.cfg.xml");

            return configuration.buildSessionFactory();
        } catch (Exception exception) {
            System.err.println(
                    "Failed to configure Hibernate: "
                            + exception.getMessage()
            );

            throw new ExceptionInInitializerError(exception);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (!sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}