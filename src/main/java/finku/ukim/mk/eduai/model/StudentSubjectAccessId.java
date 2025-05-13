package finku.ukim.mk.eduai.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class StudentSubjectAccessId implements Serializable {

    private Long studentId;
    private Long subjectId;

    public StudentSubjectAccessId(Long studentId, Long subjectId) {
        this.studentId = studentId;
        this.subjectId = subjectId;
    }
}
