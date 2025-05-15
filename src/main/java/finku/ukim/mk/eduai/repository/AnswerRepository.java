package finku.ukim.mk.eduai.repository;

import finku.ukim.mk.eduai.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByQuestionId(Long questionId);
    List<Answer> findAllByQuestionIdAndIsCorrectTrue(Long questionId);
}
