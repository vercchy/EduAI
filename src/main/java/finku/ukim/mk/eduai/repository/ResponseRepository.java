package finku.ukim.mk.eduai.repository;

import finku.ukim.mk.eduai.model.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
    List<Response> findByTestAttemptStudentUserId(Long studentId);

    @Query("SELECT r FROM Response r WHERE r.testAttempt.test.id = :testId AND r.testAttempt.test.subject.id = :subjectId")
    List<Response> findByTestIdAndSubjectId(@Param("testId") Long testId, @Param("subjectId") Long subjectId);
    List<Response> findAllByTestAttemptId(Long testAttemptId);
}
