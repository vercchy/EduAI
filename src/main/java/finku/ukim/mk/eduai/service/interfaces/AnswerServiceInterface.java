package finku.ukim.mk.eduai.service.interfaces;

import finku.ukim.mk.eduai.dto.CreateAnswerDto;
import finku.ukim.mk.eduai.model.Answer;
import finku.ukim.mk.eduai.model.Question;

public interface AnswerServiceInterface {
    Answer createAnswerForTestQuestion(CreateAnswerDto createAnswerDto, Question question);
}
