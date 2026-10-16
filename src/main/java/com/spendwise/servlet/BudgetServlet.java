package com.spendwise.servlet;

import com.spendwise.service.BudgetService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/budget")
public class BudgetServlet extends HttpServlet {
    private final BudgetService budgetService = new BudgetService();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, jakarta.servlet.ServletException {
        request.setAttribute("budget", budgetService.currentBudget());
        request.getRequestDispatcher("/WEB-INF/views/budget.jsp").forward(request, response);
    }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, jakarta.servlet.ServletException {
        String error = budgetService.saveCurrentMonth(request.getParameter("limit"));
        if (error != null) { request.setAttribute("budgetError", error); request.setAttribute("enteredLimit", request.getParameter("limit")); doGet(request, response); return; }
        response.sendRedirect(request.getContextPath() + "/budget?msg=saved");
    }
}
