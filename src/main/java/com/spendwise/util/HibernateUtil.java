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
            Properties connectionProperties = new Properties();
            if (input != null) {
                connectionProperties.load(input);
            } else {
                // Render supplies these values at runtime, so a password never belongs in the deployed WAR.
                addEnvironmentProperty(connectionProperties, "hibernate.connection.url", "SPENDWISE_DB_URL");
                addOptionalEnvironmentProperty(connectionProperties, "hibernate.connection.username", "SPENDWISE_DB_USERNAME");
                addOptionalEnvironmentProperty(connectionProperties, "hibernate.connection.password", "SPENDWISE_DB_PASSWORD");
            }
            return new Configuration().configure().addProperties(connectionProperties).buildSessionFactory();
        } catch (IOException exception) {
            throw new ExceptionInInitializerError("Could not read hibernate.properties: " + exception.getMessage());
        } catch (Exception exception) {
            throw new ExceptionInInitializerError("SessionFactory creation failed: " + exception.getMessage());
        }
    }

    private static void addEnvironmentProperty(Properties properties, String hibernateName, String environmentName) {
        String value = System.getenv(environmentName);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing " + environmentName + ". Add it as a Render environment variable or use hibernate.properties locally.");
        }
        properties.setProperty(hibernateName, value);
    }

    private static void addOptionalEnvironmentProperty(Properties properties, String hibernateName, String environmentName) {
        String value = System.getenv(environmentName);
        if (value != null && !value.isBlank()) properties.setProperty(hibernateName, value);
    }

    // A SessionFactory is expensive, so the whole application shares this one instance.
    public static SessionFactory getSessionFactory() { return SESSION_FACTORY; }
}
