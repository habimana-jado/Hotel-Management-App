
package dao;

import domain.Booking;
import domain.Payment;
import enums.EPaymentMode;
import enums.EType;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Jean de Dieu HABIMANA @2020
 */
public class PaymentDao extends GenericDao<Payment>{
    
    public List<Payment> findByTransactionDate(Date dt){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Payment a WHERE a.paymentDate = :dt");
        q.setParameter("dt", dt);
        List<Payment> list = q.list();
        s.close();
        return list;
    }
    
    public List<Payment> findByTransactionDateAndPaymentMode(Date dt, EPaymentMode mode){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Payment a WHERE a.paymentDate = :dt AND a.paymentMode = :mode");
        q.setParameter("dt", dt);
        q.setParameter("mode", mode);
        List<Payment> list = q.list();
        s.close();
        return list;
    }
    
    public List<Payment> findByRoomBookingAndType(Booking b, EType type){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Payment a WHERE a.roomBooking = :b AND a.paymentType = :type");
        q.setParameter("b", b);
        q.setParameter("type", type);
        List<Payment> list = q.list();
        s.close();
        return list;
    }
    
    public List<Payment> findByTransactionDateAndCredit(Date dt){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Payment a WHERE a.paymentDate = :dt AND a.amountPaidCredit > 0");
        q.setParameter("dt", dt);
        List<Payment> list = q.list();
        s.close();
        return list;
    }
    
    public List<Payment> findByTransactionDateAndCreditAndPerson(Date dt, String number){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Payment a WHERE a.paymentDate = :dt AND a.amountPaidCredit > 0 AND a.mobileNumber = :number");
        q.setParameter("dt", dt);
        q.setParameter("number", number);
        List<Payment> list = q.list();
        s.close();
        return list;
    }
    
    public List<Payment> findByTransactionDateAndDifferentPaymentMode(Date dt, EPaymentMode mode, EPaymentMode mode1, EPaymentMode mode2){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Payment a WHERE a.paymentDate = :dt AND a.paymentMode = :mode OR a.paymentMode = :mode1 OR a.paymentMode = :mode2");
        q.setParameter("dt", dt);
        q.setParameter("mode", mode);
        q.setParameter("mode1", mode1);
        q.setParameter("mode2", mode2);
        List<Payment> list = q.list();
        s.close();
        return list;
    }
    
    public List<Payment> findByTransactionDateAndDifferentPaymentModeAndMobileNumber(Date dt, EPaymentMode mode, EPaymentMode mode1, EPaymentMode mode2, String number){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Payment a WHERE a.paymentDate = :dt AND a.mobileNumber = :number AND a.paymentMode = :mode OR a.paymentMode = :mode1 OR a.paymentMode = :mode2");
        q.setParameter("dt", dt);
        q.setParameter("number", number);
        q.setParameter("mode", mode);
        q.setParameter("mode1", mode1);
        q.setParameter("mode2", mode2);
        List<Payment> list = q.list();
        s.close();
        return list;
    }
    
    public List<Payment> findByBillNo(){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT DISTINCT a.billNo, a.amountPaid, a.paymentDate, a.paymentMode FROM Payment a ");
        List<Payment> list = q.list();
        s.close();
        return list;
    }
    public List<Payment> findByStatusAndDateBetween(String status, Date from, Date to){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Payment a WHERE a.status= :status AND a.paymentDate BETWEEN :from AND :to");
        q.setParameter("status", status);
        q.setParameter("from", from);
        q.setParameter("to", to);
        List<Payment> list = q.list();        
        s.close();
        return list;
    }
    
    public List<Payment> findByStatusAndDateBetweenAndNotRoomPayment(String status, Date from, Date to){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Payment a WHERE a.status= :status AND a.paymentDate BETWEEN :from AND :to AND a.paymentType IS NULL");
        q.setParameter("status", status);
        q.setParameter("from", from);
        q.setParameter("to", to);
//        q.setParameter("type", EType.ROOM);
        List<Payment> list = q.list();        
        s.close();
        return list;
    }
    public List<Payment> findByStatusAndDateBetweenAndHour(String status, Date from, Date to){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Payment a WHERE a.status= :status AND a.paymentDate BETWEEN :from AND :to");
        q.setParameter("status", status);
        q.setParameter("from", from);
        q.setParameter("to", to);
        List<Payment> list = q.list();        
        s.close();
        return list;
    }
    
    public List<Payment> findByStatusAndDateBetweenAndMobileNumber(String status, Date from, Date to){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT DISTINCT(a.mobileNumber) FROM Payment a "
                + "WHERE a.status= :status AND a.paymentDate BETWEEN :from AND :to ");
        q.setParameter("status", status);
        q.setParameter("from", from);
        q.setParameter("to", to);
        List<Payment> list = q.list();        
        s.close();
        return list;
    }
    
    public List<Payment> findByStatusAndDateBetweenAndPaymentType(String status, Date from, Date to, EType type){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Payment a WHERE a.status= :status AND a.paymentDate BETWEEN :from AND :to AND a.paymentType = :type");
        q.setParameter("status", status);
        q.setParameter("from", from);
        q.setParameter("to", to);
        q.setParameter("type", type);
        List<Payment> list = q.list();        
        s.close();
        return list;
    }
    
    
    public Payment findByBookingAndType(Booking b, EType type){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Payment a WHERE a.roomBooking = :booking AND a.paymentType = :type");
        q.setParameter("booking", b);
        q.setParameter("type", type);
        Payment list = (Payment) q.uniqueResult();
        s.close();
        return list;
    }
    
    public List<Payment> findByBookingAndStatus(Booking b, String status){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Payment a WHERE a.roomBooking = :booking AND a.status = :status");
        q.setParameter("booking", b);
        q.setParameter("status", status);
        List<Payment> list = q.list();
        s.close();
        return list;
    }
}
