package entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class PrescriptedDrugsPK implements Serializable {
    private long visitId;
    private long drugId;

    @Column(name = "VISIT_ID")
    @Id
    public long getVisitId() {
        return visitId;
    }

    public void setVisitId(long visitId) {
        this.visitId = visitId;
    }

    @Column(name = "DRUG_ID")
    @Id
    public long getDrugId() {
        return drugId;
    }

    public void setDrugId(long drugId) {
        this.drugId = drugId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrescriptedDrugsPK that = (PrescriptedDrugsPK) o;

        if (visitId != that.visitId) return false;
        if (drugId != that.drugId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (visitId ^ (visitId >>> 32));
        result = 31 * result + (int) (drugId ^ (drugId >>> 32));
        return result;
    }
}
