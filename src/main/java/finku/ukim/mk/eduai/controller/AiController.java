package finku.ukim.mk.eduai.controller;


import finku.ukim.mk.eduai.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/evaluation-ai")
public class AiController {

    private final AiService service;

    public AiController(AiService service) {
        this.service = service;
    }


    @PostMapping("/evaluate")
    public ResponseEntity<String> evaluateStudentAnswer(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        String answer = request.get("answer");
        String evaluationResult = service.evaluateStudentAnswer(question, answer);
        return ResponseEntity.ok(evaluationResult);
    }

}
