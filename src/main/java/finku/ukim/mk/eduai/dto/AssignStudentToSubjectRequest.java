package finku.ukim.mk.eduai.dto;

import lombok.Getter;

@Getter
public class AssignStudentToSubjectRequest {
    private Long subjectId;
    private String studentEmail;
}
