package dao;

import domain.DeletedTransaction;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Jean de Dieu Habimana @2021
 */
public class DeletedTransactionDao extends GenericDao<DeletedTransaction>{
    public List<DeletedTransaction> findByDateBetween(Date from, Date to){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM DeletedTransaction a WHERE a.transactionDate BETWEEN :from AND :to");
        q.setParameter("from", from);
        q.setParameter("to", to);
        List<DeletedTransaction> list = q.list();
        s.close();
        return list;
    }
}
