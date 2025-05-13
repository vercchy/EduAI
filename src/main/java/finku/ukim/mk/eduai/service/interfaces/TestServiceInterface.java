package finku.ukim.mk.eduai.service.interfaces;

import finku.ukim.mk.eduai.dto.CreateTestDto;
import finku.ukim.mk.eduai.dto.TestMetadataDTO;

import java.util.List;

public interface TestServiceInterface {
    List<TestMetadataDTO> getAvailableTestsForSubject(Long subjectId, Long studentId);
    TestMetadataDTO getTestMetadata(Long testId);
    void createTest(CreateTestDto createTestDto, String professorEmail);
}
