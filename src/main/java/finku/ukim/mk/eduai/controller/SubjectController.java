package finku.ukim.mk.eduai.controller;

import finku.ukim.mk.eduai.dto.SubjectCreateRequest;
import finku.ukim.mk.eduai.dto.SubjectCreateResponse;
import finku.ukim.mk.eduai.service.interfaces.SubjectServiceInterface;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        String email = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        return ResponseEntity.ok(subjectService.createSubject(request, email, role.replace("ROLE_", "")));
    }
}
