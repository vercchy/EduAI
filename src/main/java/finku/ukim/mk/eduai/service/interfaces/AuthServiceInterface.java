package finku.ukim.mk.eduai.service.interfaces;

import finku.ukim.mk.eduai.dto.LoginRequest;
import finku.ukim.mk.eduai.dto.LoginResponse;
import finku.ukim.mk.eduai.dto.RegisterRequest;

public interface AuthServiceInterface {
    void register(RegisterRequest registerRequest);
    LoginResponse authenticate(LoginRequest request);
}
