package entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Owners {
    private long ownerId;
    private String name;
    private String surname;
    private long phoneeNumber;
    private String email;

    @Id
    @Column(name = "OWNER_ID")
    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
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
    @Column(name = "SURNAME")
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Basic
    @Column(name = "PHONEE_NUMBER")
    public long getPhoneeNumber() {
        return phoneeNumber;
    }

    public void setPhoneeNumber(long phoneeNumber) {
        this.phoneeNumber = phoneeNumber;
    }

    @Basic
    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Owners owners = (Owners) o;

        if (ownerId != owners.ownerId) return false;
        if (phoneeNumber != owners.phoneeNumber) return false;
        if (name != null ? !name.equals(owners.name) : owners.name != null) return false;
        if (surname != null ? !surname.equals(owners.surname) : owners.surname != null) return false;
        if (email != null ? !email.equals(owners.email) : owners.email != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (ownerId ^ (ownerId >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (int) (phoneeNumber ^ (phoneeNumber >>> 32));
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}
