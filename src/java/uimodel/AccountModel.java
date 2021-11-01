package uimodel;

import dao.PersonDao;
import dao.UserDepartmentDao;
import enums.EStatus;
import domain.Person;
import domain.UserDepartment;
import java.io.IOException;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class AccountModel {

    private Person account = new Person();

    private Person userDetails = new Person();

    private PersonDao userDao = new PersonDao();

    private List<Person> users;

    private String username = new String();

    private String password = new String();

    private String userdetails = new String();

    private String sid = new String();

    private String sectid = new String();

    private Person u = new Person();


    public String login() throws IOException, Exception {
        findUser();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        if (account != null && account.getStatus().equals(EStatus.ACTIVE)) {

            switch (account.getUserDepartment().getDepartmentName()) {
                case "ADMINISTRATOR":
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("session", account);
                    ec.redirect(ec.getRequestContextPath() + "/page/Admin/employee.xhtml");
                    return "page/Admin/employee?faces-redirect=true";
                case "FRONTOFFICE":
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("session", account);
                    ec.redirect(ec.getRequestContextPath() + "/page/Frontoffice/main.xhtml");
                    return "page/Frontoffice/main.xhtml?faces-redirect=true";
                case "CASHIER":
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("session", account);
                    ec.redirect(ec.getRequestContextPath() + "/page/Cashier/main.xhtml");
                    return "page/Cashier/main.xhtml?faces-redirect=true";
                case "Back Office":
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("session", account);
                    ec.redirect(ec.getRequestContextPath() + "/page/home.xhtml");
                    return "page/home.xhtml?faces-redirect=true";
                
                case "Bar":
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("session", account);
                    ec.redirect(ec.getRequestContextPath() + "/page/home.xhtml");
                    return "page/home.xhtml?faces-redirect=true";
                case "HOUSEKEEPING":
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("session", account);
                    ec.redirect(ec.getRequestContextPath() + "/page/Housekeeping/main.xhtml");
                    return "page/Housekeeping/main.xhtml?faces-redirect=true";
                case "Security":
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("session", account);
                    ec.redirect(ec.getRequestContextPath() + "/page/home.xhtml");
                    return "page/home.xhtml?faces-redirect=true";
                
                case "SUPERADMIN":
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("session", account);
                    ec.redirect(ec.getRequestContextPath() + "/page/SuperAdmin/main.xhtml");
                    return "page/SuperAdmin/main?faces-redirect=true";
                default:
                    account = null;

                    ec.redirect(ec.getRequestContextPath() + "/index.xhtml");

                    return "/HMA/index.xhtml";
            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Wrong Password Or Username"));
            ec.redirect(ec.getRequestContextPath() + "/index.xhtml");
            return "index.xhtml";
        }

    }

    public void findUser() throws Exception {
        List<Person> accountsLogin = new PersonDao().loginencrypt(username, password);

        if (!accountsLogin.isEmpty()) {
            for (Person u : accountsLogin) {
                account = u;
            }
        } else {
            account = null;
        }
    }

    public void test(){
        UserDepartment u = new UserDepartment();
        u.setDepartmentName("Security");
        u.setStatus(EStatus.ACTIVE);
        new UserDepartmentDao().register(u);
    }
    
    public void logout() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        account = null;
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/index.xhtml");
    }

    public Person getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(Person userDetails) {
        this.userDetails = userDetails;
    }

    public PersonDao getUserDao() {
        return userDao;
    }

    public void setUserDao(PersonDao userDao) {
        this.userDao = userDao;
    }

    public List<Person> getUsers() {
        return users;
    }

    public void setUsers(List<Person> users) {
        this.users = users;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserdetails() {
        return userdetails;
    }

    public void setUserdetails(String userdetails) {
        this.userdetails = userdetails;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSectid() {
        return sectid;
    }

    public void setSectid(String sectid) {
        this.sectid = sectid;
    }

    public Person getU() {
        return u;
    }

    public void setU(Person u) {
        this.u = u;
    }

}
