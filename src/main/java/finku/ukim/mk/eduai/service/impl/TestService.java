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
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuestionServiceInterface questionService;
    private final AnswerServiceInterface answerService;
    private final TestAttemptRepository testAttemptRepository;

    public TestService(TestRepository testRepository,
                       StudentSubjectAccessRepository studentSubjectAccessRepository, StudentRepository studentRepository,
                       SubjectRepository subjectRepository,
                       QuestionRepository questionRepository,
                       AnswerRepository answerRepository,
                       QuestionServiceInterface questionService,
                       AnswerServiceInterface answerServiceInterface, TestAttemptRepository testAttemptRepository) {
        this.testRepository = testRepository;
        this.studentSubjectAccessRepository = studentSubjectAccessRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.questionService = questionService;
        this.answerService = answerServiceInterface;
        this.testAttemptRepository = testAttemptRepository;
    }


    @Override
    public List<TestMetadataDTO> getAvailableTestsForSubject(Long subjectId, String studentEmail) {
        studentSubjectAccessRepository
                .findBySubjectIdAndStudentUserEmail(subjectId, studentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Student is not assigned to this subject"));

        List<Test> tests = testRepository.findBySubjectId(subjectId)
                .stream()
                .filter(test -> test.getStartDate().isBefore(LocalDateTime.now()) &&
                        test.getEndDate().isAfter(LocalDateTime.now()))
                .toList();

        return tests.stream()
                .map(test -> {
                    TestAttempt testAttempt = testAttemptRepository
                            .findTestAttemptByTestIdAndStudentUserEmail(test.getId(), studentEmail)
                            .orElse(null);
                    return new TestMetadataDTO(test, testAttempt != null);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<TestMetadataDTO> getTestsByProfessorAndSubject(Long subjectId, String professorEmail) {
        obtainSubjectByIdAndTeacherEmail(subjectId, professorEmail);
        return testRepository
                .findBySubjectId(subjectId)
                .stream()
                .map(test -> new TestMetadataDTO(test, false))
                .collect(Collectors.toList());
    }

    @Override
    public TestMetadataDTO getTestMetadata(Long testId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new ResourceNotFoundException("Test not found"));

        return new TestMetadataDTO(test, false);
    }

    @Override
    @Transactional
    public void createTest(CreateTestDto createTestDto, String professorEmail) {
        BasicTestCreationInfoDto basicTestInfo = createTestDto.getBasicTestCreationInfo();
        Subject subject = obtainSubjectByIdAndTeacherEmail(basicTestInfo.getSubjectId(), professorEmail);
        validateTestDates(basicTestInfo.getStartDate(), basicTestInfo.getEndDate());
        validateTotalQuestionPoints(createTestDto.getQuestions(), basicTestInfo.getMaxPoints());

        Test test = createTestForSubject(basicTestInfo, subject);
        testRepository.save(test);

        for (CreateQuestionDto createQuestionDto : createTestDto.getQuestions()) {
            List<CreateAnswerDto> answersPerQuestion = createQuestionDto.getAnswers();
            validateQuestionTypeCompatibilityWithNumberOfCorrectAnswers(answersPerQuestion, createQuestionDto.getQuestionType());
            Question question = questionService.createQuestionForTestFromQuestionDto(createQuestionDto, test);
            questionRepository.save(question);

            for (CreateAnswerDto createAnswerDto : answersPerQuestion) {
                Answer answer = answerService.createAnswerForTestQuestion(createAnswerDto, question);
                answerRepository.save(answer);
            }
        }
    }

    @Override
    public List<Test> getTestsForSubject(Long subjectId) {
        return testRepository.findBySubjectId(subjectId);
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

    private void validateTotalQuestionPoints(List<CreateQuestionDto> questions, int testMaxPoints) {
        double totalPoints = questions.stream()
                .mapToDouble(CreateQuestionDto::getMaxPoints)
                .sum();

        if (totalPoints != testMaxPoints) {
            throw new ForbiddenOperationException("Total points from all questions (" + totalPoints +
                    ") do not equal max points for the test (" + testMaxPoints + ").");
        }
    }

    private void validateQuestionTypeCompatibilityWithNumberOfCorrectAnswers(List<CreateAnswerDto> answers, int questionType) {
        int numberOfCorrectAnswers = Math.toIntExact(answers.stream()
                .filter(CreateAnswerDto::getIsCorrect)
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

    private Subject obtainSubjectByIdAndTeacherEmail(Long subjectId, String email) {
        return subjectRepository.findByIdAndTeacherUserEmail(subjectId, email)
                .orElseThrow(() -> new UnauthorizedActionException("Invalid subject or no permission"));
    }
}
