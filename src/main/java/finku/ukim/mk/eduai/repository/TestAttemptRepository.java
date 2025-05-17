package finku.ukim.mk.eduai.repository;

import finku.ukim.mk.eduai.model.TestAttempt;
import finku.ukim.mk.eduai.model.TestAttemptStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestAttemptRepository extends JpaRepository<TestAttempt, Long> {
    List<TestAttempt> findByStudentUserIdAndStatus(Long studentId, TestAttemptStatus status);
}
