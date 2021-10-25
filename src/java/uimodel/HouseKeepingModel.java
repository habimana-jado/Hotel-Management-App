
package uimodel;

import dao.RoomMasterDao;
import domain.RoomMaster;
import enums.ERoomStatus;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class HouseKeepingModel {    
    
    private List<RoomMaster> allRooms = new RoomMasterDao().findAll(RoomMaster.class);
    private RoomMaster chosenRoomMaster = new RoomMaster();
    private String nowDate = new String();
    private String status = new String();
    
    
    public List<RoomMaster> getAllRooms() {
        return allRooms;
    }

    
    public void availRoomFromCleaning() {
        if (status.equalsIgnoreCase("CLEANED")) {
            chosenRoomMaster.setRoomStatus(ERoomStatus.AVAILABLE);
        } else if (status.equalsIgnoreCase("BLOCKED")) {
            chosenRoomMaster.setRoomStatus(ERoomStatus.REPAIR);
        }else if (status.equalsIgnoreCase("CLEANING")) {
            chosenRoomMaster.setRoomStatus(ERoomStatus.CLEANING);
        }
        
        new RoomMasterDao().update(chosenRoomMaster);
        chosenRoomMaster = new RoomMaster();
        allRooms = new RoomMasterDao().findAll(RoomMaster.class);
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Updated"));
    }

    
    public void chooseRoom(RoomMaster r) {
        chosenRoomMaster = r;
        System.out.println(chosenRoomMaster.getRoomNumber());
        nowDate = new SimpleDateFormat("dd MMM yyyy hh:mm").format(new Date());
    }

    public RoomMaster getChosenRoomMaster() {
        return chosenRoomMaster;
    }

    public void setChosenRoomMaster(RoomMaster chosenRoomMaster) {
        this.chosenRoomMaster = chosenRoomMaster;
    }

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
}
