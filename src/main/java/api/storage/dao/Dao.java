package api.storage.dao;

import api.storage.models.DBEntity;
import api.storage.util.SessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Entity;
import javax.persistence.PersistenceException;

public abstract class Dao {
    public void save(DBEntity productName) throws PersistenceException {
        Session session = SessionFactoryUtil.getSession();
        Transaction tr = session.beginTransaction();
        session.save(productName);
        tr.commit();
        session.close();
    }

    public boolean drop(DBEntity storageEntity) {
        Session session = SessionFactoryUtil.getSession();
        Transaction tr = session.beginTransaction();
        session.delete(storageEntity);
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
