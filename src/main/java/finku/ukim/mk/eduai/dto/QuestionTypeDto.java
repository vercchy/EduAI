package finku.ukim.mk.eduai.dto;

import finku.ukim.mk.eduai.model.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QuestionTypeDto {
    private String name;
    private int value;

    public QuestionTypeDto(QuestionType questionType) {
        this.name = questionType.name();
        this.value = questionType.ordinal();
    }
}
