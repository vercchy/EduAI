package finku.ukim.mk.eduai.service.interfaces;

import finku.ukim.mk.eduai.dto.TestMetadataDTO;
import finku.ukim.mk.eduai.model.Test;

import java.util.List;

public interface TestServiceInterface {
    List<TestMetadataDTO> getAvailableTestsForSubject(Long subjectId, Long studentId);

    TestMetadataDTO getTestMetadata(Long testId);
}
