package finku.ukim.mk.eduai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class QuestionDto {
    private String questionText;
    private int questionType;
    private float maxPoints;
    private List<AnswerDto> answers;
}
