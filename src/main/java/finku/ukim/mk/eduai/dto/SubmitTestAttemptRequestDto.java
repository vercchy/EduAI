package finku.ukim.mk.eduai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SubmitTestAttemptRequestDto {
    private Long testAttemptId;
    private List<SubmittedResponseDto> responses;
}
