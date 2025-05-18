package finku.ukim.mk.eduai.dto;

import finku.ukim.mk.eduai.model.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ProfessorReportDto {

    private double averageCorrectPercent;           // average % correct answers
    private List<QuestionStatDto> weakAreas;         // questions/areas with worst results
    private List<ScoreTrendDto> scoreTrend;           // average results with tries
    private QuestionDto hardestQuestion;              // hardest question
    private List<Response> responses;
}

