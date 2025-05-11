package finku.ukim.mk.eduai.service.impl;

import finku.ukim.mk.eduai.dto.SubjectCreateRequest;
import finku.ukim.mk.eduai.dto.SubjectCreateResponse;
import finku.ukim.mk.eduai.exception.DuplicateResourceException;
import finku.ukim.mk.eduai.exception.ForbiddenOperationException;
import finku.ukim.mk.eduai.exception.ResourceNotFoundException;
import finku.ukim.mk.eduai.model.*;
import finku.ukim.mk.eduai.repository.SubjectRepository;
import finku.ukim.mk.eduai.repository.TeacherRepository;
import finku.ukim.mk.eduai.repository.UserRepository;
import finku.ukim.mk.eduai.service.interfaces.SubjectServiceInterface;
import org.springframework.stereotype.Service;

@Service
public class SubjectService implements SubjectServiceInterface {
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;

    public SubjectService(SubjectRepository subjectRepository, UserRepository userRepository, TeacherRepository teacherRepository) {
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public SubjectCreateResponse createSubject(SubjectCreateRequest request, String email, String role) {
        if (!Role.PROFESSOR.name().equals(role))
            throw new ForbiddenOperationException("Only professors can create subjects.");
        if (subjectRepository.existsByNameAndTeacherUserEmail(request.getName(), email))
            throw new DuplicateResourceException("Subject already exists.");
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found."));
        Teacher teacher = teacherRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher profile not found for email: " + email));
        DifficultyLevel difficultyLevel = DifficultyLevel.values()[request.getDifficultyLevel()];
        Subject subject = new Subject(
                request.getName(),
                request.getDescription(),
                difficultyLevel,
                teacher
        );
        subjectRepository.save(subject);
        return new SubjectCreateResponse(subject.getId(), subject.getName(), subject.getDescription());
    }
}
