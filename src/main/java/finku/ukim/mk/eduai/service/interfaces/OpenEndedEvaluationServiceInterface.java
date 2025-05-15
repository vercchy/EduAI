package finku.ukim.mk.eduai.service.interfaces;

import finku.ukim.mk.eduai.model.Response;

import java.util.List;

public interface OpenEndedEvaluationServiceInterface {
    void evaluateOpenEndedResponses(List<Response> responses);
}
