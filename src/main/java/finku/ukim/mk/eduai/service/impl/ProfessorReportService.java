package finku.ukim.mk.eduai.service.impl;

import finku.ukim.mk.eduai.dto.ProfessorReportDto;
import finku.ukim.mk.eduai.dto.QuestionDto;
import finku.ukim.mk.eduai.dto.QuestionStatDto;
import finku.ukim.mk.eduai.dto.ScoreTrendDto;
import finku.ukim.mk.eduai.model.Question;
import finku.ukim.mk.eduai.model.Response;
import finku.ukim.mk.eduai.repository.QuestionRepository;
import finku.ukim.mk.eduai.repository.ResponseRepository;
import finku.ukim.mk.eduai.repository.TestRepository;
import finku.ukim.mk.eduai.service.interfaces.ProfessorReportServiceInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProfessorReportService implements ProfessorReportServiceInterface {

    private final ResponseRepository responseRepository;
    private final QuestionRepository questionRepository;
    private final TestRepository testRepository;

    public ProfessorReportService(ResponseRepository responseRepository,
                                      QuestionRepository questionRepository,
                                      TestRepository testRepository) {
        this.responseRepository = responseRepository;
        this.questionRepository = questionRepository;
        this.testRepository = testRepository;
    }

    @Override
    public ProfessorReportDto getProfessorReport(Long subjectId, Long testId) {
        // 1. Врати ги сите responses за тестот (testId) и предметот (subjectId)
        List<Response> responses = responseRepository.findByTestIdAndSubjectId(testId, subjectId);

        // 2. Пресметај просечен correct % за сите студенти (score/максимален score)
        double averageCorrectPercent = calculateAverageCorrectPercent(responses);

        // 3. Најди слабите области (прашања со најмал просечен резултат)
        List<QuestionStatDto> weakAreas = calculateWeakAreas(responses);

        // 4. Пресметај тренд на резултати (score trend)
        List<ScoreTrendDto> scoreTrend = calculateScoreTrend(testId, subjectId);

        // 5. Најди најтешко прашање (прашање со најнизок просечен резултат)
        Question hardestQuestion = findHardestQuestion(responses);

        QuestionDto hardestQuestionDto = new QuestionDto(
                hardestQuestion.getId(),
                hardestQuestion.getText()
                // додади други полиња ако има
        );

        return ProfessorReportDto.builder()
                .averageCorrectPercent(averageCorrectPercent)
                .weakAreas(weakAreas)
                .scoreTrend(scoreTrend)
                .hardestQuestion(hardestQuestionDto)
                .build();

    }

    // --- помошни методи ---

    private double calculateAverageCorrectPercent(List<Response> responses) {
        if (responses.isEmpty()) return 0;
        double totalScore = responses.stream().mapToDouble(Response::getScore).sum();
        int count = responses.size();
        // assuming max score per response is 1.0, adjust if different
        return (totalScore / count) * 100;
    }

    private List<QuestionStatDto> calculateWeakAreas(List<Response> responses) {
        // групирај ги responses по прашање, пресметај просек score
        Map<Question, Double> avgScoreByQuestion = responses.stream()
                .collect(Collectors.groupingBy(Response::getQuestion,
                        Collectors.averagingDouble(Response::getScore)));

        // земи прашања со score под некоја вредност (на пример 50%)
        return avgScoreByQuestion.entrySet().stream()
                .filter(e -> e.getValue() < 0.5)
                .map(e -> new QuestionStatDto(e.getKey().getId(), e.getKey().getQuestionText(), e.getValue()))
                .collect(Collectors.toList());
    }

    private List<ScoreTrendDto> calculateScoreTrend(Long testId, Long subjectId) {
        // Пример: земи претходни обиди на тестот (пр. по дати) и пресметај просечен резултат за секој
        // Ова бара дополнителен метод во репозиториуми за повлекување според време

        // Еве пример без реален код, само структура:
        // List<TestAttempt> attempts = testAttemptRepository.findByTestIdAndSubjectIdOrderByDateAsc(testId, subjectId);

        // Map<Date, Double> avgScorePerAttemptDate = ...

        // return List<ScoreTrendDto> sorted by date

        return List.of(); // врати празно додека не имплементираш
    }

    private Question findHardestQuestion(List<Response> responses) {
        return responses.stream()
                .collect(Collectors.groupingBy(Response::getQuestion,
                        Collectors.averagingDouble(Response::getScore)))
                .entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}
