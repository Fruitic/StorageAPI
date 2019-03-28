package api.storage.dao;

import api.storage.models.StorageEntity;
import api.storage.util.SessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class StorageDao {
//    public List<StorageEntity> getEntityListSortedByDate() {
//        Session session = SessionFactoryUtil.getSession();
//        List<StorageEntity> list = session.get
//    }

    @SuppressWarnings("Duplicates")
    public void save(StorageEntity storageEntity) {
        Session session = SessionFactoryUtil.getSession();
        Transaction tr = session.beginTransaction();
        session.save(storageEntity);
        tr.commit();
        session.close();
    }

    public boolean canDemand(int amount, Date date) {
        Session session = SessionFactoryUtil.getSession();
        String hql = "SELECT s.amount FROM StorageEntity AS s WHERE s.date <= :date ORDER BY s.date, s.amount DESC";
        Query query = session.createQuery(hql);
        query.setParameter("date", date);
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

        hql = "SELECT s.amount FROM StorageEntity AS s WHERE s.date > :date ORDER BY s.date, s.amount DESC";
        query = session.createQuery(hql);
        query.setParameter("date", date);
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
}
