package finku.ukim.mk.eduai.service.interfaces;

import finku.ukim.mk.eduai.dto.*;
import finku.ukim.mk.eduai.model.Subject;

import java.util.List;

public interface SubjectServiceInterface {
    SubjectCreateResponse createSubject(SubjectCreateRequest request, String email, String role);
    void assignStudentsToSubject(AssignStudentToSubjectRequest request, String studentEmail);
    List<SubjectBasicInfoDto> getSubjectsByRole(String email, String role);
    SubjectDetailsDto getSubjectDetails(Long subjectId, String email, String role);

}
