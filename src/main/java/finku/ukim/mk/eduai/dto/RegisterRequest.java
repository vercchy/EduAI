package finku.ukim.mk.eduai.dto;

import lombok.*;

@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}

