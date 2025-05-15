package finku.ukim.mk.eduai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SubmittedResponseDto {
    private Long questionId;
    private String openEndedAnswerText;
    private List<Long> answerIds;
}
