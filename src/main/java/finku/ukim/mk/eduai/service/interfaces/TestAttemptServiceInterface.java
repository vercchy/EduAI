package finku.ukim.mk.eduai.service.interfaces;

import finku.ukim.mk.eduai.dto.StartTestResponseDto;

public interface TestAttemptServiceInterface {
    StartTestResponseDto startTestAttempt(Long testId, String studentEmail);
}
