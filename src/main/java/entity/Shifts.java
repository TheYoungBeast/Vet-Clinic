package entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@NamedQuery(name = "SHIFTS.GetAll", query = "select s from Shifts s")
public class Shifts {
    private long shiftId;
    private long vetId;
    private long clinicId;
    private Timestamp startDate;
    private Timestamp endDate;

    @Id
    @Column(name = "SHIFT_ID")
    public long getShiftId() {
        return shiftId;
    }

    public void setShiftId(long shiftId) {
        this.shiftId = shiftId;
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
    @Column(name = "CLINIC_ID")
    public long getClinicId() {
        return clinicId;
    }

    public void setClinicId(long clinicId) {
        this.clinicId = clinicId;
    }

    @Basic
    @Column(name = "START_DATE")
    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    @Basic
    @Column(name = "END_DATE")
    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shifts shifts = (Shifts) o;

        if (shiftId != shifts.shiftId) return false;
        if (vetId != shifts.vetId) return false;
        if (clinicId != shifts.clinicId) return false;
        if (startDate != null ? !startDate.equals(shifts.startDate) : shifts.startDate != null) return false;
        if (endDate != null ? !endDate.equals(shifts.endDate) : shifts.endDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (shiftId ^ (shiftId >>> 32));
        result = 31 * result + (int) (vetId ^ (vetId >>> 32));
        result = 31 * result + (int) (clinicId ^ (clinicId >>> 32));
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }
}
