package finku.ukim.mk.eduai.service.impl;

import finku.ukim.mk.eduai.dto.CreateAnswerDto;
import finku.ukim.mk.eduai.model.Answer;
import finku.ukim.mk.eduai.model.Question;
import finku.ukim.mk.eduai.service.interfaces.AnswerServiceInterface;
import org.springframework.stereotype.Service;

@Service
public class AnswerService implements AnswerServiceInterface {
    @Override
    public Answer createAnswerForTestQuestion(CreateAnswerDto createAnswerDto, Question question) {
        return Answer.builder()
                .answerText(createAnswerDto.getAnswerText())
                .isCorrect(createAnswerDto.getIsCorrect())
                .question(question)
                .build();
    }
}
