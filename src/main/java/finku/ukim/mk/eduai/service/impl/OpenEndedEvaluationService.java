package finku.ukim.mk.eduai.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import finku.ukim.mk.eduai.dto.AIEvaluationResponseDto;
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
    private final ObjectMapper objectMapper;

    public OpenEndedEvaluationService(
            ResponseRepository responseRepository,
            EvaluationRepository evaluationRepository,
            OpenEndedResponseRepository openEndedResponseRepository,
            AIServiceInterface aiService, TestAttemptRepository testAttemptRepository,
            ObjectMapper objectMapper
    ) {
        this.responseRepository = responseRepository;
        this.evaluationRepository = evaluationRepository;
        this.openEndedResponseRepository = openEndedResponseRepository;
        this.aiService = aiService;
        this.testAttemptRepository = testAttemptRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Async
    @Transactional
    public void evaluateOpenEndedResponses(List<Response> responses) {
        float totalAIScore = 0f;

        for (Response response : responses) {
            OpenEndedResponse openEnded = openEndedResponseRepository.findByResponseId(response.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("OpenEndedResponse not found"));

            String answerText = openEnded.getAnswerText();
            if (answerText == null || answerText.isEmpty()) continue;

            String questionText = response.getQuestion().getQuestionText();
            float maxPoints = response.getQuestion().getMaxPoints();
            String aiResult = aiService.evaluateStudentAnswer(questionText, answerText, maxPoints);

            AIEvaluationResponseDto aiEvaluationResponseDto = parseEvaluationJson(aiResult);
            float score = aiEvaluationResponseDto.getScore();

            Evaluation evaluation = Evaluation.builder()
                    .openEndedResponse(openEnded)
                    .score(score)
                    .comment(aiEvaluationResponseDto.getExplanation())
                    .isAIGenerated(true)
                    .build();

            response.setScore(score);
            totalAIScore += score;
            responseRepository.save(response);
            evaluationRepository.save(evaluation);
        }

        TestAttempt attempt = responses.get(0).getTestAttempt();
        float existingScore = attempt.getTotalScore();
        attempt.setTotalScore(existingScore + totalAIScore);
        attempt.setStatus(TestAttemptStatus.GRADED);
        testAttemptRepository.save(attempt);

    }

    private AIEvaluationResponseDto parseEvaluationJson(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, AIEvaluationResponseDto.class);
        } catch (Exception e) {
            return new AIEvaluationResponseDto(0f, "Could not parse response properly");
        }
    }
}
