package finku.ukim.mk.eduai.repository;

import finku.ukim.mk.eduai.model.OpenEndedResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OpenEndedResponseRepository extends JpaRepository<OpenEndedResponse, Long> {
    Optional<OpenEndedResponse> findByResponseId(Long openEndedId);
}
