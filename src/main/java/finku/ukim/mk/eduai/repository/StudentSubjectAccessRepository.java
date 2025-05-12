package finku.ukim.mk.eduai.repository;

import finku.ukim.mk.eduai.model.StudentSubjectAccess;
import finku.ukim.mk.eduai.model.StudentSubjectAccessId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentSubjectAccessRepository extends JpaRepository<StudentSubjectAccess, StudentSubjectAccessId> {
    List<StudentSubjectAccess> findAllByStudentUserEmail(String email);
    List<StudentSubjectAccess> findAllBySubjectId(Long subjectId);
    boolean existsBySubjectIdAndStudentUserEmail(Long subjectId, String email);
}
