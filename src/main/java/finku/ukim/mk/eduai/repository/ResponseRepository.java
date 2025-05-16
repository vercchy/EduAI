package finku.ukim.mk.eduai.repository;

import finku.ukim.mk.eduai.model.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
    List<Response> findAllByTestAttemptId(Long testAttemptId);
}
