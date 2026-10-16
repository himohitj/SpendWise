package com.spendwise.service;

import com.spendwise.dao.BudgetDao;
import com.spendwise.model.Budget;
import java.math.BigDecimal;
import java.time.LocalDate;

public class BudgetService {
    private final BudgetDao budgetDao = new BudgetDao();
    public Budget currentBudget() { return budgetDao.findByMonth(LocalDate.now().withDayOfMonth(1)); }
    public String saveCurrentMonth(String amountText) {
        try {
            BigDecimal limit = new BigDecimal(amountText == null ? "" : amountText.trim());
            if (limit.compareTo(BigDecimal.ZERO) <= 0 || limit.scale() > 2) throw new NumberFormatException();
            Budget budget = currentBudget();
            if (budget == null) { budget = new Budget(); budget.setBudgetMonth(LocalDate.now().withDayOfMonth(1)); }
            budget.setMonthlyLimit(limit);
            budgetDao.saveOrUpdate(budget);
            return null;
        } catch (NumberFormatException exception) { return "Enter a monthly limit greater than zero with at most 2 decimal places."; }
    }
}
