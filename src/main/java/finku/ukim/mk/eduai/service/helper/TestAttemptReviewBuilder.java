package finku.ukim.mk.eduai.service.helper;

import finku.ukim.mk.eduai.dto.*;
import finku.ukim.mk.eduai.exception.ResourceNotFoundException;
import finku.ukim.mk.eduai.model.*;
import finku.ukim.mk.eduai.repository.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TestAttemptReviewBuilder {
    private final ResponseRepository responseRepository;
    private final AnswerRepository answerRepository;
    private final OpenEndedResponseRepository openEndedResponseRepository;
    private final EvaluationRepository evaluationRepository;
    private final ChoiceResponseRepository choiceResponseRepository;
    private final ChoiceResponseAnswerRepository choiceResponseAnswerRepository;

    public TestAttemptReviewBuilder(
            ResponseRepository responseRepository,
            AnswerRepository answerRepository,
            OpenEndedResponseRepository openEndedResponseRepository,
            EvaluationRepository evaluationRepository,
            ChoiceResponseRepository choiceResponseRepository,
            ChoiceResponseAnswerRepository choiceResponseAnswerRepository) {
        this.responseRepository = responseRepository;
        this.answerRepository = answerRepository;
        this.openEndedResponseRepository = openEndedResponseRepository;
        this.evaluationRepository = evaluationRepository;
        this.choiceResponseRepository = choiceResponseRepository;
        this.choiceResponseAnswerRepository = choiceResponseAnswerRepository;
    }

    public TestAttemptReviewDto buildTestAttemptReviewDto(TestAttempt testAttempt) {
        List<Response> responses = responseRepository.findAllByTestAttemptId(testAttempt.getId());
        List<TestReviewQuestionDto> questionDtos = new ArrayList<>();

        for (Response response : responses) {
            questionDtos.add(buildTestReviewQuestionDtoBasedOnQuestionType(response));
        }

        return new TestAttemptReviewDto(new TestAttemptBasicInfoDto(testAttempt), questionDtos);
    }

    private TestReviewQuestionDto buildTestReviewQuestionDtoBasedOnQuestionType(Response response) {
        Long responseId = response.getId();
        Question question = response.getQuestion();
        QuestionTypeDto questionTypeDto = new QuestionTypeDto(question.getQuestionType());
        String questionText = question.getQuestionText();
        Float earnedScore = response.getScore();
        Float maximumPoints = question.getMaxPoints();

        if (question.getQuestionType() == QuestionType.OPEN_ENDED)
            return buildTestReviewOpenEndedQuestionDto(responseId, questionTypeDto, questionText, maximumPoints, earnedScore);
        else
            return buildTestReviewChoiceQuestionDto(responseId, question.getId(), questionTypeDto, questionText, maximumPoints, earnedScore);
    }

    private TestReviewChoiceQuestionDto buildTestReviewChoiceQuestionDto(
            Long responseId,
            Long questionId,
            QuestionTypeDto questionTypeDto,
            String questionText,
            float maximumPoints,
            float earnedScore) {
        ChoiceResponse choiceResponse = choiceResponseRepository.findByResponseId(responseId)
                .orElseThrow(() -> new ResourceNotFoundException("Choice response not found"));

        List<Long> selectedAnswerIds = choiceResponseAnswerRepository
                .findAllByChoiceResponse_ResponseId(choiceResponse.getResponseId())
                .stream()
                .map(cra -> cra.getAnswer().getId())
                .toList();

        List<TestReviewAnswerDto> allAnswers = answerRepository.findAllByQuestionId(questionId)
                .stream()
                .map(a -> new TestReviewAnswerDto(a.getId(), a.getAnswerText()))
                .toList();

        return TestReviewChoiceQuestionDto.builder()
                .questionText(questionText)
                .questionTypeDto(questionTypeDto)
                .maximumPoints(maximumPoints)
                .earnedScore(earnedScore)
                .allAnswersForQuestion(allAnswers)
                .providedAnswerIds(selectedAnswerIds)
                .build();
    }

    private TestReviewOpenEndedQuestionDto buildTestReviewOpenEndedQuestionDto(
            Long responseId,
            QuestionTypeDto questionTypeDto,
            String questionText,
            float maximumPoints,
            float earnedScore) {
        OpenEndedResponse openEnded = openEndedResponseRepository.findByResponseId(responseId)
                .orElseThrow(() -> new ResourceNotFoundException("Open-ended response not found"));
        Evaluation evaluation = evaluationRepository.findByOpenEndedResponse(openEnded)
                .orElse(new Evaluation());

        return TestReviewOpenEndedQuestionDto.builder()
                .questionText(questionText)
                .questionTypeDto(questionTypeDto)
                .maximumPoints(maximumPoints)
                .earnedScore(earnedScore)
                .providedOpenEndedAnswer(openEnded.getAnswerText())
                .aiEvaluationComment(evaluation.getComment())
                .build();
    }
}
