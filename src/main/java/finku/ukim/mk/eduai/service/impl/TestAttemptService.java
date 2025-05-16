package finku.ukim.mk.eduai.service.impl;

import finku.ukim.mk.eduai.dto.*;
import finku.ukim.mk.eduai.exception.ForbiddenOperationException;
import finku.ukim.mk.eduai.exception.InvalidDataException;
import finku.ukim.mk.eduai.exception.ResourceNotFoundException;
import finku.ukim.mk.eduai.exception.UnauthorizedActionException;
import finku.ukim.mk.eduai.model.*;
import finku.ukim.mk.eduai.repository.*;
import finku.ukim.mk.eduai.service.interfaces.OpenEndedEvaluationServiceInterface;
import finku.ukim.mk.eduai.service.interfaces.TestAttemptServiceInterface;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TestAttemptService implements TestAttemptServiceInterface {
    private final TestRepository testRepository;
    private final StudentSubjectAccessRepository studentSubjectAccessRepository;
    private final TestAttemptRepository testAttemptRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final ResponseRepository responseRepository;
    private final OpenEndedResponseRepository openEndedResponseRepository;
    private final ChoiceResponseRepository choiceResponseRepository;
    private final ChoiceResponseAnswerRepository choiceResponseAnswerRepository;
    private final OpenEndedEvaluationServiceInterface openEndedEvaluationService;

    public TestAttemptService(
            TestRepository testRepository,
            StudentSubjectAccessRepository studentSubjectAccessRepository,
            TestAttemptRepository testAttemptRepository,
            QuestionRepository questionRepository,
            AnswerRepository answerRepository,
            ResponseRepository responseRepository,
            OpenEndedResponseRepository openEndedResponseRepository,
            ChoiceResponseRepository choiceResponseRepository,
            ChoiceResponseAnswerRepository choiceResponseAnswerRepository,
            OpenEndedEvaluationServiceInterface openEndedEvaluationService
    ) {
        this.testRepository = testRepository;
        this.studentSubjectAccessRepository = studentSubjectAccessRepository;
        this.testAttemptRepository = testAttemptRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.responseRepository = responseRepository;
        this.openEndedResponseRepository = openEndedResponseRepository;
        this.choiceResponseRepository = choiceResponseRepository;
        this.choiceResponseAnswerRepository = choiceResponseAnswerRepository;
        this.openEndedEvaluationService = openEndedEvaluationService;
    }

    @Override
    @Transactional
    public StartTestResponseDto startTestAttempt(Long testId, String studentEmail) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new ResourceNotFoundException("Test not found"));
        StudentSubjectAccess access = studentSubjectAccessRepository
                .findBySubjectIdAndStudentUserEmail(test.getSubject().getId(), studentEmail)
                .orElseThrow(() -> new UnauthorizedActionException("Unauthorized action"));
        TestAttempt testAttempt = TestAttempt.builder()
                .test(test)
                .student(access.getStudent())
                .status(TestAttemptStatus.IN_PROGRESS)
                .totalScore(0f)
                .submissionDate(null)
                .build();
        testAttempt = testAttemptRepository.save(testAttempt);
        List<TestAttemptQuestionDto> testAttemptQuestion = constructListOfQuestionsInTest(testId);
        return new StartTestResponseDto(testAttempt.getId(), testAttemptQuestion);
    }

    @Override
    @Transactional
    public void submitTestAttempt(SubmitTestAttemptRequestDto submitTestAttemptRequestDto, String studentEmail) {
        TestAttempt testAttempt = validateAndAuthorizeTestAttempt(submitTestAttemptRequestDto.getTestAttemptId(), studentEmail);
        List<Response> savedResponses = new ArrayList<>();
        submitTestAttemptRequestDto.getResponses().forEach(response -> {
            Question question = questionRepository.findById(response.getQuestionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
            Response newResponse = new Response(testAttempt, question);
            newResponse = responseRepository.save(newResponse);
            createQuestionTypeSpecificResponseEntity(newResponse, question, response);
            savedResponses.add(newResponse);
        });
        Map<QuestionType, List<Response>> groupedByType = savedResponses.stream()
                .collect(Collectors.groupingBy(r -> r.getQuestion().getQuestionType()));
        float totalPointsFromChoiceResponses = processChoiceScoring(groupedByType);
        finalizeTestAttemptSubmission(testAttempt, totalPointsFromChoiceResponses);
        flushAndDetachPersistenceContext();
        performOpenEndedResponseEvaluation(testAttempt, groupedByType);
    }

    private TestAttempt validateAndAuthorizeTestAttempt(Long testAttemptId, String studentEmail) {
       TestAttempt testAttempt = testAttemptRepository.findById(testAttemptId)
                .orElseThrow(() -> new ResourceNotFoundException("TestAttempt not found"));
        if (!testAttempt.getStudent().getUser().getEmail().equals(studentEmail))
            throw new UnauthorizedActionException("Unauthorized action");
        validateTestAttemptStatus(testAttempt.getStatus());
        return testAttempt;
    }

    private void validateTestAttemptStatus(TestAttemptStatus testAttemptStatus) {
        if (testAttemptStatus != TestAttemptStatus.IN_PROGRESS)
            throw new ForbiddenOperationException("Only tests currently in progress can be submitted");
    }

    private void createQuestionTypeSpecificResponseEntity(Response response, Question question, SubmittedResponseDto responseDto) {
        QuestionType questionType = question.getQuestionType();
        if (questionType == QuestionType.OPEN_ENDED) {
            OpenEndedResponse openEnded = new OpenEndedResponse(response, responseDto.getOpenEndedAnswerText());
            openEndedResponseRepository.save(openEnded);
        } else {
            ChoiceResponse choiceResponse = new ChoiceResponse(response);
            choiceResponse = choiceResponseRepository.save(choiceResponse);

            List<Answer> selectedAnswers = answerRepository.findAllById(responseDto.getAnswerIds());
            for (Answer answer : selectedAnswers) {
                if (!answer.getQuestion().getId().equals(question.getId()))
                    throw new InvalidDataException("Invalid answer for this question");
                ChoiceResponseAnswer cra = new ChoiceResponseAnswer(choiceResponse, answer);
                choiceResponseAnswerRepository.save(cra);
            }
        }
    }

    private float processChoiceScoring(Map<QuestionType, List<Response>> groupedByType) {
        List<Response> choiceResponses = Stream.concat(
                groupedByType.getOrDefault(QuestionType.SINGLE_CHOICE, List.of()).stream(),
                groupedByType.getOrDefault(QuestionType.MULTIPLE_CHOICE, List.of()).stream()
        ).toList();
        return processScoresForChoiceResponses(choiceResponses);
    }

    private float processScoresForChoiceResponses(List<Response> choiceResponses) {
        float totalPoints = 0;
        for (Response response : choiceResponses) {
            List<ChoiceResponseAnswer> selectedAnswers = validateChoiceResponseAndExtractGivenAnswers(response);
            if (selectedAnswers.isEmpty()) continue;
            Question question = response.getQuestion();
            float questionMaxPoints = question.getMaxPoints();

            List<Long> correctAnswersIds = getCorrectAnswerIds(question);
            long correctSelected = countCorrectSelections(selectedAnswers, correctAnswersIds);
            long incorrectSelected = selectedAnswers.size() - correctSelected;

            float score = calculateFinalScorePerResponse(correctSelected, incorrectSelected, correctAnswersIds.size(),
                    questionMaxPoints, question.getQuestionType());

            totalPoints += score;
            response.setScore(score);
            responseRepository.save(response);
        }
        return totalPoints;
    }

    private float calculateFinalScorePerResponse(long correctSelected, long incorrectSelected, int correctGivenAnswers, float questionMaxPoints, QuestionType questionType) {
        float score;
        if (questionType == QuestionType.SINGLE_CHOICE) {
            score = correctSelected > 0 ? questionMaxPoints : 0f;
        } else {
            float rawScore = questionMaxPoints * ((float) (correctSelected - incorrectSelected) / correctGivenAnswers);
            score = Math.max(0, rawScore);
        }
        return score;
    }

    private List<Long> getCorrectAnswerIds(Question question) {
        return answerRepository.findAllByQuestionIdAndIsCorrectTrue(question.getId()).stream()
                .map(Answer::getId)
                .toList();
    }

    private long countCorrectSelections(List<ChoiceResponseAnswer> selectedAnswers, List<Long> correctAnswerIds) {
        return selectedAnswers.stream()
                .filter(cra -> correctAnswerIds.contains(cra.getAnswer().getId()))
                .count();
    }

    private List<ChoiceResponseAnswer> validateChoiceResponseAndExtractGivenAnswers(Response response) {
        ChoiceResponse choiceResponse = choiceResponseRepository.findByResponseId(response.getId())
                .orElseThrow(() -> new ResourceNotFoundException("ChoiceResponse not found"));
        return choiceResponseAnswerRepository.findAllByChoiceResponse_ResponseId(choiceResponse.getResponseId());
    }

    private void finalizeTestAttemptSubmission(TestAttempt testAttempt, float totalPoints) {
        testAttempt.setStatus(TestAttemptStatus.SUBMITTED);
        testAttempt.setSubmissionDate(LocalDateTime.now());
        testAttempt.setTotalScore(totalPoints);
        testAttemptRepository.save(testAttempt);
    }

    private void performOpenEndedResponseEvaluation(TestAttempt testAttempt, Map<QuestionType, List<Response>> groupedByType) {
        List<Response> openEndedResponses = groupedByType.getOrDefault(QuestionType.OPEN_ENDED, List.of());
        if (openEndedResponses.isEmpty()) {
            testAttempt.setStatus(TestAttemptStatus.GRADED);
            testAttemptRepository.save(testAttempt);
            return;
        }
        openEndedEvaluationService.evaluateOpenEndedResponses(openEndedResponses);
    }

    private List<TestAttemptQuestionDto> constructListOfQuestionsInTest(Long testId) {
        List<Question> allQuestionsInTest = questionRepository.findAllByTestId(testId);
        return allQuestionsInTest.stream().map(q -> {
            List<TestAttemptAnswerDto> answers = constructListOfAnswersForQuestion(q.getId());
            return new TestAttemptQuestionDto(q, answers);
        }).toList();
    }

    private List<TestAttemptAnswerDto> constructListOfAnswersForQuestion(Long questionId) {
        List<Answer> allAnswersForQuestion = answerRepository.findAllByQuestionId(questionId);
        return allAnswersForQuestion.stream().map(a -> new TestAttemptAnswerDto(a.getId(), a.getAnswerText())).toList();
    }

    private void flushAndDetachPersistenceContext(){
        responseRepository.flush();
        choiceResponseRepository.flush();
        choiceResponseAnswerRepository.flush();
        openEndedResponseRepository.flush();
    }
}
