package api.storage.dao;

import api.storage.models.ProductNamesEntity;
import api.storage.util.SessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.PersistenceException;

public class ProductNamesDao {
    @SuppressWarnings("Duplicates")
    public void save(ProductNamesEntity productName) throws PersistenceException {
        Session session = SessionFactoryUtil.getSession();
        Transaction tr = session.beginTransaction();
        session.save(productName);
        tr.commit();
        session.close();
    }

    public ProductNamesEntity getByName(String name) throws PersistenceException {
        return SessionFactoryUtil.getSession().get(ProductNamesEntity.class, name);
    }
}
