package finku.ukim.mk.eduai.controller;

import finku.ukim.mk.eduai.dto.AssignStudentToSubjectRequest;
import finku.ukim.mk.eduai.dto.SubjectCreateRequest;
import finku.ukim.mk.eduai.dto.SubjectCreateResponse;
import finku.ukim.mk.eduai.dto.SubjectDTO;
import finku.ukim.mk.eduai.security.SecurityUtils;
import finku.ukim.mk.eduai.service.interfaces.SubjectServiceInterface;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {
    private final SubjectServiceInterface subjectService;

    public SubjectController(SubjectServiceInterface subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping
    public ResponseEntity<SubjectCreateResponse> createSubject(
            @Valid @RequestBody SubjectCreateRequest request,
            Authentication authentication) {
        String email = SecurityUtils.getEmail(authentication);
        String role = SecurityUtils.getRole(authentication);
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
    @GetMapping("/api/subjects/{id}")
    public ResponseEntity<SubjectDTO> getSubject(@PathVariable Long id) {
        return ResponseEntity.ok(subjectService.getSubjectById(id));
    }
}
