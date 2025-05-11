package finku.ukim.mk.eduai.service.interfaces;

import finku.ukim.mk.eduai.dto.AssignStudentToSubjectRequest;
import finku.ukim.mk.eduai.dto.SubjectCreateRequest;
import finku.ukim.mk.eduai.dto.SubjectCreateResponse;

import java.util.List;

public interface SubjectServiceInterface {
    SubjectCreateResponse createSubject(SubjectCreateRequest request, String email, String role);
    void assignStudentsToSubject(AssignStudentToSubjectRequest request, String studentEmail);
}
