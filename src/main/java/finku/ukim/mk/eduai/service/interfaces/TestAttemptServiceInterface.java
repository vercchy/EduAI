package finku.ukim.mk.eduai.service.interfaces;

import finku.ukim.mk.eduai.dto.StartTestResponseDto;
import finku.ukim.mk.eduai.dto.SubmitTestAttemptRequestDto;

public interface TestAttemptServiceInterface {
    StartTestResponseDto startTestAttempt(Long testId, String studentEmail);
    void submitTestAttempt(SubmitTestAttemptRequestDto submitTestAttemptRequestDto, String studentEmail);
}
