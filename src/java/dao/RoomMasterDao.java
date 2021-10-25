package dao;

import domain.RoomMaster;
import enums.ERoomStatus;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Jean de Dieu@2021
 */
public class RoomMasterDao extends GenericDao<RoomMaster>{
    
    public List<RoomMaster> findByStatus(ERoomStatus st){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM RoomMaster a WHERE a.roomStatus = :st");
        q.setParameter("st", st);
        List<RoomMaster> list = q.list();
        s.close();
        return list;
    }
      
    
    public List<RoomMaster> findAllSorted(){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM RoomMaster a ORDER BY a.roomNumber ASC");
        List<RoomMaster> list = q.list();
        s.close();
        return list;
    }
      
}
