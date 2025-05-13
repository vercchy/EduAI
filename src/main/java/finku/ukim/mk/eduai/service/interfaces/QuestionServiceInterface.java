package finku.ukim.mk.eduai.service.interfaces;

import finku.ukim.mk.eduai.dto.QuestionDto;
import finku.ukim.mk.eduai.model.Question;
import finku.ukim.mk.eduai.model.Test;

public interface QuestionServiceInterface {
    Question createQuestionForTestFromQuestionDto(QuestionDto questionDto, Test test);
}
