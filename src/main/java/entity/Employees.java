package entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Employees {
    private long usersId;
    private String name;
    private String surname;
    private long pesel;
    private String adress;
    private String position;

    @Id
    @Column(name = "USERS_ID")
    public long getUsersId() {
        return usersId;
    }

    public void setUsersId(long usersId) {
        this.usersId = usersId;
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
    @Column(name = "PESEL")
    public long getPesel() {
        return pesel;
    }

    public void setPesel(long pesel) {
        this.pesel = pesel;
    }

    @Basic
    @Column(name = "ADRESS")
    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    @Basic
    @Column(name = "POSITION")
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employees employees = (Employees) o;

        if (usersId != employees.usersId) return false;
        if (pesel != employees.pesel) return false;
        if (name != null ? !name.equals(employees.name) : employees.name != null) return false;
        if (surname != null ? !surname.equals(employees.surname) : employees.surname != null) return false;
        if (adress != null ? !adress.equals(employees.adress) : employees.adress != null) return false;
        if (position != null ? !position.equals(employees.position) : employees.position != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (usersId ^ (usersId >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (int) (pesel ^ (pesel >>> 32));
        result = 31 * result + (adress != null ? adress.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        return result;
    }
}
