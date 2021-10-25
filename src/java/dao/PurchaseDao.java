
package dao;

import domain.Item;
import domain.Purchase;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author nizey
 */
public class PurchaseDao extends GenericDao<Purchase>{
    public List<Purchase> findExpenses(){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Purchase a WHERE a.item IS NULL");
        List<Purchase> list = q.list();
        s.close();
        return list;
    }
    public List<Purchase> findExpensesByDate(Date dt){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Purchase a WHERE a.item IS NULL AND a.purchaseDate = :dt");
        q.setParameter("dt", dt);
        List<Purchase> list = q.list();
        s.close();
        return list;
    }
    public List<Purchase> findItemsPurchased(){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Purchase a WHERE a.item IS NOT NULL");
//        q.setParameter("i", null);
        List<Purchase> list = q.list();
        s.close();
        return list;
    }
    public List<Purchase> findItemsPurchasedByDate(Date dt){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Purchase a WHERE a.item IS NOT NULL AND a.purchaseDate = :dt");
        q.setParameter("dt", dt);
        List<Purchase> list = q.list();
        s.close();
        return list;
    }
    
    public List<Purchase> findByItemAndDate(Date dt, Item it){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Purchase a WHERE a.item IS NOT NULL AND a.purchaseDate = :dt AND a.item = :it");
        q.setParameter("dt", dt);
        q.setParameter("it", it);
        List<Purchase> list = q.list();
        s.close();
        return list;
    }
    
    
    public List<Purchase> findItemsPurchasedByDateAndType(Date dt, String type){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Purchase a WHERE a.purchaseDate = :dt AND a.type = :type");
        q.setParameter("dt", dt);
        q.setParameter("type", type);
        List<Purchase> list = q.list();
        s.close();
        return list;
    }
    
    public List<Purchase> findItemsPurchasedByDatesBetweenAndType(Date from, Date to, String type){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Purchase a WHERE a.type = :type AND a.purchaseDate BETWEEN :from AND :to");
        q.setParameter("from", from);
        q.setParameter("to", to);
        q.setParameter("type", type);
        List<Purchase> list = q.list();
        s.close();
        return list;
    }
    public List<Purchase> findItemsPurchasedByDatesBetweenAndTypeAndItem(Date from, Date to, String type, Item it){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Purchase a WHERE a.type = :type AND a.item = :it AND a.purchaseDate BETWEEN :from AND :to");
        q.setParameter("from", from);
        q.setParameter("to", to);
        q.setParameter("it", it);
        q.setParameter("type", type);
        List<Purchase> list = q.list();
        s.close();
        return list;
    }
    public List<Purchase> findItemsPurchasedByType( String type){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Purchase a WHERE a.type = :type");
        q.setParameter("type", type);
        List<Purchase> list = q.list();
        s.close();
        return list;
    }

    
}
