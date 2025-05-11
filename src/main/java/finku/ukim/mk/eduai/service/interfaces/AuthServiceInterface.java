package finku.ukim.mk.eduai.service.interfaces;

import finku.ukim.mk.eduai.dto.LoginRequest;
import finku.ukim.mk.eduai.dto.LoginResponse;
import finku.ukim.mk.eduai.dto.RegisterRequest;
import finku.ukim.mk.eduai.model.User;

public interface AuthServiceInterface {
    User register(RegisterRequest registerRequest);
    void createRoleSpecificEntity(User user);
    LoginResponse authenticate(LoginRequest request);
}
