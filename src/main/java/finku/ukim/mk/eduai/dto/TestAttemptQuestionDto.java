package finku.ukim.mk.eduai.dto;

import finku.ukim.mk.eduai.model.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TestAttemptQuestionDto {
    private Long id;
    private String questionText;
    private QuestionTypeDto questionType;
    private Float maxPoints;
    private List<TestAttemptAnswerDto> answers;

    public TestAttemptQuestionDto(Question question, List<TestAttemptAnswerDto> answers) {
        this.id = question.getId();
        this.questionText = question.getQuestionText();
        this.questionType = new QuestionTypeDto(question.getQuestionType());
        this.maxPoints = question.getMaxPoints();
        this.answers = answers;
    }
}
