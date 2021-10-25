
package dto;

import domain.Booking;
import domain.RoomMaster;
import domain.Visitor;
import java.sql.Timestamp;

/**
 *
 * @author Jean de Dieu HABIMANA @2021
 */
public class FrontOfficeCollectionDto {
    
    private String guestName;
    private String roomNo;
    private String paymentMode;
    private String frontDesk;
    private Double amount;
    private Timestamp checkOutPeriod;
    
    private Visitor visitor;
    private RoomMaster roomMaster;
    private Booking booking;
    private Double discount;

    private Double amountPaidCash;
    private Double amountPaidMomo;
    
    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Timestamp getCheckOutPeriod() {
        return checkOutPeriod;
    }

    public void setCheckOutPeriod(Timestamp checkOutPeriod) {
        this.checkOutPeriod = checkOutPeriod;
    }

    public String getFrontDesk() {
        return frontDesk;
    }

    public void setFrontDesk(String frontDesk) {
        this.frontDesk = frontDesk;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public RoomMaster getRoomMaster() {
        return roomMaster;
    }

    public void setRoomMaster(RoomMaster roomMaster) {
        this.roomMaster = roomMaster;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getAmountPaidCash() {
        return amountPaidCash;
    }

    public void setAmountPaidCash(Double amountPaidCash) {
        this.amountPaidCash = amountPaidCash;
    }

    public Double getAmountPaidMomo() {
        return amountPaidMomo;
    }

    public void setAmountPaidMomo(Double amountPaidMomo) {
        this.amountPaidMomo = amountPaidMomo;
    }
    
}
