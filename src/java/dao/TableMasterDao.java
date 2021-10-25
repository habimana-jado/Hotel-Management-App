package dao;

import domain.Restaurant;
import domain.TableGroup;
import domain.TableMaster;
import domain.TableTransaction;
import enums.ETableStatus;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Jean de Dieu HABIMANA @2020
 */
public class TableMasterDao extends GenericDao<TableMaster> {

    public List<TableMaster> findAllGrouped() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM TableMaster a ORDER BY a.tableNo ASC");
        List<TableMaster> list = q.list();
        s.close();
        return list;
    }
    
    public List<TableMaster> findByStatus(ETableStatus status) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM TableMaster a WHERE a.tableStatus = :x");
        q.setParameter("x", status);
        List<TableMaster> list = q.list();
        s.close();
        return list;
    }
    
    public List<TableMaster> findByStatusAndType(ETableStatus status,String type) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM TableMaster a WHERE a.tableStatus = :x AND a.type = :type");
        q.setParameter("x", status);
        q.setParameter("type", type);
        List<TableMaster> list = q.list();
        s.close();
        return list;
    }
    
    public List<TableMaster> findByType(String type) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM TableMaster a WHERE a.type = :type ORDER BY a.tableNo ASC");
        q.setParameter("type", type);
        List<TableMaster> list = q.list();
        s.close();
        return list;
    }

    public Long findTotalByStatus(ETableStatus status) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT COUNT(a.tableNo) FROM TableMaster a WHERE a.tableStatus = :x");
        q.setParameter("x", status);
        Long list = (Long) q.uniqueResult();
        s.close();
        return list;
    }

    public List<TableMaster> findByRestaurant(Restaurant restaurant) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM TableMaster a WHERE a.tableGroup = :x");
        q.setParameter("x", restaurant);
        List<TableMaster> list = q.list();
        s.close();
        return list;
    }

    public List<TableMaster> findByTableGroup(TableGroup group) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM TableMaster a WHERE a.tableGroup = :x");
        q.setParameter("x", group);
        List<TableMaster> list = q.list();
        s.close();
        return list;
    }

    public List<?> findByTotalTableAndStatus(String status) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT m, t FROM TableMaster m, TableTransaction t, Person p WHERE t.status = :status AND t.tableMaster.tableNo = m.tableNo");
        q.setParameter("status", status);
        List<?> list = q.list();

        for (int i = 0; i < list.size(); i++) {
            Object[] row = (Object[]) list.get(i);
            TableMaster subject = (TableMaster) row[0];
            TableTransaction employee = (TableTransaction) row[1];
            System.out.println(subject.getTableNo() + employee.getTransactionId());
        }
        
        s.close();
        return list;
    }
}
