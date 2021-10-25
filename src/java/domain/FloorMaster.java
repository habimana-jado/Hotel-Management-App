package domain;

import enums.EStatus;
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
public class FloorMaster {
    @Id
    private String floorMasterId = UUID.randomUUID().toString();
    private String floorName;
    @Enumerated(EnumType.STRING)
    private EStatus status;

    public String getFloorMasterId() {
        return floorMasterId;
    }

    public void setFloorMasterId(String floorMasterId) {
        this.floorMasterId = floorMasterId;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }
    
    
}
