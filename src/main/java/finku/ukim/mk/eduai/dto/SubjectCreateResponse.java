package finku.ukim.mk.eduai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubjectCreateResponse {
    private long id;
    private String name;
    private String description;
}
