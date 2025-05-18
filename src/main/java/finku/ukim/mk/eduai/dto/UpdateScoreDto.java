package finku.ukim.mk.eduai.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateScoreDto {
    private Long responseId;      // The Response ID
    private Float updatedScore;   // New score from the professor
}

