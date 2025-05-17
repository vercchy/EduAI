package finku.ukim.mk.eduai.controller;

import finku.ukim.mk.eduai.dto.*;
import finku.ukim.mk.eduai.exception.ForbiddenOperationException;
import finku.ukim.mk.eduai.model.Student;
import finku.ukim.mk.eduai.model.Subject;
import finku.ukim.mk.eduai.model.User;
import finku.ukim.mk.eduai.security.SecurityUtils;
import finku.ukim.mk.eduai.service.interfaces.SubjectServiceInterface;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import finku.ukim.mk.eduai.security.JwtUtil;


@RestController
@RequestMapping("/api/subjects")
public class SubjectController {
    private final SubjectServiceInterface subjectService;
    private String email;
    private String role;
    private final JwtUtil jwtUtil;



    public SubjectController(SubjectServiceInterface subjectService, JwtUtil jwtUtil) {
        this.subjectService = subjectService;
        this.jwtUtil = jwtUtil;
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

    @GetMapping("/student")
    public ResponseEntity<?> getStudentSubjects(@RequestHeader(value = "Authorization", required = false) String token) {
        System.out.println("==> HIT /api/subjects/student endpoint");

        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("Authorization token is missing or invalid");
            }

            token = token.replace("Bearer ", "");
            String email = jwtUtil.extractUsername(token);
            String role = jwtUtil.extractRole(token);

            role = role.replace("ROLE_", "");  // Remove the prefix if present

            if (!"STUDENT".equals(role)) {
                return ResponseEntity.status(403).body("Only students can access this endpoint.");
            }

            List<SubjectBasicInfoDto> subjects = subjectService.getSubjectsByRole(email, role);
            return ResponseEntity.ok(subjects);
        } catch (Exception e) {
            e.printStackTrace(); // or log the error
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }

    @GetMapping("/teacher")
    public ResponseEntity<?> getProfessorSubjects(@RequestHeader(value = "Authorization", required = false) String token) {
        System.out.println("==> HIT /api/subjects/professor endpoint");

        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("Authorization token is missing or invalid");
            }

            token = token.replace("Bearer ", "");
            String email = jwtUtil.extractUsername(token);
            String role = jwtUtil.extractRole(token);

            role = role.replace("ROLE_", "");  // Remove the prefix if present

            if (!"PROFESSOR".equals(role)) {
                return ResponseEntity.status(403).body("Only professors can access this endpoint.");
            }

            List<SubjectBasicInfoDto> subjects = subjectService.getSubjectsByRole(email, role);
            return ResponseEntity.ok(subjects);
        } catch (Exception e) {
            e.printStackTrace(); // or log the error
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }




}
