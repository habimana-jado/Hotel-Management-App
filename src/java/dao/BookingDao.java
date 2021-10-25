
package dao;

import domain.Booking;
import domain.Payment;
import dto.FrontOfficeCollectionDto;
import dto.TableTransactionDto;
import enums.EType;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

/**
 *
 * @author Jean de Dieu HABIMANA @2021
 */
public class BookingDao extends GenericDao<Booking>{
    public List<Booking> findByPayment(Payment payment){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Booking a WHERE a.payment = :payment");
        q.setParameter("payment", payment);
        List<Booking> list = q.list();
        s.close();
        return list;
    }
    
    public List<Booking> findByDateBetween(Date from, Date to){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Booking a WHERE DATE(a.checkInDate) BETWEEN :from AND :to");
        q.setParameter("from", from);
        q.setParameter("to", to);
        List<Booking> list = q.list();
        s.close();
        return list;
    }
    
    public List<Booking> findByDateBetweenAndPaid(Date from, Date to){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a FROM Booking a WHERE DATE(a.checkOutDate) BETWEEN :from AND :to AND a.payment.status = :mode");
        q.setParameter("from", from);
        q.setParameter("to", to);
        q.setParameter("mode", "Completed");
        List<Booking> list = q.list();
        s.close();
        return list;
    }
    
    public List<FrontOfficeCollectionDto> findByDateBetweenAndPaidGrouped(Date from, Date to){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT a AS booking, SUM(b.amountPaid) AS amount, SUM(b.discount) AS discount FROM Booking a, Payment b WHERE DATE(a.checkOutDate) BETWEEN :from "
                + "AND :to AND b.status = :mode GROUP BY a.bookingId, b.roomBooking");
        q.setParameter("from", from);
        q.setParameter("to", to);
        q.setParameter("mode", "Completed");
        q.setResultTransformer(Transformers.aliasToBean(FrontOfficeCollectionDto.class));
        List<FrontOfficeCollectionDto> list = q.list();
        s.close();
        return list;
    }
    
    public List<FrontOfficeCollectionDto> findByDateBetweenAndPaidGroup(Date from, Date to){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT SUM(b.amountPaid) AS amount, SUM(b.amountPaidCash) AS amountPaidCash, SUM(b.amountPaidMomo) AS amountPaidMomo, SUM(b.discount) AS discount, b.roomBooking.visitor.visitorName AS guestName, b.roomBooking.roomMaster.roomNumber AS roomNo FROM Payment b WHERE DATE(b.paymentDate) BETWEEN :from "
                + "AND :to AND b.status = :mode GROUP BY b.roomBooking.visitor.visitorName, b.roomBooking.roomMaster.roomNumber");
        q.setParameter("from", from);
        q.setParameter("to", to);
        q.setParameter("mode", "Completed");
        q.setResultTransformer(Transformers.aliasToBean(FrontOfficeCollectionDto.class));
        List<FrontOfficeCollectionDto> list = q.list();
        s.close();
        return list;
    }
    
    public List<FrontOfficeCollectionDto> findByDateBetweenAndPaidGrouped(String status, Date from, Date to){
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("SELECT SUM(b.payment.amountPaid) AS amount, b.roomMaster.roomNumber AS roomNo, b.payment.paymentMode AS paymentMode, "
                + "b.visitor.visitorName As guestName, b.payment.cashier.names AS frontDesk, b.checkOutPeriod AS checkoutPeriod FROM Booking b "
                + "WHERE b.payment.status = :status AND DATE(a.checkOutDate) BETWEEN :from AND :to"
                + "GROUP BY b.item.itemName, b.item.unitRate");
        q.setParameter("status", status);
        q.setParameter("from", from);
        q.setParameter("to", to);
        q.setResultTransformer(Transformers.aliasToBean(FrontOfficeCollectionDto.class));
        List<FrontOfficeCollectionDto> list = q.list();
        s.close();
        return list;
    }
}
