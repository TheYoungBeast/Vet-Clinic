package entity;

import javax.persistence.*;

@Entity
@NamedQuery(name = "Veterinarians.GetAll", query = "select v from Veterinarians v")
public class Veterinarians {
    private long vetId;
    private long pwzNo;
    private String academicTitle;

    @Id
    @Column(name = "VET_ID")
    public long getVetId() {
        return vetId;
    }

    public void setVetId(long vetId) {
        this.vetId = vetId;
    }

    @Basic
    @Column(name = "PWZ_NO")
    public long getPwzNo() {
        return pwzNo;
    }

    public void setPwzNo(long pwzNo) {
        this.pwzNo = pwzNo;
    }

    @Basic
    @Column(name = "ACADEMIC_TITLE")
    public String getAcademicTitle() {
        return academicTitle;
    }

    public void setAcademicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Veterinarians that = (Veterinarians) o;

        if (vetId != that.vetId) return false;
        if (pwzNo != that.pwzNo) return false;
        if (academicTitle != null ? !academicTitle.equals(that.academicTitle) : that.academicTitle != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (vetId ^ (vetId >>> 32));
        result = 31 * result + (int) (pwzNo ^ (pwzNo >>> 32));
        result = 31 * result + (academicTitle != null ? academicTitle.hashCode() : 0);
        return result;
    }
}
