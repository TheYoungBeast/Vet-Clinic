package entity;

import javax.persistence.*;

@Entity
public class MedicalTreatments {
    private long procedureId;
    private long visitId;
    private String procedureDescription;
    private String type;

    @Id
    @Column(name = "PROCEDURE_ID")
    public long getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(long procedureId) {
        this.procedureId = procedureId;
    }

    @Basic
    @Column(name = "VISIT_ID")
    public long getVisitId() {
        return visitId;
    }

    public void setVisitId(long visitId) {
        this.visitId = visitId;
    }

    @Basic
    @Column(name = "PROCEDURE_DESCRIPTION")
    public String getProcedureDescription() {
        return procedureDescription;
    }

    public void setProcedureDescription(String procedureDescription) {
        this.procedureDescription = procedureDescription;
    }

    @Basic
    @Column(name = "TYPE")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MedicalTreatments that = (MedicalTreatments) o;

        if (procedureId != that.procedureId) return false;
        if (visitId != that.visitId) return false;
        if (procedureDescription != null ? !procedureDescription.equals(that.procedureDescription) : that.procedureDescription != null)
            return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (procedureId ^ (procedureId >>> 32));
        result = 31 * result + (int) (visitId ^ (visitId >>> 32));
        result = 31 * result + (procedureDescription != null ? procedureDescription.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
