package finku.ukim.mk.eduai.repository;

import finku.ukim.mk.eduai.model.StudentSubjectAccess;
import finku.ukim.mk.eduai.model.StudentSubjectAccessId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentSubjectAccessRepository extends JpaRepository<StudentSubjectAccess, StudentSubjectAccessId> {
    Optional<StudentSubjectAccess> findById_StudentIdAndId_SubjectId(Long studentId, Long subjectId);

}
