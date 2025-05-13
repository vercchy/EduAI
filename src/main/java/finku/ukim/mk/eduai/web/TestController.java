package finku.ukim.mk.eduai.web;

import finku.ukim.mk.eduai.dto.TestMetadataDTO;
import finku.ukim.mk.eduai.service.impl.TestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tests")
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/available-tests/subject/{subjectId}")
    public List<TestMetadataDTO> getAvailableTestsForSubject(
            @PathVariable Long subjectId,
            @RequestParam Long studentId) {
        return testService.getAvailableTestsForSubject(subjectId, studentId);
    }

    @GetMapping("/{testId}")
    public TestMetadataDTO getTestMetadata(@PathVariable Long testId) {
        return testService.getTestMetadata(testId);
    }
}
