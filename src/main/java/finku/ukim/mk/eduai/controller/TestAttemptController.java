package finku.ukim.mk.eduai.controller;

import finku.ukim.mk.eduai.dto.StartTestResponseDto;
import finku.ukim.mk.eduai.service.impl.TestAttemptService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test-attempts")
public class TestAttemptController {
    private final TestAttemptService testAttemptService;

    public TestAttemptController(TestAttemptService testAttemptService) {
        this.testAttemptService = testAttemptService;
    }

    @PostMapping("/start/{testId}")
    public ResponseEntity<StartTestResponseDto> startTestAttempt(
            @PathVariable Long testId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(testAttemptService.startTestAttempt(testId, userDetails.getUsername()));
    }
}
