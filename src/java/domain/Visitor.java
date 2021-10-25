
package domain;

import enums.EGender;
import enums.ETitle;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Jean de Dieu HABIMANA @2021
 */
@Entity
public class Visitor implements Serializable {
    @Id
    private final String visitorId = UUID.randomUUID().toString();
    private String visitorName;
    private String printName;
    @Enumerated(EnumType.STRING)
    private ETitle title;
    @Enumerated(EnumType.STRING)
    private EGender gender;
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    private String fatherNames;
    private String address;
    private String city;
    private String nationality;
    private String phone;
    private String email;
    private String documentType;
    private String documentId;
    private String documentProof;

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getPrintName() {
        return printName;
    }

    public void setPrintName(String printName) {
        this.printName = printName;
    }

    public ETitle getTitle() {
        return title;
    }

    public void setTitle(ETitle title) {
        this.title = title;
    }

    public EGender getGender() {
        return gender;
    }

    public void setGender(EGender gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFatherNames() {
        return fatherNames;
    }

    public void setFatherNames(String fatherNames) {
        this.fatherNames = fatherNames;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentProof() {
        return documentProof;
    }

    public void setDocumentProof(String documentProof) {
        this.documentProof = documentProof;
    }
    
    
}
