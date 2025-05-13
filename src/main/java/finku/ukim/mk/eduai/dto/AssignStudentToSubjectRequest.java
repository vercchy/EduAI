package finku.ukim.mk.eduai.dto;

import lombok.Getter;

@Getter
public class AssignStudentToSubjectRequest {
    private Long subjectId;
    private String studentEmail;

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }
}
