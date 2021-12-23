package entity;

import javax.persistence.*;

@Entity
@IdClass(PrescriptedDrugsPK.class)
@NamedQuery(name="PrescriptedDrugs.ByVisitId", query = "select pd from PrescriptedDrugs pd where pd.visitId = ?1")
public class PrescriptedDrugs {
    private long visitId;
    private long drugId;
    private int amount;

    @Id
    @Column(name = "VISIT_ID")
    public long getVisitId() {
        return visitId;
    }

    public void setVisitId(long visitId) {
        this.visitId = visitId;
    }

    @Id
    @Column(name = "DRUG_ID")
    public long getDrugId() {
        return drugId;
    }

    public void setDrugId(long drugId) {
        this.drugId = drugId;
    }

    @Basic
    @Column(name = "AMOUNT")
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrescriptedDrugs that = (PrescriptedDrugs) o;

        if (visitId != that.visitId) return false;
        if (drugId != that.drugId) return false;
        if (amount != that.amount) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (visitId ^ (visitId >>> 32));
        result = 31 * result + (int) (drugId ^ (drugId >>> 32));
        result = 31 * result + amount;
        return result;
    }
}
