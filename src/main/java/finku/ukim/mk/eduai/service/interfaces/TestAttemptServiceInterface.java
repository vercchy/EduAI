package finku.ukim.mk.eduai.service.interfaces;

import finku.ukim.mk.eduai.dto.StartTestResponseDto;
import finku.ukim.mk.eduai.dto.SubmitTestAttemptRequestDto;
import finku.ukim.mk.eduai.dto.TestAttemptBasicInfoDto;
import finku.ukim.mk.eduai.dto.TestAttemptReviewDto;

public interface TestAttemptServiceInterface {
    StartTestResponseDto startTestAttempt(Long testId, String studentEmail);
    void submitTestAttempt(SubmitTestAttemptRequestDto submitTestAttemptRequestDto, String studentEmail);
    TestAttemptBasicInfoDto getTestAttemptBasicInfo(Long testAttemptId, String studentEmail);
    TestAttemptReviewDto reviewTestAttempt(Long testAttemptId, String email);
}
