package finku.ukim.mk.eduai.repository;

import finku.ukim.mk.eduai.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUserEmail(String userEmail);
}
