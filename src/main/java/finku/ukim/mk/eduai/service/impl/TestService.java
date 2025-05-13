package finku.ukim.mk.eduai.service.impl;

import finku.ukim.mk.eduai.dto.TestMetadataDTO;
import finku.ukim.mk.eduai.exception.ResourceNotFoundException;
import finku.ukim.mk.eduai.model.StudentSubjectAccess;
import finku.ukim.mk.eduai.model.Test;
import finku.ukim.mk.eduai.repository.StudentSubjectAccessRepository;
import finku.ukim.mk.eduai.repository.TestRepository;
import finku.ukim.mk.eduai.service.interfaces.TestServiceInterface;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestService implements TestServiceInterface {


    private final TestRepository testRepository;
    private final StudentSubjectAccessRepository studentSubjectAccessRepository;

    public TestService(TestRepository testRepository, StudentSubjectAccessRepository studentSubjectAccessRepository) {
        this.testRepository = testRepository;
        this.studentSubjectAccessRepository = studentSubjectAccessRepository;
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
}
