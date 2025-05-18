package finku.ukim.mk.eduai.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ScoreTrendDto {
    private LocalDate date;
    private double averageScore;

    public ScoreTrendDto(LocalDate date, double v) {
        this.date = date;
        this.averageScore = v;
    }

}

