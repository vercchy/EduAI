package finku.ukim.mk.eduai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class SubjectCreateResponse {
    private long id;
    private String name;
    private String description;

    public SubjectCreateResponse(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
