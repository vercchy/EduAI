package finku.ukim.mk.eduai.dto;

import finku.ukim.mk.eduai.model.Teacher;
import finku.ukim.mk.eduai.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class TeacherDto {
    private UserDto user;
    private String employmentStatus;

    public TeacherDto(Teacher teacher) {
        this.user = new UserDto(teacher.getUser());
        this.employmentStatus = teacher.getEmploymentStatus().toString();
    }
}
