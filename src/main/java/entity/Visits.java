package entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class Visits {
    private long visitId;
    private long vetId;
    private long petId;
    private Timestamp date;
    private String status;

    @Id
    @Column(name = "VISIT_ID")
    public long getVisitId() {
        return visitId;
    }

    public void setVisitId(long visitId) {
        this.visitId = visitId;
    }

    @Basic
    @Column(name = "VET_ID")
    public long getVetId() {
        return vetId;
    }

    public void setVetId(long vetId) {
        this.vetId = vetId;
    }

    @Basic
    @Column(name = "PET_ID")
    public long getPetId() {
        return petId;
    }

    public void setPetId(long petId) {
        this.petId = petId;
    }

    @Basic
    @Column(name = "VISIT_DATE")
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Basic
    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Visits visits = (Visits) o;

        if (visitId != visits.visitId) return false;
        if (vetId != visits.vetId) return false;
        if (petId != visits.petId) return false;
        if (date != null ? !date.equals(visits.date) : visits.date != null) return false;
        if (status != null ? !status.equals(visits.status) : visits.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (visitId ^ (visitId >>> 32));
        result = 31 * result + (int) (vetId ^ (vetId >>> 32));
        result = 31 * result + (int) (petId ^ (petId >>> 32));
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
