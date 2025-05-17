package finku.ukim.mk.eduai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StudentReportDto {

    private Long userId;
    private float correctPercentage;
    private List<String> weakAreas;       // пример: листа на предмети или теми
    private List<ScoreTrendPoint> scoreTrend;
    private QuestionDto hardestQuestion;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ScoreTrendPoint {
        private String date;
        private float score;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class QuestionDto {
        private Long questionId;
        private String questionText;
    }
}
