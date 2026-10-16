package com.spendwise.service;

import com.spendwise.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class TransactionForm {
    private final Map<String, String> errors = new LinkedHashMap<>();
    private BigDecimal amount;
    private TransactionType type;
    private Long categoryId;
    private LocalDate transactionDate;
    private String description;

    public Map<String, String> getErrors() { return errors; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
