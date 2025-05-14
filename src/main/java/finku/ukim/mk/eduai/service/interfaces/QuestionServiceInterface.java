package finku.ukim.mk.eduai.service.interfaces;

import finku.ukim.mk.eduai.dto.CreateQuestionDto;
import finku.ukim.mk.eduai.model.Question;
import finku.ukim.mk.eduai.model.Test;

public interface QuestionServiceInterface {
    Question createQuestionForTestFromQuestionDto(CreateQuestionDto createQuestionDto, Test test);
}
