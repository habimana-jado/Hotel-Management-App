
package domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Jean de Dieu HABIMANA @2021
 */
@Entity
public class HouseKeepingService implements Serializable {
    @Id
    private String houseKeepingServiceId = UUID.randomUUID().toString();
    private Double quantity;
    @Temporal(TemporalType.DATE)
    private Date registerDate;
    private String remarks;
    private String status;
    
    @ManyToOne
    private Booking booking;
    
    @ManyToOne
    private RoomService roomService;

    public String getHouseKeepingServiceId() {
        return houseKeepingServiceId;
    }

    public void setHouseKeepingServiceId(String houseKeepingServiceId) {
        this.houseKeepingServiceId = houseKeepingServiceId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RoomService getRoomService() {
        return roomService;
    }

    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }
    
    
}
