
package domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Jean de Dieu HABIMANA @2021
 */
@Entity
public class Booking implements Serializable {
    @Id
    private String bookingId = UUID.randomUUID().toString();
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkInDate = new Date();
    @Temporal(TemporalType.DATE)
    private Date checkOutDate;
    private String comingFrom;
    private String goingTo;
    private String visitPurpose;
    private int totalPerson;
    private int totalMale;
    private int totalFemale;
    private int totalChildren;
    private String relationship;
    private Double negociatedPrice;
    private Timestamp checkInPeriod;
    private Timestamp checkOutPeriod;
    private int graceTime = 0;
    private int daysSpent = 0;    
    
    @ManyToOne
    private RoomMaster roomMaster;
    
    @ManyToOne
    private Visitor visitor;
    
    @ManyToOne
    private Payment payment;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getComingFrom() {
        return comingFrom;
    }

    public void setComingFrom(String comingFrom) {
        this.comingFrom = comingFrom;
    }

    public String getGoingTo() {
        return goingTo;
    }

    public void setGoingTo(String goingTo) {
        this.goingTo = goingTo;
    }

    public String getVisitPurpose() {
        return visitPurpose;
    }

    public void setVisitPurpose(String visitPurpose) {
        this.visitPurpose = visitPurpose;
    }

    public int getTotalPerson() {
        return totalPerson;
    }

    public void setTotalPerson(int totalPerson) {
        this.totalPerson = totalPerson;
    }

    public int getTotalMale() {
        return totalMale;
    }

    public void setTotalMale(int totalMale) {
        this.totalMale = totalMale;
    }

    public int getTotalFemale() {
        return totalFemale;
    }

    public void setTotalFemale(int totalFemale) {
        this.totalFemale = totalFemale;
    }

    public int getTotalChildren() {
        return totalChildren;
    }

    public void setTotalChildren(int totalChildren) {
        this.totalChildren = totalChildren;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public RoomMaster getRoomMaster() {
        return roomMaster;
    }

    public void setRoomMaster(RoomMaster roomMaster) {
        this.roomMaster = roomMaster;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public Double getNegociatedPrice() {
        return negociatedPrice;
    }

    public void setNegociatedPrice(Double negociatedPrice) {
        this.negociatedPrice = negociatedPrice;
    }

    public Timestamp getCheckInPeriod() {
        return checkInPeriod;
    }

    public void setCheckInPeriod(Timestamp checkInPeriod) {
        this.checkInPeriod = checkInPeriod;
    }

    public Timestamp getCheckOutPeriod() {
        return checkOutPeriod;
    }

    public void setCheckOutPeriod(Timestamp checkOutPeriod) {
        this.checkOutPeriod = checkOutPeriod;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public int getGraceTime() {
        return graceTime;
    }

    public void setGraceTime(int graceTime) {
        this.graceTime = graceTime;
    }

    public int getDaysSpent() {
        return daysSpent;
    }

    public void setDaysSpent(int daysSpent) {
        this.daysSpent = daysSpent;
    }
    
    
}
