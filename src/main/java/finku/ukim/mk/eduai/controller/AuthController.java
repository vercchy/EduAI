package finku.ukim.mk.eduai.controller;

import finku.ukim.mk.eduai.dto.LoginRequest;
import finku.ukim.mk.eduai.dto.LoginResponse;
import finku.ukim.mk.eduai.dto.RegisterRequest;
import finku.ukim.mk.eduai.model.User;
import finku.ukim.mk.eduai.service.interfaces.AuthServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthServiceInterface authService;

    public AuthController(AuthServiceInterface authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = authService.register(request);
            authService.createRoleSpecificEntity(user);
            return ResponseEntity.ok("Successfully registered");
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.authenticate(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
