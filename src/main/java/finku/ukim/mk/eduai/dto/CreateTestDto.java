package finku.ukim.mk.eduai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CreateTestDto {
    private BasicTestCreationInfoDto basicTestCreationInfo;
    private List<QuestionDto> questions;
}
