
package dao;

import domain.Booking;
import domain.HouseKeepingService;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Jean de Dieu HABIMANA @2021
 */
public class HouseKeepingServiceDao extends GenericDao<HouseKeepingService>{
    
    public List<HouseKeepingService> findByBooking(Booking st){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM HouseKeepingService a WHERE a.booking = :st");
        q.setParameter("st", st);
        List<HouseKeepingService> list = q.list();
        s.close();
        return list;
    }
      
}
