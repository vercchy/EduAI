package finku.ukim.mk.eduai.repository;

import finku.ukim.mk.eduai.model.ChoiceResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChoiceResponseRepository extends JpaRepository<ChoiceResponse, Long> {
    Optional<ChoiceResponse> findByResponseId(Long responseId);
}
