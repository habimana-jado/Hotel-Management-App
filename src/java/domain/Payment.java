
package domain;

import enums.EPaymentMode;
import enums.EType;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author Jean de Dieu HABIMANA @2020
 */
@Entity
public class Payment implements Serializable {
    @Id
    private String paymentId = UUID.randomUUID().toString();
    @Temporal(TemporalType.DATE)
    private Date paymentDate;
    @Enumerated(EnumType.STRING)
    private EPaymentMode paymentMode;
    private Double amountPaid = 0.0;
    private String mobileNumber;
    private String status;
    private String billNo;
    private Double amountPaidCash = 0.0;
    private Double amountPaidMomo = 0.0;
    private Double amountPaidCredit = 0.0;
    private Double amountPaidNC = 0.0;
    private Double amountPaidCard = 0.0;
    private Double amountPaidPostToRoom = 0.0;
    private Double discount = 0.0;
    @Enumerated(EnumType.STRING)
    private EType paymentType;
    private String narration;
    
    @OneToMany(mappedBy = "payment", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<TableTransaction> tableTransaction;
    
    @ManyToOne
    private Person cashier;
    
    @OneToMany(mappedBy = "payment", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Booking> booking;
    
    @ManyToOne
    private Booking roomBooking;
    
    @ManyToOne
    private HouseKeepingService houseKeepingService;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public EPaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(EPaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public List<TableTransaction> getTableTransaction() {
        return tableTransaction;
    }

    public void setTableTransaction(List<TableTransaction> tableTransaction) {
        this.tableTransaction = tableTransaction;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Person getCashier() {
        return cashier;
    }

    public void setCashier(Person cashier) {
        this.cashier = cashier;
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

    public Double getAmountPaidCredit() {
        return amountPaidCredit;
    }

    public void setAmountPaidCredit(Double amountPaidCredit) {
        this.amountPaidCredit = amountPaidCredit;
    }

    public Double getAmountPaidNC() {
        return amountPaidNC;
    }

    public void setAmountPaidNC(Double amountPaidNC) {
        this.amountPaidNC = amountPaidNC;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getAmountPaidCard() {
        return amountPaidCard;
    }

    public void setAmountPaidCard(Double amountPaidCard) {
        this.amountPaidCard = amountPaidCard;
    }

    public List<Booking> getBooking() {
        return booking;
    }

    public void setBooking(List<Booking> booking) {
        this.booking = booking;
    }

    public EType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(EType paymentType) {
        this.paymentType = paymentType;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public Double getAmountPaidPostToRoom() {
        return amountPaidPostToRoom;
    }

    public void setAmountPaidPostToRoom(Double amountPaidPostToRoom) {
        this.amountPaidPostToRoom = amountPaidPostToRoom;
    }

    public Booking getRoomBooking() {
        return roomBooking;
    }

    public void setRoomBooking(Booking roomBooking) {
        this.roomBooking = roomBooking;
    }

    public HouseKeepingService getHouseKeepingService() {
        return houseKeepingService;
    }

    public void setHouseKeepingService(HouseKeepingService houseKeepingService) {
        this.houseKeepingService = houseKeepingService;
    }
        
}
