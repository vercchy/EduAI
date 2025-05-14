package finku.ukim.mk.eduai.service.impl;

import finku.ukim.mk.eduai.dto.CreateQuestionDto;
import finku.ukim.mk.eduai.model.Question;
import finku.ukim.mk.eduai.model.QuestionType;
import finku.ukim.mk.eduai.model.Test;
import finku.ukim.mk.eduai.service.interfaces.QuestionServiceInterface;
import org.springframework.stereotype.Service;

@Service
public class QuestionService implements QuestionServiceInterface {
    @Override
    public Question createQuestionForTestFromQuestionDto(CreateQuestionDto createQuestionDto, Test test) {
        QuestionType questionType = QuestionType.fromOrdinal(createQuestionDto.getQuestionType());

        return Question.builder()
                .questionText(createQuestionDto.getQuestionText())
                .questionType(questionType)
                .maxPoints(createQuestionDto.getMaxPoints())
                .test(test)
                .build();
    }
}
