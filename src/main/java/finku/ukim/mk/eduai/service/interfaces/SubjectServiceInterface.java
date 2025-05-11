package finku.ukim.mk.eduai.service.interfaces;

import finku.ukim.mk.eduai.dto.SubjectCreateRequest;
import finku.ukim.mk.eduai.dto.SubjectCreateResponse;

public interface SubjectServiceInterface {
    SubjectCreateResponse createSubject(SubjectCreateRequest request, String email, String role);
}
