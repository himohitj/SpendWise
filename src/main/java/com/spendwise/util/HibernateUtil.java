package com.spendwise.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
                String databaseUrl = requiredEnvironmentValue("SPENDWISE_DB_URL");
                connectionProperties.setProperty("hibernate.connection.url", normalizePostgresUrl(databaseUrl));
                addOptionalEnvironmentProperty(connectionProperties, "hibernate.connection.username", "SPENDWISE_DB_USERNAME");
                addOptionalEnvironmentProperty(connectionProperties, "hibernate.connection.password", "SPENDWISE_DB_PASSWORD");
            }
            verifyDatabaseConnection(connectionProperties);
            return new Configuration().configure().addProperties(connectionProperties).buildSessionFactory();
        } catch (IOException exception) {
            throw new ExceptionInInitializerError("Could not read hibernate.properties: " + exception.getMessage());
        } catch (Exception exception) {
            throw new ExceptionInInitializerError("SessionFactory creation failed: " + exception.getMessage());
        }
    }

    private static String requiredEnvironmentValue(String environmentName) {
        String value = System.getenv(environmentName);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing " + environmentName + ". Add it as a Render environment variable or use hibernate.properties locally.");
        }
        return value.trim();
    }

    private static void addOptionalEnvironmentProperty(Properties properties, String hibernateName, String environmentName) {
        String value = System.getenv(environmentName);
        if (value != null && !value.isBlank()) properties.setProperty(hibernateName, value);
    }

    private static String normalizePostgresUrl(String databaseUrl) {
        String jdbcUrl = databaseUrl;
        if (jdbcUrl.startsWith("postgresql://") || jdbcUrl.startsWith("postgres://")) {
            jdbcUrl = "jdbc:" + jdbcUrl;
        }
        if (!jdbcUrl.startsWith("jdbc:postgresql://")) {
            throw new IllegalStateException("SPENDWISE_DB_URL must start with jdbc:postgresql:// (or postgresql://). Do not use a Supabase API URL or key.");
        }
        if (!jdbcUrl.contains("sslmode=")) {
            jdbcUrl += jdbcUrl.contains("?") ? "&sslmode=require" : "?sslmode=require";
        }
        return jdbcUrl;
    }

    private static void verifyDatabaseConnection(Properties properties) {
        String url = properties.getProperty("hibernate.connection.url");
        String username = properties.getProperty("hibernate.connection.username");
        String password = properties.getProperty("hibernate.connection.password");
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                // Opening and closing the connection is enough to verify Render can reach Supabase.
            }
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException("PostgreSQL JDBC driver is missing from the application.", exception);
        } catch (SQLException exception) {
            String message = "Could not connect to Supabase PostgreSQL. Check the Session pooler host, SPENDWISE_DB_USERNAME, and SPENDWISE_DB_PASSWORD.";
            if ("28P01".equals(exception.getSQLState())) {
                message = "Supabase rejected the database username or password. Check SPENDWISE_DB_USERNAME and SPENDWISE_DB_PASSWORD in Render.";
            }
            throw new IllegalStateException(message, exception);
        }
    }

    // A SessionFactory is expensive, so the whole application shares this one instance.
    public static SessionFactory getSessionFactory() { return SESSION_FACTORY; }
}
