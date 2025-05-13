package finku.ukim.mk.eduai.model;

import finku.ukim.mk.eduai.exception.InvalidDataException;

import java.util.Arrays;

public enum QuestionType {
    SINGLE_CHOICE,
    MULTIPLE_CHOICE,
    OPEN_ENDED;

    public static QuestionType fromOrdinal(int ordinal) {
        return Arrays.stream(values())
                .filter(qt -> qt.ordinal() == ordinal)
                .findFirst()
                .orElseThrow(() -> new InvalidDataException("Invalid question type ordinal: " + ordinal));
    }
}