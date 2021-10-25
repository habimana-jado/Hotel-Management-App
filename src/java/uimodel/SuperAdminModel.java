package uimodel;

import domain.Person;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author Jean de Dieu HABIMANA @2020
 */
@ManagedBean
@SessionScoped
public class SuperAdminModel {

    private Person loggedInUser = new Person();

    @PostConstruct
    public void init() {
        userInit();
    }

    public void userInit() {
        loggedInUser = (Person) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("session");
    }

    public void redirectAdmin() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/pages/Admin/employee.xhtml");
    }

    public void redirectCashier() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/pages/Cashier/main.xhtml");
    }

    public void redirectFrontOffice() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/pages/Frontoffice/main.xhtml");
    }

    public void redirectHouseKeeping() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/pages/Housekeeping/main.xhtml");
    }

    public void redirectCashierHome() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/pages/Housekeeping/main.xhtml");
    }

    public String redirectHome() throws IOException {
        switch (loggedInUser.getUserDepartment().getDepartmentName()) {
            case "CASHIER":
            case "FRONTOFFICE":
            case "HOUSEKEEPING":
                return "main.xhtml?faces-redirect=true";
            case "ADMINISTRATOR":
                return "employee.xhtml?faces-redirect=true";
            case "SUPERADMIN":
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(ec.getRequestContextPath() + "/pages/SuperAdmin/main.xhtml");
                return "pages/SuperAdmin/main?faces-redirect=true";
            default:
                return "#";
        }
    }
}
