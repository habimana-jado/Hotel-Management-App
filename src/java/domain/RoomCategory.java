package domain;

import enums.EStatus;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

/**
 *
 * @author Jean de Dieu HABIMANA @2021
 */
@Entity
public class RoomCategory implements Serializable{
    @Id
    private String roomCategoryId = UUID.randomUUID().toString();
    private String roomCategoryName;
    private Double price;
    @Enumerated(EnumType.STRING)
    private EStatus status;

    public String getRoomCategoryId() {
        return roomCategoryId;
    }

    public void setRoomCategoryId(String roomCategoryId) {
        this.roomCategoryId = roomCategoryId;
    }

    public String getRoomCategoryName() {
        return roomCategoryName;
    }

    public void setRoomCategoryName(String roomCategoryName) {
        this.roomCategoryName = roomCategoryName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }
    
    
}
