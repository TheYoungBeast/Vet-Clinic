package entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Clinics {
    private long clinicId;
    private String name;
    private String zipCode;
    private String city;
    private String address;
    private long phoneNumber;
    private String clinicType;

    @Id
    @Column(name = "CLINIC_ID")
    public long getClinicId() {
        return clinicId;
    }

    public void setClinicId(long clinicId) {
        this.clinicId = clinicId;
    }

    @Basic
    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "ZIP_CODE")
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Basic
    @Column(name = "CITY")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Basic
    @Column(name = "ADDRESS")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "PHONE_NUMBER")
    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Basic
    @Column(name = "CLINIC_TYPE")
    public String getClinicType() {
        return clinicType;
    }

    public void setClinicType(String clinicType) {
        this.clinicType = clinicType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Clinics clinics = (Clinics) o;

        if (clinicId != clinics.clinicId) return false;
        if (phoneNumber != clinics.phoneNumber) return false;
        if (name != null ? !name.equals(clinics.name) : clinics.name != null) return false;
        if (zipCode != null ? !zipCode.equals(clinics.zipCode) : clinics.zipCode != null) return false;
        if (city != null ? !city.equals(clinics.city) : clinics.city != null) return false;
        if (address != null ? !address.equals(clinics.address) : clinics.address != null) return false;
        if (clinicType != null ? !clinicType.equals(clinics.clinicType) : clinics.clinicType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (clinicId ^ (clinicId >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (zipCode != null ? zipCode.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (int) (phoneNumber ^ (phoneNumber >>> 32));
        result = 31 * result + (clinicType != null ? clinicType.hashCode() : 0);
        return result;
    }
}
