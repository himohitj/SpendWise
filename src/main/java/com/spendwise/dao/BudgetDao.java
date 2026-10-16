package com.spendwise.dao;

import com.spendwise.model.Budget;
import com.spendwise.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.time.LocalDate;

public class BudgetDao {
    public Budget findByMonth(LocalDate firstDay) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Budget b WHERE b.budgetMonth = :month", Budget.class)
                    .setParameter("month", firstDay).uniqueResult();
        }
    }

    public void saveOrUpdate(Budget budget) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try { session.merge(budget); transaction.commit(); }
            catch (RuntimeException exception) { if (transaction.isActive()) transaction.rollback(); throw exception; }
        }
    }
}
