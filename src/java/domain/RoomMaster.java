package domain;

import enums.ERoomStatus;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Jean de Dieu HABIMANA @2021
 */
@Entity
public class RoomMaster implements Serializable {
    @Id
    private String roomId = UUID.randomUUID().toString();
    private String roomNumber;
    private Boolean isNegotiated = Boolean.FALSE;
    private Double negotiatedPrice;
    @Enumerated(EnumType.STRING)
    private ERoomStatus roomStatus;
    private String cuurentBookingId;
    
    @ManyToOne
    private Visitor visitor;
    
    @ManyToOne
    private RoomCategory roomCategory;
    
    @ManyToOne
    private FloorMaster floorMaster;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Boolean getIsNegotiated() {
        return isNegotiated;
    }

    public void setIsNegotiated(Boolean isNegotiated) {
        this.isNegotiated = isNegotiated;
    }

    public Double getNegotiatedPrice() {
        return negotiatedPrice;
    }

    public void setNegotiatedPrice(Double negotiatedPrice) {
        this.negotiatedPrice = negotiatedPrice;
    }

    public ERoomStatus getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(ERoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }

    public RoomCategory getRoomCategory() {
        return roomCategory;
    }

    public void setRoomCategory(RoomCategory roomCategory) {
        this.roomCategory = roomCategory;
    }

    public FloorMaster getFloorMaster() {
        return floorMaster;
    }

    public void setFloorMaster(FloorMaster floorMaster) {
        this.floorMaster = floorMaster;
    }

    public String getCuurentBookingId() {
        return cuurentBookingId;
    }

    public void setCuurentBookingId(String cuurentBookingId) {
        this.cuurentBookingId = cuurentBookingId;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }
    
    
}
