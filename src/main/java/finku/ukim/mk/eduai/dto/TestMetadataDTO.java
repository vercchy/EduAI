package finku.ukim.mk.eduai.dto;

import finku.ukim.mk.eduai.model.Test;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
public class TestMetadataDTO {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int duration;
    private int maxPoints;
    private boolean isTestTakenByStudent;

    public TestMetadataDTO(Test test, boolean isTestTakenByStudent) {
        this.id = test.getId();
        this.title = test.getTitle();
        this.description = test.getDescription();
        this.startDate = test.getStartDate();
        this.endDate = test.getEndDate();
        this.duration = test.getDurationMinutes();
        this.maxPoints = test.getMaxPoints();
        this.isTestTakenByStudent = isTestTakenByStudent;
    }


}