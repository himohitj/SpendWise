package com.spendwise.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class HibernateUtil {
    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private HibernateUtil() { }

    private static SessionFactory buildSessionFactory() {
        try (InputStream input = HibernateUtil.class.getClassLoader().getResourceAsStream("hibernate.properties")) {
            if (input == null) {
                throw new IllegalStateException("hibernate.properties is missing. Copy hibernate.properties.example and add your database connection values.");
            }
            Properties connectionProperties = new Properties();
            connectionProperties.load(input);
            return new Configuration().configure().addProperties(connectionProperties).buildSessionFactory();
        } catch (IOException exception) {
            throw new ExceptionInInitializerError("Could not read hibernate.properties: " + exception.getMessage());
        } catch (Exception exception) {
            throw new ExceptionInInitializerError("SessionFactory creation failed: " + exception.getMessage());
        }
    }

    // A SessionFactory is expensive, so the whole application shares this one instance.
    public static SessionFactory getSessionFactory() { return SESSION_FACTORY; }
}
