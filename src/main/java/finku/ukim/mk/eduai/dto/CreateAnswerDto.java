package finku.ukim.mk.eduai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateAnswerDto {
    private String answerText;
    private Boolean isCorrect;
}
