package com.spendwise.servlet;

import com.spendwise.service.DashboardService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private final DashboardService dashboardService = new DashboardService();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, jakarta.servlet.ServletException {
        request.setAttribute("dashboard", dashboardService.getDashboard());
        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}
