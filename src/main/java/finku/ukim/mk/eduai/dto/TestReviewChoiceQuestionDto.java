package finku.ukim.mk.eduai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TestReviewChoiceQuestionDto extends TestReviewQuestionDto {
    private List<TestReviewAnswerDto> allAnswersForQuestion;
    private List<Long> providedAnswerIds;
}
