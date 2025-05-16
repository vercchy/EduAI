package finku.ukim.mk.eduai.dto;

import finku.ukim.mk.eduai.model.TestAttemptStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestAttemptStatusDto {
    private String name;
    private int value;

    public TestAttemptStatusDto(TestAttemptStatus testAttemptStatus) {
        this.name = testAttemptStatus.name();
        this.value = testAttemptStatus.ordinal();
    }
}
