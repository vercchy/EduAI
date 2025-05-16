package finku.ukim.mk.eduai.service.impl;

import finku.ukim.mk.eduai.dto.*;
import finku.ukim.mk.eduai.exception.*;
import finku.ukim.mk.eduai.model.*;
import finku.ukim.mk.eduai.repository.*;
import finku.ukim.mk.eduai.service.interfaces.SubjectServiceInterface;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SubjectService implements SubjectServiceInterface {
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final StudentSubjectAccessRepository studentSubjectAccessRepository;

    public SubjectService(SubjectRepository subjectRepository, UserRepository userRepository, TeacherRepository teacherRepository, StudentRepository studentRepository, StudentSubjectAccessRepository studentSubjectAccessRepository) {
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.studentSubjectAccessRepository = studentSubjectAccessRepository;
    }

    @Override
    public List<SubjectBasicInfoDto> getSubjectsByRole(String email, String role) {
        List<Subject> subjects;
        System.out.println("getSubjectsByRole called with email: " + email + ", role: " + role); //raboti so ovoj line, ne raboti bez (pojma nemam kako neka stoi)


        switch (role) {
            case "PROFESSOR" -> subjects = subjectRepository.findAllByTeacherUserEmail(email);
            case "STUDENT" -> subjects = studentSubjectAccessRepository.findAllByStudentUserEmail(email)
                    .stream()
                    .map(StudentSubjectAccess::getSubject)
                    .toList();
            default -> throw new InvalidRoleException("Invalid role: " + role);
        }
        return subjects.stream()
                .map(subject -> {
                    User professor = subject.getTeacher().getUser();
                    return new SubjectBasicInfoDto(
                            subject.getId(),
                            subject.getName(),
                            professor.getFullName());
                })
                .toList();
    }

    @Override
    public SubjectDetailsDto getSubjectDetails(Long subjectId, String email, String role) {
        var doesSubjectExist = false;
        switch (role) {
            case "PROFESSOR" -> doesSubjectExist = subjectRepository.existsByIdAndTeacherUserEmail(subjectId, email);
            case "STUDENT" -> doesSubjectExist = studentSubjectAccessRepository.existsBySubjectIdAndStudentUserEmail(subjectId, email);
            default -> throw new InvalidRoleException("Invalid role: " + role);
        }
        if (!doesSubjectExist)
            throw new UnauthorizedActionException("User with email " + email + " is not authorized to view this subject");

        Subject subject = subjectRepository.findById(subjectId).orElseThrow(
                () -> new ResourceNotFoundException("Subject with id " + subjectId + " not found"));
        List<UserDto> students = studentSubjectAccessRepository.findAllBySubjectId(subjectId)
                .stream()
                .map(access -> access.getStudent().getUser())
                .map(UserDto::new)
                .toList();

        return new SubjectDetailsDto(subject, students);
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

    @Override
    public void assignStudentsToSubject(AssignStudentToSubjectRequest request, String teacherEmail) {
        var subjectId = request.getSubjectId();
        var studentEmail = request.getStudentEmail();
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject with id " + subjectId + " not found."));
        if (teacherRepository.findByUserEmail(teacherEmail).isEmpty() || !subject.getTeacher().getUser().getEmail().equals(teacherEmail))
            throw new UnauthorizedActionException("You do not have permission to assign students to this subject.");
        Student student = studentRepository.findByUserEmail(studentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Student record not found for email: " + studentEmail));

        StudentSubjectAccessId accessId = new StudentSubjectAccessId(student.getUserId(), subjectId);
        if (studentSubjectAccessRepository.existsById(accessId))
            throw new DuplicateResourceException("Student already assigned to this subject.");

        StudentSubjectAccess access = StudentSubjectAccess.builder()
                .id(accessId)
                .student(student)
                .subject(subject)
                .build();
        studentSubjectAccessRepository.save(access);
    }

}
