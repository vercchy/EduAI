package finku.ukim.mk.eduai.dto;

import finku.ukim.mk.eduai.model.Subject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SubjectDetailsDto {
    private Long id;
    private String name;
    private String description;
    private String difficultyLevel;
    private LocalDateTime createdAt;
    private TeacherDto teacherDto;
    private List<UserDto> students;

    public SubjectDetailsDto(Subject subject, List<UserDto> students) {
        this.id = subject.getId();
        this.name = subject.getName();
        this.description = subject.getDescription();
        this.difficultyLevel = subject.getDifficultyLevel().toString();
        this.createdAt = subject.getCreatedAt();
        this.teacherDto = new TeacherDto(subject.getTeacher());
        this.students = students;
    }

}
