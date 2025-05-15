package finku.ukim.mk.eduai.repository;

import finku.ukim.mk.eduai.model.ChoiceResponseAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChoiceResponseAnswerRepository extends JpaRepository<ChoiceResponseAnswer, Long> {
    List<ChoiceResponseAnswer> findAllByChoiceResponse_ResponseId(Long responseId);
}
