package finku.ukim.mk.eduai.controller;

import finku.ukim.mk.eduai.dto.*;
import finku.ukim.mk.eduai.security.SecurityUtils;
import finku.ukim.mk.eduai.service.interfaces.SubjectServiceInterface;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {
    private final SubjectServiceInterface subjectService;
    private String email;
    private String role;

    public SubjectController(SubjectServiceInterface subjectService) {
        this.subjectService = subjectService;
    }

    private void setCommonProperties(Authentication authentication) {
        this.email = SecurityUtils.getEmail(authentication);
        this.role = SecurityUtils.getRole(authentication);
    }

    @GetMapping
    public ResponseEntity<List<SubjectBasicInfoDto>> getSubjects(Authentication authentication) {
        setCommonProperties(authentication);
        return ResponseEntity.ok(subjectService.getSubjectsByRole(email, role));
    }

    @GetMapping("/{subjectId}")
    public ResponseEntity<SubjectDetailsDto> getSubjectDetails(@PathVariable Long subjectId, Authentication authentication) {
        setCommonProperties(authentication);
        return ResponseEntity.ok(subjectService.getSubjectDetails(subjectId, email, role));
    }

    @PostMapping
    public ResponseEntity<SubjectCreateResponse> createSubject(
            @Valid @RequestBody SubjectCreateRequest request,
            Authentication authentication) {
        setCommonProperties(authentication);
        return ResponseEntity.ok(subjectService.createSubject(request, email, role));
    }

    @PostMapping("/assign-student")
    public ResponseEntity<String> assignStudents(
            @RequestBody AssignStudentToSubjectRequest request,
            Authentication authentication) {
        String email = SecurityUtils.getEmail(authentication);
        subjectService.assignStudentsToSubject(request, email);
        return ResponseEntity.ok("Access successfully assigned to student.");
    }
}
