package finku.ukim.mk.eduai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class TestReviewQuestionDto {
    private String questionText;
    private QuestionTypeDto questionTypeDto;
    private Float earnedScore;
}
