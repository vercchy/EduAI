package finku.ukim.mk.eduai.controller;

import finku.ukim.mk.eduai.dto.CreateTestDto;
import finku.ukim.mk.eduai.dto.TestMetadataDTO;
import finku.ukim.mk.eduai.service.impl.TestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tests")
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

    @PostMapping
    public ResponseEntity<Void> createTest(
            @RequestBody CreateTestDto createTestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        testService.createTest(createTestDto, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
