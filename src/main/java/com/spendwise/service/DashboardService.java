package com.spendwise.service;

import com.spendwise.dao.BudgetDao;
import com.spendwise.dao.TransactionDao;
import com.spendwise.model.Budget;
import com.spendwise.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DashboardService {
    private final TransactionDao transactionDao = new TransactionDao();
    private final BudgetDao budgetDao = new BudgetDao();
    public DashboardData getDashboard() {
        LocalDate start = LocalDate.now().withDayOfMonth(1);
        LocalDate next = start.plusMonths(1);
        DashboardData data = new DashboardData();
        // EnumMap is efficient and clear when keys are a fixed enum: income and expense.
        EnumMap<TransactionType, BigDecimal> allTime = new EnumMap<>(TransactionType.class);
        for (TransactionType type : TransactionType.values()) allTime.put(type, transactionDao.totalForType(type, LocalDate.of(1970, 1, 1), LocalDate.of(9999, 1, 1)));
        data.setTotalIncome(allTime.get(TransactionType.INCOME));
        data.setTotalExpenses(allTime.get(TransactionType.EXPENSE));
        data.setMonthIncome(transactionDao.totalForType(TransactionType.INCOME, start, next));
        data.setMonthExpenses(transactionDao.totalForType(TransactionType.EXPENSE, start, next));
        Budget budget = budgetDao.findByMonth(start);
        if (budget != null) data.setBudgetLimit(budget.getMonthlyLimit());
        // LinkedHashMap preserves the amount-descending order created by the sorted stream.
        Map<String, BigDecimal> categories = new LinkedHashMap<>();
        transactionDao.categoryExpenseTotals(start, next).stream()
                .sorted(Comparator.comparing(row -> (BigDecimal) row[1], Comparator.reverseOrder()))
                .forEach(row -> categories.put((String) row[0], (BigDecimal) row[1]));
        data.setCategorySpending(categories);
        return data;
    }
}
