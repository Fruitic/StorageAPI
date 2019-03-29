package api.storage.dao;

import api.storage.models.StorageEntity;
import api.storage.util.Product;
import api.storage.util.SessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.sql.Date;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@SuppressWarnings("Duplicates")
public class StorageDao extends Dao{

    public boolean canDemand(String name, int amount, Date date) {
        Session session = SessionFactoryUtil.getSession();
        String hql = "SELECT s.amount FROM StorageEntity AS s WHERE s.name = :name AND s.date <= :date ORDER BY s.date, s.amount DESC";
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

        hql = "SELECT s.amount FROM StorageEntity AS s WHERE s.name = :name AND  s.date > :date ORDER BY s.date, s.amount DESC";
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

    public List<Object[]> calcProfit(String name, Date date) {
        Session session = SessionFactoryUtil.getSession();
        String hql = "SELECT s.amount, s.price FROM StorageEntity AS s WHERE s.name = :name AND  s.date <= :date ORDER BY s.date, s.amount DESC";
        Query query = session.createQuery(hql);
        query.setParameter("date", date);
        query.setParameter("name", name);

        return query.getResultList();
    }
}
