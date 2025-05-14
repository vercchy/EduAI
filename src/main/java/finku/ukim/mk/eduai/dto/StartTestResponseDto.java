package finku.ukim.mk.eduai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StartTestResponseDto {
    private Long id;
    private List<TestAttemptQuestionDto> questions;
}
