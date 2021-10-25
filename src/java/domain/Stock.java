
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
 * @author Jean de Dieu HABIMANA @2021
 */
@Entity
public class Stock implements Serializable{
    @Id
    private String stockId = UUID.randomUUID().toString();
    private Double initialStock;
    private Double entryQuantity;
    private Double finalStock;
    private Double totalSale;
    @Temporal(TemporalType.DATE)
    private Date stockDate;
    
    @ManyToOne
    private Item item;

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public Double getInitialStock() {
        return initialStock;
    }

    public void setInitialStock(Double initialStock) {
        this.initialStock = initialStock;
    }

    public Double getEntryQuantity() {
        return entryQuantity;
    }

    public void setEntryQuantity(Double entryQuantity) {
        this.entryQuantity = entryQuantity;
    }

    public Double getFinalStock() {
        return finalStock;
    }

    public void setFinalStock(Double finalStock) {
        this.finalStock = finalStock;
    }

    public Double getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(Double totalSale) {
        this.totalSale = totalSale;
    }

    public Date getStockDate() {
        return stockDate;
    }

    public void setStockDate(Date stockDate) {
        this.stockDate = stockDate;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
    
    
}
