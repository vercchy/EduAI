package finku.ukim.mk.eduai.repository;

import finku.ukim.mk.eduai.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByUserId(Long userId);

    Optional<Teacher> findByUserEmail(String userEmail);
}
