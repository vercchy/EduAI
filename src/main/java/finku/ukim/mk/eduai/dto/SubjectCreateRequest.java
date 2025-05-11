package finku.ukim.mk.eduai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SubjectCreateRequest {
    @NotBlank
    private String name;
    private String description;
    private int difficultyLevel;
}
