package finku.ukim.mk.eduai.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentSubjectAccessId implements Serializable {

    private Long studentId;
    private Long subjectId;
}
