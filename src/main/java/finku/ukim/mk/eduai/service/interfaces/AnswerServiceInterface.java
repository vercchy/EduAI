package finku.ukim.mk.eduai.service.interfaces;

import finku.ukim.mk.eduai.dto.AnswerDto;
import finku.ukim.mk.eduai.model.Answer;
import finku.ukim.mk.eduai.model.Question;

public interface AnswerServiceInterface {
    Answer createAnswerForTestQuestion(AnswerDto answerDto, Question question);
}
