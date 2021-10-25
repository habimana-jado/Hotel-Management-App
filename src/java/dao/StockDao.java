
package dao;

import domain.Item;
import domain.Person;
import domain.Stock;
import domain.TableTransaction;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;

/**
 *
 * @author Jean de Dieu HABIMANA @2020
 */
public class StockDao extends GenericDao<Stock>{
    
    public Stock findPreviousStockByItem(Date stockDate, Item i){
        stockDate = new DateTime(stockDate).minusDays(1).toDate();
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Stock a WHERE a.stockDate = :stockDate AND a.item = :i");
        q.setParameter("stockDate", stockDate);
        q.setParameter("i", i);
        Stock list = (Stock) q.uniqueResult();
        s.close();
        return list;
    }
    
    public boolean isStockAvailable(Date stockDate){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Stock a WHERE a.stockDate = :stockDate");
        q.setParameter("stockDate", stockDate);
        Stock list = (Stock) q.uniqueResult();
        s.close();
        if(list != null){
            return true;
        }else{
            return false;
        }
    }
    
    public Stock findByDateAndItem(Date stockDate, Item i){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Stock a WHERE a.stockDate = :stockDate AND a.item = :i");
        q.setParameter("stockDate", stockDate);
        q.setParameter("i", i);
        Stock list = (Stock) q.uniqueResult();
        s.close();
        return list;
    }
    
    public Stock findByItem(Item i){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Stock a WHERE a.item = :i");
        q.setParameter("i", i);
        Stock list = (Stock) q.uniqueResult();
        s.close();
        return list;
    }
    
    public List<Stock> findAllByItem(Item i){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Stock a WHERE a.item = :i");
        q.setParameter("i", i);
        List<Stock> list = q.list();
        s.close();
        return list;
    }
    
    public List<Stock> findByDate(Date i, Date x){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Stock a WHERE a.stockDate BETWEEN :i AND :x ");
        q.setParameter("i", i);
        q.setParameter("x", x);
        List<Stock> list = q.list();
        s.close();
        return list;
    }
    
}
