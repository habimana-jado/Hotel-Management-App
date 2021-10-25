
package dao;

import domain.Visitor;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Jean de Dieu HABIMANA @2021  
 */
public class VisitorDao extends GenericDao<Visitor>{
    
    public List<Visitor> findLikeName(String key){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Visitor a WHERE upper(a.visitorName) LIKE :name ");
        q.setParameter("name", "%" + key + "%");
        List<Visitor> list = q.list();
        s.close();
        return list;
    }
    
    public List<Visitor> findLikeNameOrDocumentId(String key){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Visitor a WHERE upper(a.visitorName) LIKE :name OR UPPER(a.documentId) LIKE :nid");
        q.setParameter("name", "%" + key + "%");
        q.setParameter("nid", "%" + key + "%");
        List<Visitor> list = q.list();
        s.close();
        return list;
    }
    public List<Visitor> findByDocumentId(String key){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Visitor a WHERE UPPER(a.documentId) = :nid");
        q.setParameter("nid", key);
        List<Visitor> list = q.list();
        s.close();
        return list;
    }
}
