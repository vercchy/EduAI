package finku.ukim.mk.eduai.service.impl;

import finku.ukim.mk.eduai.exception.ResourceNotFoundException;
import finku.ukim.mk.eduai.model.*;
import finku.ukim.mk.eduai.repository.EvaluationRepository;
import finku.ukim.mk.eduai.repository.OpenEndedResponseRepository;
import finku.ukim.mk.eduai.repository.ResponseRepository;
import finku.ukim.mk.eduai.repository.TestAttemptRepository;
import finku.ukim.mk.eduai.service.interfaces.AIServiceInterface;
import finku.ukim.mk.eduai.service.interfaces.OpenEndedEvaluationServiceInterface;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenEndedEvaluationService implements OpenEndedEvaluationServiceInterface {
    private final ResponseRepository responseRepository;
    private final OpenEndedResponseRepository openEndedResponseRepository;
    private final EvaluationRepository evaluationRepository;
    private final AIServiceInterface aiService;
    private final TestAttemptRepository testAttemptRepository;

    public OpenEndedEvaluationService(
            ResponseRepository responseRepository,
            EvaluationRepository evaluationRepository,
            OpenEndedResponseRepository openEndedResponseRepository,
            AIServiceInterface aiService, TestAttemptRepository testAttemptRepository
    ) {
        this.responseRepository = responseRepository;
        this.evaluationRepository = evaluationRepository;
        this.openEndedResponseRepository = openEndedResponseRepository;
        this.aiService = aiService;
        this.testAttemptRepository = testAttemptRepository;
    }

    @Override
    @Async
    @Transactional
    public void evaluateOpenEndedResponses(List<Response> responses) {
        float totalAIScore = 0f;

        for (Response response : responses) {
            OpenEndedResponse openEnded = openEndedResponseRepository.findById(response.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("OpenEndedResponse not found"));

            String questionText = response.getQuestion().getQuestionText();
            float maxPoints = response.getQuestion().getMaxPoints();
            String aiResult = aiService.evaluateStudentAnswer(questionText, openEnded.getAnswerText(), maxPoints);

            float score = extractScore(aiResult);
            String comment = extractExplanation(aiResult);

            Evaluation evaluation = Evaluation.builder()
                    .openEndedResponse(openEnded)
                    .score(score)
                    .comment(comment)
                    .isAIGenerated(true)
                    .build();

            response.setScore(score);
            totalAIScore += score;
            responseRepository.save(response);
            evaluationRepository.save(evaluation);
        }

        if (!responses.isEmpty()) {
            TestAttempt attempt = responses.get(0).getTestAttempt();
            float existingScore = attempt.getTotalScore();
            attempt.setTotalScore(existingScore + totalAIScore);
            attempt.setStatus(TestAttemptStatus.GRADED);
            testAttemptRepository.save(attempt);
        }
    }

    private float extractScore(String jsonString) {
        try {
            String num = jsonString.split("\"score\"\\s*:\\s*")[1].split("[,}]")[0];
            return Float.parseFloat(num);
        } catch (Exception e) {
            return 0f;
        }
    }

    private String extractExplanation(String jsonString) {
        try {
            return jsonString.split("\"explanation\"\\s*:\\s*\"")[1].split("\"")[0];
        } catch (Exception e) {
            return "Could not parse explanation.";
        }
    }
}
