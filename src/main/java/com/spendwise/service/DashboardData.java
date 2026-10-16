package com.spendwise.service;

import java.math.BigDecimal;
import java.util.Map;

public class DashboardData {
    private BigDecimal totalIncome = BigDecimal.ZERO;
    private BigDecimal totalExpenses = BigDecimal.ZERO;
    private BigDecimal monthIncome = BigDecimal.ZERO;
    private BigDecimal monthExpenses = BigDecimal.ZERO;
    private BigDecimal budgetLimit;
    private Map<String, BigDecimal> categorySpending;
    public BigDecimal getTotalIncome() { return totalIncome; }
    public void setTotalIncome(BigDecimal totalIncome) { this.totalIncome = totalIncome; }
    public BigDecimal getTotalExpenses() { return totalExpenses; }
    public void setTotalExpenses(BigDecimal totalExpenses) { this.totalExpenses = totalExpenses; }
    public BigDecimal getBalance() { return totalIncome.subtract(totalExpenses); }
    public BigDecimal getMonthIncome() { return monthIncome; }
    public void setMonthIncome(BigDecimal monthIncome) { this.monthIncome = monthIncome; }
    public BigDecimal getMonthExpenses() { return monthExpenses; }
    public void setMonthExpenses(BigDecimal monthExpenses) { this.monthExpenses = monthExpenses; }
    public BigDecimal getBudgetLimit() { return budgetLimit; }
    public void setBudgetLimit(BigDecimal budgetLimit) { this.budgetLimit = budgetLimit; }
    public Map<String, BigDecimal> getCategorySpending() { return categorySpending; }
    public void setCategorySpending(Map<String, BigDecimal> categorySpending) { this.categorySpending = categorySpending; }
    public boolean hasBudget() { return budgetLimit != null; }
    public BigDecimal getRemainingBudget() { return hasBudget() ? budgetLimit.subtract(monthExpenses) : BigDecimal.ZERO; }
    public int getBudgetPercent() { return !hasBudget() || budgetLimit.signum() == 0 ? 0 : Math.min(100, monthExpenses.multiply(BigDecimal.valueOf(100)).divide(budgetLimit, 0, java.math.RoundingMode.HALF_UP).intValue()); }
    public String getBudgetStatus() {
        if (!hasBudget()) return "No budget set";
        int percent = monthExpenses.multiply(BigDecimal.valueOf(100)).divide(budgetLimit, 0, java.math.RoundingMode.HALF_UP).intValue();
        if (percent > 100) return "Over budget";
        if (percent == 100) return "Budget reached";
        if (percent >= 80) return "Nearing limit";
        return "Within budget";
    }
}
