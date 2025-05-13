package finku.ukim.mk.eduai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@Getter
@AllArgsConstructor
public class SubjectDTO {
    private long id;
    private String name;
    private String description;
    private List<String> assignedStudentEmails; // optional, if needed
}