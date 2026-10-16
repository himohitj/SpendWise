package com.spendwise.servlet;

import com.spendwise.model.Transaction;
import com.spendwise.service.TransactionService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/transactions/delete")
public class DeleteTransactionServlet extends HttpServlet {
    private final TransactionService transactionService = new TransactionService();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, jakarta.servlet.ServletException {
        Transaction transaction = find(request); if (transaction == null) { response.sendError(404); return; }
        request.setAttribute("transaction", transaction);
        request.getRequestDispatcher("/WEB-INF/views/delete-confirm.jsp").forward(request, response);
    }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Transaction transaction = find(request); if (transaction == null) { response.sendError(404); return; }
        transactionService.delete(transaction.getId());
        response.sendRedirect(request.getContextPath() + "/transactions?msg=deleted");
    }
    private Transaction find(HttpServletRequest request) { try { return transactionService.findById(Long.valueOf(request.getParameter("id"))); } catch (Exception exception) { return null; } }
}
