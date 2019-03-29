package api.storage.dao;

import api.storage.models.DBEntity;
import api.storage.models.StorageEntity;
import api.storage.util.SessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.sql.Date;
import java.util.List;

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

    public List<Object[]> getList(String name, Date date) {
        Session session = SessionFactoryUtil.getSession();
        String hql = "SELECT s.amount, s.price FROM StorageEntity AS s WHERE s.name = :name AND  s.date <= :date ORDER BY s.date, s.amount DESC";
        Query query = session.createQuery(hql);
        query.setParameter("date", date);
        query.setParameter("name", name);

        return query.getResultList();
    }

    public boolean drop(StorageEntity storageEntity) {
        Session session = SessionFactoryUtil.getSession();
        Transaction tr = session.beginTransaction();
        String hql = "SELECT s.id FROM StorageEntity AS s WHERE s.name = :name " +
                "AND s.amount = :amount AND s.price = :price AND s.date = :date";
        Query query = session.createQuery(hql);
        query.setParameter("name", storageEntity.getName());
        query.setParameter("date", storageEntity.getDate());
        query.setParameter("amount", storageEntity.getAmount());
        query.setParameter("price", storageEntity.getPrice());

        List<Object> list = query.list();
        for (Object o : list) {
            storageEntity.setId((int)o);
            session.delete(storageEntity);
        }
        boolean result = true;
        try {
            tr.commit();
        } catch (PersistenceException e) {
            System.out.println("NOT DROPPED");
            result = false;
        } finally {
            session.close();
            return result;
        }
    }
}
