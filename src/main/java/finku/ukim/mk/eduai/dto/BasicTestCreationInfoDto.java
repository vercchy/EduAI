package finku.ukim.mk.eduai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BasicTestCreationInfoDto {
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int duration;
    private int maxPoints;
    private Long subjectId;
}
