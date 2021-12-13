package entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@NamedQuery(name = "Patients.AllPatients", query = "select p from Patients p")
public class Patients {
    private long petId;
    private long ownerId;
    private String petName;
    private String sex;
    private String species;
    private String breed;
    private Date birthDate;

    @Id
    @Column(name = "PET_ID")
    public long getPetId() {
        return petId;
    }

    public void setPetId(long petId) {
        this.petId = petId;
    }

    @Basic
    @Column(name = "OWNER_ID")
    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    @Basic
    @Column(name = "PET_NAME")
    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    @Basic
    @Column(name = "SEX")
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Basic
    @Column(name = "SPECIES")
    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    @Basic
    @Column(name = "BREED")
    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    @Basic
    @Column(name = "BIRTH_DATE")
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return  petName + ' ' +
                sex + ' ' +
                species + ' ' +
                breed + ' ' +
                birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Patients patients = (Patients) o;

        if (petId != patients.petId) return false;
        if (ownerId != patients.ownerId) return false;
        if (petName != null ? !petName.equals(patients.petName) : patients.petName != null) return false;
        if (sex != null ? !sex.equals(patients.sex) : patients.sex != null) return false;
        if (species != null ? !species.equals(patients.species) : patients.species != null) return false;
        if (breed != null ? !breed.equals(patients.breed) : patients.breed != null) return false;
        if (birthDate != null ? !birthDate.equals(patients.birthDate) : patients.birthDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (petId ^ (petId >>> 32));
        result = 31 * result + (int) (ownerId ^ (ownerId >>> 32));
        result = 31 * result + (petName != null ? petName.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (species != null ? species.hashCode() : 0);
        result = 31 * result + (breed != null ? breed.hashCode() : 0);
        result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
        return result;
    }
}
