package finku.ukim.mk.eduai.service.impl;

import finku.ukim.mk.eduai.dto.ProfessorReportDto;
import finku.ukim.mk.eduai.dto.QuestionDto;
import finku.ukim.mk.eduai.dto.QuestionStatDto;
import finku.ukim.mk.eduai.dto.ScoreTrendDto;
import finku.ukim.mk.eduai.model.Question;
import finku.ukim.mk.eduai.model.Response;
import finku.ukim.mk.eduai.model.TestAttempt;
import finku.ukim.mk.eduai.repository.QuestionRepository;
import finku.ukim.mk.eduai.repository.ResponseRepository;
import finku.ukim.mk.eduai.repository.TestAttemptRepository;
import finku.ukim.mk.eduai.repository.TestRepository;
import finku.ukim.mk.eduai.service.interfaces.ProfessorReportServiceInterface;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProfessorReportService implements ProfessorReportServiceInterface {

    private final ResponseRepository responseRepository;
    private final QuestionRepository questionRepository;
    private final TestRepository testRepository;
    private final TestAttemptRepository testAttemptRepository;

    public ProfessorReportService(ResponseRepository responseRepository,
                                  QuestionRepository questionRepository,
                                  TestRepository testRepository,
                                  TestAttemptRepository testAttemptRepository) {
        this.responseRepository = responseRepository;
        this.questionRepository = questionRepository;
        this.testRepository = testRepository;
        this.testAttemptRepository = testAttemptRepository;
    }

    @Override
    public ProfessorReportDto getProfessorReport(Long subjectId, Long testId) {
        List<Response> responses = responseRepository.findByTestIdAndSubjectId(testId, subjectId);

        double averageCorrectPercent = calculateAverageCorrectPercent(responses);

        List<QuestionStatDto> weakAreas = calculateWeakAreas(responses);

        List<ScoreTrendDto> scoreTrend = calculateScoreTrend(testId, subjectId);

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


    public double calculateAverageCorrectPercent(List<Response> responses) {
        if (responses == null || responses.isEmpty()) {
            return 0.0;
        }

        double totalPercentage = responses.stream()
                .mapToDouble(response -> {
                    Float maxPoints = response.getQuestion().getMaxPoints();
                    return (response.getScore() / maxPoints) * 100.0;
                })
                .sum();

        return Math.round((totalPercentage / responses.size()) * 100.0) / 100.0;
    }
    private List<QuestionStatDto> calculateWeakAreas(List<Response> responses) {
        if (responses == null || responses.isEmpty()) {
            return List.of();
        }
        Map<Question, Double> avgScorePercentByQuestion = responses.stream()
                .filter(r -> r.getQuestion().getMaxPoints() != 0) // avoid division by zero
                .collect(Collectors.groupingBy(
                        Response::getQuestion,
                        Collectors.averagingDouble(r -> (r.getScore() / r.getQuestion().getMaxPoints()) * 100.0)
                ));

        return avgScorePercentByQuestion.entrySet().stream()
                .filter(e -> e.getValue() < 50.0)
                .map(e -> new QuestionStatDto(
                        e.getKey().getId(),
                        e.getKey().getQuestionText(),
                        Math.round(e.getValue() * 100.0) / 100.0
                ))
                .collect(Collectors.toList());
    }

    public List<ScoreTrendDto> calculateScoreTrend(Long testId, Long subjectId) {
        List<TestAttempt> attempts = testAttemptRepository.findByTestIdAndSubjectIdOrderByDateAsc(testId, subjectId);

        if (attempts.isEmpty()) return List.of();

        Map<LocalDate, List<TestAttempt>> attemptsByDate = attempts.stream()
                .filter(a -> a.getSubmissionDate() != null)
                .collect(Collectors.groupingBy(a -> a.getSubmissionDate().toLocalDate()));

        List<ScoreTrendDto> trend = new ArrayList<>();

        for (Map.Entry<LocalDate, List<TestAttempt>> entry : attemptsByDate.entrySet()) {
            LocalDate date = entry.getKey();
            List<TestAttempt> dailyAttempts = entry.getValue();

            List<Double> normalizedScores = new ArrayList<>();

            for (TestAttempt attempt : dailyAttempts) {
                List<Response> responses = responseRepository.findAllByTestAttemptId(attempt.getId());

                if (responses.isEmpty()) continue;

                double avg = responses.stream()
                        .filter(r -> r.getQuestion().getMaxPoints() != 0)
                        .mapToDouble(r -> r.getScore() / r.getQuestion().getMaxPoints())
                        .average()
                        .orElse(0.0);

                normalizedScores.add(avg);
            }

            double dailyAverage = normalizedScores.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);

            trend.add(new ScoreTrendDto(date, Math.round(dailyAverage * 10000.0) / 100.0));
        }

        trend.sort(Comparator.comparing(ScoreTrendDto::getDate));
        return trend;
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