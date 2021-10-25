
package dao;

import domain.Item;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Jean de Dieu HABIMANA @2020
 */
public class ItemDao extends GenericDao<Item>{
    public List<Item> findLikeName(String key){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Item a WHERE upper(a.itemName) LIKE :name");
        q.setParameter("name", "%" + key + "%");
        List<Item> list = q.list();
        s.close();
        return list;
    }
    public List<Item> findLikeNameAndQuantity(String key, Double quant){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Item a WHERE a.quantity > :quant AND upper(a.itemName) LIKE :name ");
        q.setParameter("quant", quant);
        q.setParameter("name", "%" + key + "%");
        List<Item> list = q.list();
        s.close();
        return list;
    }
    public List<Item> findByStockState(boolean fromStock){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Item a WHERE a.isFromStock = :fromStock");
        q.setParameter("fromStock", fromStock);
        List<Item> list = q.list();
        s.close();
        return list;
    }
}
