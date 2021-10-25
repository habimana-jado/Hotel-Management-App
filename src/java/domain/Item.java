
package domain;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Jean de Dieu HABIMANA @2020
 */
@Entity
public class Item implements Serializable {
    @Id
    private String itemId = UUID.randomUUID().toString();
    private String itemName;
    private Double unitRate;
    private String menuType;
    private double quantity;
    private Boolean isFromStock;
//    private Double purchasePrice;

    @OneToMany(mappedBy = "item")
//    @Fetch(FetchMode.SUBSELECT)
    private List<TableTransaction> tableTransaction;
            
    @ManyToOne
    private ItemCategory itemCategory;
    
    @ManyToOne
    private ItemUnit itemUnit;
    
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getUnitRate() {
        return unitRate;
    }

    public void setUnitRate(Double unitRate) {
        this.unitRate = unitRate;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }

    public ItemUnit getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(ItemUnit itemUnit) {
        this.itemUnit = itemUnit;
    }

    public List<TableTransaction> getTableTransaction() {
        return tableTransaction;
    }

    public void setTableTransaction(List<TableTransaction> tableTransaction) {
        this.tableTransaction = tableTransaction;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Boolean getIsFromStock() {
        return isFromStock;
    }

    public void setIsFromStock(Boolean isFromStock) {
        this.isFromStock = isFromStock;
    }

}
