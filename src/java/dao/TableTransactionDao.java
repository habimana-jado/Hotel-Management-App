
package dao;

import domain.Booking;
import domain.Item;
import domain.Person;
import domain.TableMaster;
import domain.TableTransaction;
import dto.TableTransactionDto;
import enums.EPaymentMode;
import enums.ETableStatus;
import enums.EType;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

/**
 *
 * @author Jean de Dieu HABIMANA @2020
 */
public class TableTransactionDao extends GenericDao<TableTransaction>{
    
    public List<TableTransaction> findByTableAndStatus(TableMaster table, String status){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM TableTransaction a WHERE a.status = :status AND a.tableMaster = :table");
        q.setParameter("status", status);
        q.setParameter("table", table);
        List<TableTransaction> list = q.list();
        s.close();
        return list;
    }     
    
    public List<TableTransaction> findByTableAndStatusAndPrintStatus(TableMaster table, String status, String print){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM TableTransaction a WHERE a.status = :status AND a.tableMaster = :table AND a.printStatus = :st");
        q.setParameter("status", status);
        q.setParameter("table", table);
        q.setParameter("st", print);
        List<TableTransaction> list = q.list();
        s.close();
        return list;
    }     
    
    public List<TableTransaction> findByTableAndOrStatus(TableMaster table, String status, String stat){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM TableTransaction a WHERE a.status = :status OR a.status = :stat AND a.tableMaster = :table");
        q.setParameter("status", status);
        q.setParameter("stat", stat);
        q.setParameter("table", table);
        List<TableTransaction> list = q.list();
        s.close();
        return list;
    }       
    
    public Double findTotalByTableAndStatus(TableMaster table, String status, String menuType){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT SUM(a.totalPrice) FROM TableTransaction a WHERE a.status = :status AND a.tableMaster = :table AND a.item.menuType = :type");
        q.setParameter("status", status);
        q.setParameter("table", table);
        q.setParameter("type", menuType);
        Double list = (Double) q.uniqueResult();
        s.close();
        return list;
    }
       
    public Double findTotalByDate(Date dt){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT SUM(a.totalPrice) FROM TableTransaction a WHERE a.payment.paymentDate = :x");
        q.setParameter("x", dt);
        Double list = (Double) q.uniqueResult();
        s.close();
        return list;
    }
         
    public Double findTotalByDateAndTableStatus(Date dt, String status){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT SUM(a.totalPrice) FROM TableTransaction a WHERE a.transactionDate = :x AND a.status = :stat");
        q.setParameter("x", dt);
        q.setParameter("stat", status);
        Double list = (Double) q.uniqueResult();
        s.close();
        return list;
    }
       
    public List<TableMaster> findByTableStatusAndType(ETableStatus tableStatus, String status, String type){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT DISTINCT a.tableMaster FROM TableTransaction a WHERE a.status= :status AND a.tableMaster.tableStatus = :tableStatus AND a.tableMaster.type = :type");
        q.setParameter("status", status);
        q.setParameter("tableStatus", tableStatus);
        q.setParameter("type", type);
        q.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<TableMaster> list = q.list();        
        s.close();
        return list;
    }
    
    public List<TableMaster> findByTableStatusGroupBy(ETableStatus tableStatus, String status){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT DISTINCT a.tableMaster FROM TableTransaction a WHERE a.status= :status AND a.tableMaster.tableStatus = :tableStatus GROUP BY a.tableMaster.tableGroup");
        q.setParameter("status", status);
        q.setParameter("tableStatus", tableStatus);
        q.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<TableMaster> list = q.list();        
        s.close();
        return list;
    }
    
    
    public List<TableTransaction> findByStatus(String status){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM TableTransaction a WHERE a.status= :status");
        q.setParameter("status", status);
        List<TableTransaction> list = q.list();        
        s.close();
        return list;
    }
    
    public List<TableTransaction> findByStatusAndDate(String status, Date dt){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM TableTransaction a WHERE a.status= :status AND a.payment.paymentDate = :dt");
        q.setParameter("status", status);
        q.setParameter("dt", dt);
        List<TableTransaction> list = q.list();        
        s.close();
        return list;
    }
    
    public List<TableTransactionDto> findByStatusAndDateGroupByItem(String status, Date dt){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a.item.itemId, SUM(a.quantity) FROM TableTransaction a WHERE a.status= :status AND a.payment.paymentDate = :dt GROUP BY a.item.itemId");
        q.setParameter("status", status);
        q.setParameter("dt", dt);
        List<TableTransactionDto> list = q.list();        
        s.close();
        return list;
    }
    public List<TableTransaction> findByStatusAndDateBetween(String status, Date from, Date to){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM TableTransaction a WHERE a.status= :status AND a.payment.paymentDate BETWEEN :from AND :to");
        q.setParameter("status", status);
        q.setParameter("from", from);
        q.setParameter("to", to);
        List<TableTransaction> list = q.list();        
        s.close();
        return list;
    }
    
    public List<TableTransaction> findByStatusAndDateAndPerson(String status, Date dt, Person p){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM TableTransaction a WHERE a.status= :status AND a.payment.paymentDate = :dt AND a.waiter = :w");
        q.setParameter("status", status);
        q.setParameter("dt", dt);
        q.setParameter("w", p);
        List<TableTransaction> list = q.list();        
        s.close();
        return list;
    }
    
    public List<TableTransaction> findByStatusAndDatesBetweenAndPerson(String status, Date from, Date to, Person p){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM TableTransaction a WHERE a.status= :status AND a.waiter = :w AND a.payment.paymentDate BETWEEN :from AND :to ");
        q.setParameter("status", status);
        q.setParameter("from", from);
        q.setParameter("to", to);
        q.setParameter("w", p);
        List<TableTransaction> list = q.list();        
        s.close();
        return list;
    }
    
    public List<TableTransaction> findByStatusAndDatesBetweenAndItem(String status, Date from, Date to, Item p){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM TableTransaction a WHERE a.status= :status AND a.item = :w AND a.payment.paymentDate BETWEEN :from AND :to ");
        q.setParameter("status", status);
        q.setParameter("from", from);
        q.setParameter("to", to);
        q.setParameter("w", p);
        List<TableTransaction> list = q.list();        
        s.close();
        return list;
    }
    
    public List<TableTransactionDto> findTransactionDetailsByDateBetween(String status, Date from, Date to){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT SUM(b.quantity) AS quantity, b.item.itemName AS itemName, b.item.unitRate AS unitRate FROM TableTransaction b "
                + "WHERE b.status = :status AND b.payment.paymentDate BETWEEN :from AND :to "
                + "GROUP BY b.item.itemName, b.item.unitRate");
        q.setParameter("status", status);
        q.setParameter("from", from);
        q.setParameter("to", to);
        q.setResultTransformer(Transformers.aliasToBean(TableTransactionDto.class));
        List<TableTransactionDto> list = q.list();
        s.close();
        return list;
    }
    
    public List<TableTransactionDto> findTransactionDetailsByDateAndHourBetween(String status, Date from, Date to){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT SUM(b.quantity) AS quantity, b.item.itemName AS itemName, b.item.unitRate AS unitRate FROM TableTransaction b "
                + "WHERE b.status = :status AND b.transactionDate BETWEEN :from AND :to "
                + "GROUP BY b.item.itemName, b.item.unitRate");
        q.setParameter("status", status);
        q.setParameter("from", from);
        q.setParameter("to", to);
        q.setResultTransformer(Transformers.aliasToBean(TableTransactionDto.class));
        List<TableTransactionDto> list = q.list();
        s.close();
        return list;
    }
    
    public List<TableTransactionDto> findTransactionDetailsByDateAndHourBetweenAndNotRoomPayment(String status, Date from, Date to){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT SUM(b.quantity) AS quantity, b.item.itemName AS itemName, b.item.unitRate AS unitRate FROM TableTransaction b "
                + "WHERE b.status = :status AND b.transactionDate BETWEEN :from AND :to AND b.payment.paymentType is null "
                + "GROUP BY b.item.itemName, b.item.unitRate");
        q.setParameter("status", status);
        q.setParameter("from", from);
        q.setParameter("to", to);
//        q.setParameter("type", EType.ROOM);
        q.setResultTransformer(Transformers.aliasToBean(TableTransactionDto.class));
        List<TableTransactionDto> list = q.list();
        s.close();
        return list;
    }
    
    public List<TableTransactionDto> findItemAndTransactionDetailsByDateBetween(String status, Date from, Date to){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT SUM(b.quantity) AS quantity, x.quantity AS closingItemQuantity, b.item.itemName AS itemName, b.item.unitRate AS unitRate FROM TableTransaction b, Item x "
                + "WHERE b.status = :status AND b.payment.paymentDate BETWEEN :from AND :to "
                + "GROUP BY b.item.itemName, b.item.unitRate, x.quantity");
        q.setParameter("status", status);
        q.setParameter("from", from);
        q.setParameter("to", to);
        q.setResultTransformer(Transformers.aliasToBean(TableTransactionDto.class));
        List<TableTransactionDto> list = q.list();
        s.close();
        return list;
    }
    
    public List<TableTransactionDto> findBillDetails(String status, TableMaster table){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT SUM(b.quantity) AS quantity, b.item.itemName AS itemName, b.item.unitRate AS unitRate FROM TableTransaction b WHERE b.status = :status AND b.tableMaster = :table GROUP BY b.item.itemName, b.item.unitRate");
        q.setParameter("status", status);
        q.setParameter("table", table);
        q.setResultTransformer(Transformers.aliasToBean(TableTransactionDto.class));
        List<TableTransactionDto> list = q.list();
        s.close();
        return list;
    }
    
    
    public List<TableTransaction> findByBookingAndPaymentMode(Booking b, EPaymentMode mode){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM TableTransaction a WHERE a.payment.paymentMode = :mode AND a.payment.roomBooking = :b");
        q.setParameter("b", b);
        q.setParameter("mode", mode);
        List<TableTransaction> list = q.list();
        s.close();
        return list;
    }
}
