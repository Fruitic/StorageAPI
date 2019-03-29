package api.storage.dao;

import api.storage.models.StorageEntity;
import api.storage.util.Product;
import api.storage.util.SessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.Date;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@SuppressWarnings("Duplicates")
public class StorageDao {
    @SuppressWarnings("Duplicates")
    public void save(StorageEntity storageEntity) {
        Session session = SessionFactoryUtil.getSession();
        Transaction tr = session.beginTransaction();
        session.save(storageEntity);
        tr.commit();
        session.close();
    }

    public boolean canDemand(String name, int amount, Date date) {
        Session session = SessionFactoryUtil.getSession();
        String hql = "SELECT s.amount FROM StorageEntity AS s WHERE s.name <= :name AND s.date <= :date ORDER BY s.date, s.amount DESC";
        Query query = session.createQuery(hql);
        query.setParameter("date", date);
        query.setParameter("name", name);
        List<Integer> list = query.getResultList();

        int demandable = 0;
        while (demandable >= 0 && list.size() > 0) {
            demandable += list.remove(0);
        }
        demandable -= amount;
        if (demandable < 0) {
            session.close();
            return false;
        }

        hql = "SELECT s.amount FROM StorageEntity AS s WHERE s.name <= :name AND  s.date > :date ORDER BY s.date, s.amount DESC";
        query = session.createQuery(hql);
        query.setParameter("date", date);
        query.setParameter("name", name);
        list = query.getResultList();
        while (demandable >= 0 && list.size() > 0) {
            demandable += list.remove(0);
        }
        if (demandable < 0) {
            session.close();
            return false;
        }

        session.close();
        return true;
    }

    public double calcProfit(String name, Date date) {
        Session session = SessionFactoryUtil.getSession();
        String hql = "SELECT s.amount, s.price FROM StorageEntity AS s WHERE s.name <= :name AND  s.date <= :date ORDER BY s.date, s.amount DESC";
        Query query = session.createQuery(hql);
        query.setParameter("date", date);
        query.setParameter("name", name);

        List<Object[]> list = query.getResultList();
        Queue<Product> queue = new ArrayDeque<>();
        double profit = 0;

        // Так делать не надо. Ну и ладно
        for (Object[] o : list) {
            int amount = -(int)o[0];
            double price = (double)o[1];
            if (amount > 0) {
                profit += amount * price;
                Product product = queue.remove();
                while (amount > product.amount) {
                    profit += product.amount * product.price;
                    product = queue.remove();
                }
                profit += -amount * product.price;
                product.amount += amount;
                ((ArrayDeque<Product>) queue).addFirst(product);
            } else {
                queue.add(new Product(amount, price));
            }
        }
        return profit;
    }
}
