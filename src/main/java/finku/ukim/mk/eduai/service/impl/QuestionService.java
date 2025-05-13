package finku.ukim.mk.eduai.service.impl;

import finku.ukim.mk.eduai.dto.QuestionDto;
import finku.ukim.mk.eduai.model.Question;
import finku.ukim.mk.eduai.model.QuestionType;
import finku.ukim.mk.eduai.model.Test;
import finku.ukim.mk.eduai.service.interfaces.QuestionServiceInterface;
import org.springframework.stereotype.Service;

@Service
public class QuestionService implements QuestionServiceInterface {
    @Override
    public Question createQuestionForTestFromQuestionDto(QuestionDto questionDto, Test test) {
        QuestionType questionType = QuestionType.fromOrdinal(questionDto.getQuestionType());

        return Question.builder()
                .questionText(questionDto.getQuestionText())
                .questionType(questionType)
                .maxPoints(questionDto.getMaxPoints())
                .test(test)
                .build();
    }
}
