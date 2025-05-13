package finku.ukim.mk.eduai.repository;

import finku.ukim.mk.eduai.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    boolean existsByNameAndTeacherUserEmail(String name, String email);
    boolean existsByIdAndTeacherUserEmail(Long id, String email);
    List<Subject> findAllByTeacherUserEmail(String email);
    Optional<Subject> findByIdAndTeacherUserEmail(Long id, String email);
}
