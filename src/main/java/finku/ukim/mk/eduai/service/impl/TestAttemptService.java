package finku.ukim.mk.eduai.service.impl;

import finku.ukim.mk.eduai.dto.StartTestResponseDto;
import finku.ukim.mk.eduai.dto.TestAttemptAnswerDto;
import finku.ukim.mk.eduai.dto.TestAttemptQuestionDto;
import finku.ukim.mk.eduai.exception.ResourceNotFoundException;
import finku.ukim.mk.eduai.exception.UnauthorizedActionException;
import finku.ukim.mk.eduai.model.*;
import finku.ukim.mk.eduai.repository.*;
import finku.ukim.mk.eduai.service.interfaces.TestAttemptServiceInterface;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestAttemptService implements TestAttemptServiceInterface {
    private final TestRepository testRepository;
    private final StudentSubjectAccessRepository studentSubjectAccessRepository;
    private final TestAttemptRepository testAttemptRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public TestAttemptService(TestRepository testRepository, StudentSubjectAccessRepository studentSubjectAccessRepository, TestAttemptRepository testAttemptRepository, QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.testRepository = testRepository;
        this.studentSubjectAccessRepository = studentSubjectAccessRepository;
        this.testAttemptRepository = testAttemptRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
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
                .submissionDate(null)
                .build();
        testAttempt = testAttemptRepository.save(testAttempt);
        List<TestAttemptQuestionDto> testAttemptQuestion = constructListOfQuestionsInTest(testId);
        return new StartTestResponseDto(testAttempt.getId(), testAttemptQuestion);
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
}
