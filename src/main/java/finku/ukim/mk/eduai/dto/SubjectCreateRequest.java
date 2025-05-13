package finku.ukim.mk.eduai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SubjectCreateRequest {
    @NotBlank
    private String name;
    private String description;
    private int difficultyLevel;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
}
