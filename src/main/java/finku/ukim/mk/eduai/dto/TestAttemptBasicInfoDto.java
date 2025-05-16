package finku.ukim.mk.eduai.dto;

import finku.ukim.mk.eduai.model.TestAttempt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TestAttemptBasicInfoDto {
    private Long id;
    private TestAttemptStatusDto status;
    //on the front-end score should only be displayed to user if status is Graded (2)
    private Float score;
    private LocalDateTime submissionDate;
    private String testTitle;
    private int testMaxPoints;

    public TestAttemptBasicInfoDto(TestAttempt testAttempt) {
        this.id = testAttempt.getId();
        this.status = new TestAttemptStatusDto(testAttempt.getStatus());
        this.score = testAttempt.getTotalScore();
        this.submissionDate = testAttempt.getSubmissionDate();
        this.testTitle = testAttempt.getTest().getTitle();
        this.testMaxPoints = testAttempt.getTest().getMaxPoints();
    }
}
