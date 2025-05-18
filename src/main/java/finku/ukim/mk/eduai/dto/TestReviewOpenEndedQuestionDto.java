package finku.ukim.mk.eduai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TestReviewOpenEndedQuestionDto extends TestReviewQuestionDto {
    private String providedOpenEndedAnswer;
    private String aiEvaluationComment;
}
