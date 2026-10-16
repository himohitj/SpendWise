package com.spendwise.dao;

import com.spendwise.model.Transaction;
import com.spendwise.model.TransactionType;
import com.spendwise.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TransactionDao {
    public List<Transaction> findAll(TransactionType type, Long categoryId, LocalDate from, LocalDate to) {
        StringBuilder hql = new StringBuilder("SELECT t FROM Transaction t JOIN FETCH t.category WHERE 1 = 1");
        if (type != null) hql.append(" AND t.type = :type");
        if (categoryId != null) hql.append(" AND t.category.id = :categoryId");
        if (from != null) hql.append(" AND t.transactionDate >= :from");
        if (to != null) hql.append(" AND t.transactionDate <= :to");
        hql.append(" ORDER BY t.transactionDate DESC, t.id DESC");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Transaction> query = session.createQuery(hql.toString(), Transaction.class);
            if (type != null) query.setParameter("type", type);
            if (categoryId != null) query.setParameter("categoryId", categoryId);
            if (from != null) query.setParameter("from", from);
            if (to != null) query.setParameter("to", to);
            return query.getResultList();
        }
    }

    public Transaction findById(Long id) {
        // JOIN FETCH initializes Category before the session closes, preventing LazyInitializationException in JSP.
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT t FROM Transaction t JOIN FETCH t.category WHERE t.id = :id", Transaction.class)
                    .setParameter("id", id).uniqueResult();
        }
    }

    public void save(Transaction transaction) { inTransaction(session -> session.persist(transaction)); }

    public void update(Transaction transaction) { inTransaction(session -> session.merge(transaction)); }

    public void delete(Transaction transaction) { inTransaction(session -> session.remove(session.contains(transaction) ? transaction : session.merge(transaction))); }

    public BigDecimal totalForType(TransactionType type, LocalDate from, LocalDate to) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            BigDecimal total = session.createQuery("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = :type AND t.transactionDate >= :from AND t.transactionDate < :to", BigDecimal.class)
                    .setParameter("type", type).setParameter("from", from).setParameter("to", to).uniqueResult();
            return total == null ? BigDecimal.ZERO : total;
        }
    }

    public List<Object[]> categoryExpenseTotals(LocalDate from, LocalDate to) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT t.category.name, SUM(t.amount) FROM Transaction t WHERE t.type = :type AND t.transactionDate >= :from AND t.transactionDate < :to GROUP BY t.category.name", Object[].class)
                    .setParameter("type", TransactionType.EXPENSE).setParameter("from", from).setParameter("to", to).getResultList();
        }
    }

    private void inTransaction(SessionWork work) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            org.hibernate.Transaction databaseTransaction = session.beginTransaction();
            try { work.execute(session); databaseTransaction.commit(); }
            catch (RuntimeException exception) { if (databaseTransaction.isActive()) databaseTransaction.rollback(); throw exception; }
        }
    }

    @FunctionalInterface private interface SessionWork { void execute(Session session); }
}
