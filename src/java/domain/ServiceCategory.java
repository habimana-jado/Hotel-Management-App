
package domain;

import enums.EStatus;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Jean de Dieu HABIMANA@2021
 */
@Entity
public class ServiceCategory implements Serializable {
    @Id
    private String serviceCategoryId = UUID.randomUUID().toString();
    private String categoryName;
    @Temporal(TemporalType.DATE)
    private Date registeredDate = new Date();
    @Enumerated(EnumType.STRING)
    private EStatus status = EStatus.ACTIVE;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "serviceCategory")
    private List<RoomService> roomServices;

    public String getServiceCategoryId() {
        return serviceCategoryId;
    }

    public void setServiceCategoryId(String serviceCategoryId) {
        this.serviceCategoryId = serviceCategoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }

    public List<RoomService> getRoomServices() {
        return roomServices;
    }

    public void setRoomServices(List<RoomService> roomServices) {
        this.roomServices = roomServices;
    }
    
    
}
