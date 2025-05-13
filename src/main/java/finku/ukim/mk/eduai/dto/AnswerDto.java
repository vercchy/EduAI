package finku.ukim.mk.eduai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnswerDto {
    private String answerText;
    private Boolean isCorrect;
}
