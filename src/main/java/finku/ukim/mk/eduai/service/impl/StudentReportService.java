package finku.ukim.mk.eduai.service.impl;

import finku.ukim.mk.eduai.dto.StudentReportDto;
import finku.ukim.mk.eduai.model.Question;
import finku.ukim.mk.eduai.model.Response;
import finku.ukim.mk.eduai.model.TestAttempt;
import finku.ukim.mk.eduai.model.TestAttemptStatus;
import finku.ukim.mk.eduai.repository.ResponseRepository;
import finku.ukim.mk.eduai.repository.TestAttemptRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentReportService {

    private final TestAttemptRepository testAttemptRepository;
    private final ResponseRepository responseRepository;

    public StudentReportService(TestAttemptRepository testAttemptRepository,
                                ResponseRepository responseRepository) {
        this.testAttemptRepository = testAttemptRepository;
        this.responseRepository = responseRepository;
    }

    public StudentReportDto getStudentReport(Long userId) {
        //all attempts for student where status = GRADED
        List<TestAttempt> attempts = testAttemptRepository.findByStudentUserIdAndStatus(userId, TestAttemptStatus.GRADED);

        if (attempts.isEmpty()) {
            // if there aren't any graded tests, empty report or exception
            return new StudentReportDto(userId, 0f, List.of(), List.of(), null);
        }

        // correct %
        float totalPoints = 0f;
        float maxPoints = 0f;
        for (TestAttempt attempt : attempts) {
            totalPoints += attempt.getTotalScore();
            maxPoints += attempt.getTest().getMaxPoints();
        }
        float correctPercentage = maxPoints == 0 ? 0f : (totalPoints / maxPoints) * 100;

        // Score trend - list of dates and points
        List<StudentReportDto.ScoreTrendPoint> scoreTrend = attempts.stream()
                .sorted((a,b) -> a.getSubmissionDate().compareTo(b.getSubmissionDate()))
                .map(a -> new StudentReportDto.ScoreTrendPoint(
                        a.getSubmissionDate().toLocalDate().toString(), a.getTotalScore()))
                .toList();

        // weak areas for subject
        Map<String, Float> avgScoreBySubject = new HashMap<>();
        Map<String, Integer> attemptsBySubject = new HashMap<>();
        for (TestAttempt attempt : attempts) {
            String subjectName = attempt.getTest().getSubject().getName();
            avgScoreBySubject.put(subjectName,
                    avgScoreBySubject.getOrDefault(subjectName, 0f) + attempt.getTotalScore());
            attemptsBySubject.put(subjectName, attemptsBySubject.getOrDefault(subjectName, 0) + 1);
        }
        // average grade per subject
        Map<String, Float> averageBySubject = new HashMap<>();
        for (var entry : avgScoreBySubject.entrySet()) {
            String subject = entry.getKey();
            float total = entry.getValue();
            int count = attemptsBySubject.get(subject);
            averageBySubject.put(subject, total / count);
        }
        // weak areas - lowest average score per subject
        List<String> weakAreas = averageBySubject.entrySet().stream()
                .filter(e -> e.getValue() < (0.6 * 100))  // 60% threshold
                .map(Map.Entry::getKey)
                .toList();

        // hardest question
        // responses by students and question
        List<Response> responses = responseRepository.findByTestAttemptStudentUserId(userId);

        Map<Long, List<Float>> scoresByQuestion = new HashMap<>();
        for (Response r : responses) {
            scoresByQuestion.computeIfAbsent(r.getQuestion().getId(), k -> new ArrayList<>())
                    .add(r.getScore());
        }

        // Најди прашање со најниска просечна оцена
        Long hardestQuestionId = null;
        float lowestAvgScore = Float.MAX_VALUE;
        for (var entry : scoresByQuestion.entrySet()) {
            float avg = (float) entry.getValue().stream().mapToDouble(Float::doubleValue).average().orElse(0);
            if (avg < lowestAvgScore) {
                lowestAvgScore = avg;
                hardestQuestionId = entry.getKey();
            }
        }

        final Long localHardestQuestionId = hardestQuestionId;
        Question hardestQuestion = null;
        if (localHardestQuestionId != null) {
            hardestQuestion = responses.stream()
                    .filter(r -> r.getQuestion().getId().equals(localHardestQuestionId))
                    .findFirst()
                    .map(Response::getQuestion)
                    .orElse(null);
        }

        StudentReportDto.QuestionDto hardestQuestionDto = null;
        if (hardestQuestion != null) {
            hardestQuestionDto = new StudentReportDto.QuestionDto(
                    hardestQuestion.getId(), hardestQuestion.getText());
        }

        return new StudentReportDto(userId, correctPercentage, weakAreas, scoreTrend, hardestQuestionDto);
    }
}
