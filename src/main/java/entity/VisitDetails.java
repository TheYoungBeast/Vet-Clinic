package entity;

import javax.persistence.*;

@Entity
public class VisitDetails {
    private long visitId;
    private long cena;
    private String description;
    private Integer prescriptionNo;

    @Id
    @Column(name = "VISIT_ID")
    public long getVisitId() {
        return visitId;
    }

    public void setVisitId(long visitId) {
        this.visitId = visitId;
    }

    @Basic
    @Column(name = "CENA")
    public long getCena() {
        return cena;
    }

    public void setCena(long cena) {
        this.cena = cena;
    }

    @Basic
    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "PRESCRIPTION_NO")
    public Integer getPrescriptionNo() {
        return prescriptionNo;
    }

    public void setPrescriptionNo(Integer prescriptionNo) {
        this.prescriptionNo = prescriptionNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VisitDetails that = (VisitDetails) o;

        if (visitId != that.visitId) return false;
        if (cena != that.cena) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (prescriptionNo != null ? !prescriptionNo.equals(that.prescriptionNo) : that.prescriptionNo != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (visitId ^ (visitId >>> 32));
        result = 31 * result + (int) (cena ^ (cena >>> 32));
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (prescriptionNo != null ? prescriptionNo.hashCode() : 0);
        return result;
    }
}
