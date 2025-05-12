package finku.ukim.mk.eduai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SubjectBasicInfoDto {
    private Long id;
    private String name;
    private String professorFullName;
}

