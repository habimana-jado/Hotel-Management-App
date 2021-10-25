
package dto;

/**
 *
 * @author Jean de Dieu HABIMANA @2020
 */
public class TableTransactionDto {
    private String itemName;
    private Double quantity;
    private Double unitRate;
    private Double closingItemQuantity;
    
//    private Item item;

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
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

    public Double getClosingItemQuantity() {
        return closingItemQuantity;
    }

    public void setClosingItemQuantity(Double closingItemQuantity) {
        this.closingItemQuantity = closingItemQuantity;
    }

    

//    public Item getItem() {
//        return item;
//    }
//
//    public void setItem(Item item) {
//        this.item = item;
//    }

    public TableTransactionDto() {
    }

    public TableTransactionDto(String itemName, Double quantity, Double unitRate) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitRate = unitRate;
    }

    
    
}
