
package domain;

import enums.EPaymentMode;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Jean de Dieu HABIMANA @2021
 */
@Entity
public class Voucher implements Serializable {
    @Id
    private String voucherId = UUID.randomUUID().toString();
    private String narration;
    private Double credit;
    private Double debit;
    @Enumerated(EnumType.STRING)
    private EPaymentMode paymentMode;
    @Temporal(javax.persistence.TemporalType.DATE)
    private final Date entryDate = new Date();
    
    @ManyToOne
    private Booking booking;

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Double getDebit() {
        return debit;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }

    public EPaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(EPaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Date getEntryDate() {
        return entryDate;
    }
    
    
}
