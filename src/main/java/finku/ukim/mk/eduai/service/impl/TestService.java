package finku.ukim.mk.eduai.service.impl;

import finku.ukim.mk.eduai.dto.*;
import finku.ukim.mk.eduai.exception.ForbiddenOperationException;
import finku.ukim.mk.eduai.exception.ResourceNotFoundException;
import finku.ukim.mk.eduai.exception.UnauthorizedActionException;
import finku.ukim.mk.eduai.model.*;
import finku.ukim.mk.eduai.repository.*;
import finku.ukim.mk.eduai.service.interfaces.AnswerServiceInterface;
import finku.ukim.mk.eduai.service.interfaces.QuestionServiceInterface;
import finku.ukim.mk.eduai.service.interfaces.TestServiceInterface;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestService implements TestServiceInterface {


    private final TestRepository testRepository;
    private final StudentSubjectAccessRepository studentSubjectAccessRepository;
    private final SubjectRepository subjectRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuestionServiceInterface questionService;
    private final AnswerServiceInterface answerService;

    public TestService(TestRepository testRepository,
                       StudentSubjectAccessRepository studentSubjectAccessRepository,
                       SubjectRepository subjectRepository,
                       QuestionRepository questionRepository,
                       AnswerRepository answerRepository,
                       QuestionServiceInterface questionService,
                       AnswerServiceInterface answerServiceInterface) {
        this.testRepository = testRepository;
        this.studentSubjectAccessRepository = studentSubjectAccessRepository;
        this.subjectRepository = subjectRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.questionService = questionService;
        this.answerService = answerServiceInterface;
    }


    @Override
    public List<TestMetadataDTO> getAvailableTestsForSubject(Long subjectId, Long studentId) {
        StudentSubjectAccess access = studentSubjectAccessRepository
                .findById_StudentIdAndId_SubjectId(studentId, subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Student is not assigned to this subject"));

        List<Test> tests = testRepository.findBySubjectId(subjectId)
                .stream()
                .filter(test -> test.getStartDate().isBefore(LocalDateTime.now()) &&
                        test.getEndDate().isAfter(LocalDateTime.now()))
                .toList();

        return tests.stream().map(test -> new TestMetadataDTO(
                test.getId(),
                test.getTitle(),
                test.getDescription(),
                test.getStartDate(),
                test.getEndDate()
        )).collect(Collectors.toList());
    }

    @Override
    public TestMetadataDTO getTestMetadata(Long testId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new ResourceNotFoundException("Test not found"));

        return new TestMetadataDTO(
                test.getId(),
                test.getTitle(),
                test.getDescription(),
                test.getStartDate(),
                test.getEndDate()
        );
    }

    @Override
    @Transactional
    public void createTest(CreateTestDto createTestDto, String professorEmail) {
        BasicTestCreationInfoDto basicTestInfo = createTestDto.getBasicTestCreationInfo();
        Subject subject = subjectRepository.findByIdAndTeacherUserEmail(basicTestInfo.getSubjectId(), professorEmail)
                .orElseThrow(() -> new UnauthorizedActionException("Invalid subject or no permission"));
        validateTestDates(basicTestInfo.getStartDate(), basicTestInfo.getEndDate());
        validateTotalQuestionPoints(createTestDto.getQuestions(), basicTestInfo.getMaxPoints());

        Test test = createTestForSubject(basicTestInfo, subject);
        testRepository.save(test);

        for (QuestionDto questionDto : createTestDto.getQuestions()) {
            List<AnswerDto> answersPerQuestion = questionDto.getAnswers();
            validateQuestionTypeCompatibilityWithNumberOfCorrectAnswers(answersPerQuestion, questionDto.getQuestionType());
            Question question = questionService.createQuestionForTestFromQuestionDto(questionDto, test);
            questionRepository.save(question);

            for (AnswerDto answerDto : answersPerQuestion) {
                Answer answer = answerService.createAnswerForTestQuestion(answerDto, question);
                answerRepository.save(answer);
            }
        }
    }

    private Test createTestForSubject(BasicTestCreationInfoDto basicTestInfo, Subject subject) {
        return Test.builder()
                .title(basicTestInfo.getTitle())
                .description(basicTestInfo.getDescription())
                .startDate(basicTestInfo.getStartDate())
                .endDate(basicTestInfo.getEndDate())
                .durationMinutes(basicTestInfo.getDuration())
                .maxPoints(basicTestInfo.getMaxPoints())
                .subject(subject)
                .build();
    }

    private void validateTotalQuestionPoints(List<QuestionDto> questions, int testMaxPoints) {
        double totalPoints = questions.stream()
                .mapToDouble(QuestionDto::getMaxPoints)
                .sum();

        if (totalPoints != testMaxPoints) {
            throw new ForbiddenOperationException("Total points from all questions (" + totalPoints +
                    ") do not equal max points for the test (" + testMaxPoints + ").");
        }
    }

    private void validateQuestionTypeCompatibilityWithNumberOfCorrectAnswers(List<AnswerDto> answers, int questionType) {
        int numberOfCorrectAnswers = Math.toIntExact(answers.stream()
                .filter(AnswerDto::getIsCorrect)
                .count());
        boolean isCompatible = false;
        switch (questionType) {
            case 0 -> isCompatible = numberOfCorrectAnswers == 1;
            case 1 -> isCompatible = numberOfCorrectAnswers > 1 && numberOfCorrectAnswers <= answers.size();
            case 2 -> isCompatible = answers.isEmpty();
        }
        if (!isCompatible)
            throw new ForbiddenOperationException("Number of correct answers must be compatible with Question Type.");
    }

    private void validateTestDates(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate) || startDate.isEqual(endDate)) {
            throw new ForbiddenOperationException("Start date must be before end date.");
        }
    }
}
