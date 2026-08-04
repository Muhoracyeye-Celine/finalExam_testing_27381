package com.auca.library.util;

import org.hibernate.Session;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DatabaseConnectionCheck {

    @Test
    public void connectsToAucaLibraryDatabase() {
        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            String connectedDatabaseName =
                    session.doReturningWork(
                            connection -> connection.getCatalog()
                    );

            assertEquals(
                    "auca_library_db",
                    connectedDatabaseName
            );
        }
    }
}