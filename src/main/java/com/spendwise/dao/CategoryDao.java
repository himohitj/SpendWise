package com.spendwise.dao;

import com.spendwise.model.Category;
import com.spendwise.util.HibernateUtil;
import org.hibernate.Session;
import java.util.List;

public class CategoryDao {
    public List<Category> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Category c ORDER BY c.name", Category.class).getResultList();
        }
    }

    public Category findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(Category.class, id);
        }
    }
}
