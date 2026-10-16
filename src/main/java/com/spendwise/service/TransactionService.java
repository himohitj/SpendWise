package com.spendwise.service;

import com.spendwise.dao.CategoryDao;
import com.spendwise.dao.TransactionDao;
import com.spendwise.model.Category;
import com.spendwise.model.Transaction;
import com.spendwise.model.TransactionType;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

public class TransactionService {
    private final TransactionDao transactionDao = new TransactionDao();
    private final CategoryDao categoryDao = new CategoryDao();

    public List<Transaction> findTransactions(TransactionType type, Long categoryId, LocalDate from, LocalDate to) {
        return transactionDao.findAll(type, categoryId, from, to);
    }
    public Transaction findById(Long id) { return transactionDao.findById(id); }
    public List<Category> findCategories() { return categoryDao.findAll(); }

    public TransactionForm validate(String amountText, String typeText, String categoryText, String dateText, String description) {
        TransactionForm form = new TransactionForm();
        form.setDescription(description == null ? "" : description.trim());
        try {
            BigDecimal amount = new BigDecimal(amountText == null ? "" : amountText.trim());
            if (amount.compareTo(BigDecimal.ZERO) <= 0 || amount.scale() > 2) throw new NumberFormatException();
            form.setAmount(amount);
        } catch (NumberFormatException exception) { form.getErrors().put("amount", "Enter an amount greater than zero with at most 2 decimal places."); }
        try { form.setType(TransactionType.valueOf(typeText)); }
        catch (Exception exception) { form.getErrors().put("type", "Choose Income or Expense."); }
        try {
            Long categoryId = Long.valueOf(categoryText);
            if (categoryDao.findById(categoryId) == null) throw new NumberFormatException();
            form.setCategoryId(categoryId);
        } catch (NumberFormatException exception) { form.getErrors().put("category", "Choose a valid category."); }
        try { form.setTransactionDate(LocalDate.parse(dateText)); }
        catch (DateTimeException exception) { form.getErrors().put("date", "Enter a valid date."); }
        if (form.getDescription().length() > 255) form.getErrors().put("description", "Description cannot exceed 255 characters.");
        return form;
    }

    public void create(TransactionForm form) { transactionDao.save(toTransaction(form, new Transaction())); }
    public void update(Long id, TransactionForm form) {
        Transaction transaction = findById(id);
        if (transaction == null) throw new IllegalArgumentException("Transaction not found.");
        transactionDao.update(toTransaction(form, transaction));
    }
    public void delete(Long id) {
        Transaction transaction = findById(id);
        if (transaction == null) throw new IllegalArgumentException("Transaction not found.");
        transactionDao.delete(transaction);
    }

    private Transaction toTransaction(TransactionForm form, Transaction transaction) {
        transaction.setAmount(form.getAmount());
        transaction.setType(form.getType());
        transaction.setCategory(categoryDao.findById(form.getCategoryId()));
        transaction.setTransactionDate(form.getTransactionDate());
        transaction.setDescription(form.getDescription().isBlank() ? null : form.getDescription());
        return transaction;
    }
}
