package com.spendwise.servlet;

import com.spendwise.model.Transaction;
import com.spendwise.service.TransactionForm;
import com.spendwise.service.TransactionService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet({"/transactions/add", "/transactions/edit"})
public class TransactionFormServlet extends HttpServlet {
    private final TransactionService transactionService = new TransactionService();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, jakarta.servlet.ServletException {
        if (request.getServletPath().endsWith("edit")) {
            Transaction transaction = transactionService.findById(parseId(request));
            if (transaction == null) { response.sendError(HttpServletResponse.SC_NOT_FOUND); return; }
            request.setAttribute("transaction", transaction);
        }
        showForm(request, response);
    }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, jakarta.servlet.ServletException {
        TransactionForm form = transactionService.validate(request.getParameter("amount"), request.getParameter("type"), request.getParameter("categoryId"), request.getParameter("transactionDate"), request.getParameter("description"));
        Long id = request.getServletPath().endsWith("edit") ? parseId(request) : null;
        if (!form.getErrors().isEmpty()) { request.setAttribute("form", form); request.setAttribute("editing", id != null); showForm(request, response); return; }
        if (id == null) { transactionService.create(form); response.sendRedirect(request.getContextPath() + "/transactions?msg=added"); }
        else { transactionService.update(id, form); response.sendRedirect(request.getContextPath() + "/transactions?msg=updated"); }
    }
    private void showForm(HttpServletRequest request, HttpServletResponse response) throws jakarta.servlet.ServletException, IOException {
        request.setAttribute("categories", transactionService.findCategories());
        request.getRequestDispatcher("/WEB-INF/views/transaction-form.jsp").forward(request, response);
    }
    private Long parseId(HttpServletRequest request) { try { return Long.valueOf(request.getParameter("id")); } catch (Exception exception) { return null; } }
}
