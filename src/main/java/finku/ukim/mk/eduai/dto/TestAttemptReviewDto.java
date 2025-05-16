package finku.ukim.mk.eduai.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class TestAttemptReviewDto {
    TestAttemptBasicInfoDto testAttemptBasicInfoDto;
    List<TestReviewQuestionDto> questionsToReview;

    public TestAttemptReviewDto(TestAttemptBasicInfoDto testAttemptBasicInfoDto, List<TestReviewQuestionDto> questionsToReview) {
        this.testAttemptBasicInfoDto = testAttemptBasicInfoDto;
        this.questionsToReview = questionsToReview;
    }
}
