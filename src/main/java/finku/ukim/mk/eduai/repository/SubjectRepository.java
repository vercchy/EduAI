package finku.ukim.mk.eduai.repository;

import finku.ukim.mk.eduai.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    boolean existsByNameAndTeacherUserEmail(String name, String email);
}
