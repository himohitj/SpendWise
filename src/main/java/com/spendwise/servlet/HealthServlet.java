package com.spendwise.servlet;

import com.spendwise.util.HibernateUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;

import java.io.IOException;

@WebServlet("/health")
public class HealthServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Render calls this endpoint. A real query verifies both Tomcat and the database connection.
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.createNativeQuery("SELECT 1").getSingleResult();
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/plain");
            response.getWriter().write("OK");
        } catch (Throwable exception) {
            getServletContext().log("Health check could not reach the database.", exception);
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Database is temporarily unavailable.");
        }
    }
}
