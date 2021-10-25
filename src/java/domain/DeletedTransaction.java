
package domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Jean de Dieu HABIMANA @2020
 */
@Entity
public class DeletedTransaction implements Serializable {
    @Id
    private String transactionId = UUID.randomUUID().toString();
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;
    private Double quantity;
    private String kotRemarks;
    private String status;
    private Double totalPrice;
    private String billNo;
    
    @ManyToOne
    private TableMaster tableMaster;
    
    @ManyToOne
    private Payment payment;

    @ManyToOne
    private Item item;
    
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getKotRemarks() {
        return kotRemarks;
    }

    public void setKotRemarks(String kotRemarks) {
        this.kotRemarks = kotRemarks;
    }

    public TableMaster getTableMaster() {
        return tableMaster;
    }

    public void setTableMaster(TableMaster tableMaster) {
        this.tableMaster = tableMaster;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    
}
