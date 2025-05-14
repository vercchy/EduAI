package finku.ukim.mk.eduai.repository;

import finku.ukim.mk.eduai.model.TestAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestAttemptRepository extends JpaRepository<TestAttempt, Long> {
}
