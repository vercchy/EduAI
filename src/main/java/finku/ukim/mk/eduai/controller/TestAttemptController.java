package finku.ukim.mk.eduai.controller;

import finku.ukim.mk.eduai.dto.StartTestResponseDto;
import finku.ukim.mk.eduai.dto.SubmitTestAttemptRequestDto;
import finku.ukim.mk.eduai.dto.TestAttemptBasicInfoDto;
import finku.ukim.mk.eduai.dto.TestAttemptReviewDto;
import finku.ukim.mk.eduai.service.impl.TestAttemptService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test-attempts")
public class TestAttemptController {
    private final TestAttemptService testAttemptService;

    public TestAttemptController(TestAttemptService testAttemptService) {
        this.testAttemptService = testAttemptService;
    }

    @GetMapping("/{testId}")
    public ResponseEntity<TestAttemptBasicInfoDto> getTestAttemptBasicInfo(
            @PathVariable Long testId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(testAttemptService.getTestAttemptBasicInfo(testId, userDetails.getUsername()));
    }

    @GetMapping("/review/{testAttemptId}")
    public ResponseEntity<TestAttemptReviewDto> reviewTestAttempt(
            @PathVariable Long testAttemptId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(testAttemptService.reviewTestAttempt(testAttemptId, userDetails.getUsername()));
    }

    @GetMapping("/summary/{testId}")
    public ResponseEntity<List<TestAttemptBasicInfoDto>> getTestAttemptSummariesForTest(
            @PathVariable Long testId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(testAttemptService.getTestAttemptSummariesForTest(testId, userDetails.getUsername()));
    }

    @PostMapping("/start/{testId}")
    public ResponseEntity<StartTestResponseDto> startTestAttempt(
            @PathVariable Long testId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(testAttemptService.startTestAttempt(testId, userDetails.getUsername()));
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submitTestAttempt(
            @RequestBody SubmitTestAttemptRequestDto submitTestAttemptRequestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        testAttemptService.submitTestAttempt(submitTestAttemptRequestDto, userDetails.getUsername());
        return ResponseEntity.ok("Test submitted successfully!");
    }

}
