package com.spendwise.servlet;

import com.spendwise.model.TransactionType;
import com.spendwise.service.TransactionService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/transactions")
public class TransactionListServlet extends HttpServlet {
    private final TransactionService transactionService = new TransactionService();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, jakarta.servlet.ServletException {
        String error = null; TransactionType type = null; Long categoryId = null; LocalDate from = null; LocalDate to = null;
        try {
            if (!blank(request.getParameter("type"))) type = TransactionType.valueOf(request.getParameter("type"));
            if (!blank(request.getParameter("categoryId"))) categoryId = Long.valueOf(request.getParameter("categoryId"));
            if (!blank(request.getParameter("from"))) from = LocalDate.parse(request.getParameter("from"));
            if (!blank(request.getParameter("to"))) to = LocalDate.parse(request.getParameter("to"));
            if (from != null && to != null && from.isAfter(to)) error = "Start date cannot be after end date.";
        } catch (Exception exception) { error = "One or more filter values are invalid."; }
        request.setAttribute("categories", transactionService.findCategories());
        request.setAttribute("filterError", error);
        request.setAttribute("transactions", error == null ? transactionService.findTransactions(type, categoryId, from, to) : java.util.List.of());
        request.getRequestDispatcher("/WEB-INF/views/transactions.jsp").forward(request, response);
    }
    private boolean blank(String value) { return value == null || value.isBlank(); }
}
