package finku.ukim.mk.eduai.repository;

import finku.ukim.mk.eduai.model.TestAttempt;
import finku.ukim.mk.eduai.model.TestAttemptStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestAttemptRepository extends JpaRepository<TestAttempt, Long> {
    Optional<TestAttempt> findTestAttemptByTestIdAndStudentUserEmail(Long testId, String studentEmail);
    List<TestAttempt> findAllByTestId(Long testId);
    List<TestAttempt> findByStudentUserIdAndStatus(Long studentId, TestAttemptStatus status);

    @Query("SELECT t FROM TestAttempt t WHERE t.test.id = :testId AND t.test.subject.id = :subjectId ORDER BY t.submissionDate ASC")
    List<TestAttempt> findByTestIdAndSubjectIdOrderByDateAsc(@Param("testId") Long testId, @Param("subjectId") Long subjectId);
}
