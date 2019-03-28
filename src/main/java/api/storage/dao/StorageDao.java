package api.storage.dao;

import api.storage.models.StorageEntity;
import api.storage.util.SessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
}
