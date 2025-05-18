package finku.ukim.mk.eduai.repository;

import finku.ukim.mk.eduai.model.TestAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestAttemptRepository extends JpaRepository<TestAttempt, Long> {
    Optional<TestAttempt> findTestAttemptByTestIdAndStudentUserEmail(Long testId, String studentEmail);
    List<TestAttempt> findAllByTestId(Long testId);
}
