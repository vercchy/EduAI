package finku.ukim.mk.eduai.service.interfaces;

import finku.ukim.mk.eduai.dto.CreateTestDto;
import finku.ukim.mk.eduai.dto.TestMetadataDTO;

import java.util.List;

public interface TestServiceInterface {
    List<TestMetadataDTO> getAvailableTestsForSubject(Long subjectId, String studentEmail);
    List<TestMetadataDTO> getTestsByProfessorAndSubject(Long subjectId, String professorEmail);
    TestMetadataDTO getTestMetadata(Long testId);
    void createTest(CreateTestDto createTestDto, String professorEmail);
}
