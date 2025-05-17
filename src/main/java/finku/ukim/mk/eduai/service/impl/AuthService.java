package finku.ukim.mk.eduai.service.impl;

import finku.ukim.mk.eduai.dto.LoginRequest;
import finku.ukim.mk.eduai.dto.LoginResponse;
import finku.ukim.mk.eduai.dto.RegisterRequest;
import finku.ukim.mk.eduai.exception.InvalidRoleException;
import finku.ukim.mk.eduai.model.*;
import finku.ukim.mk.eduai.repository.StudentRepository;
import finku.ukim.mk.eduai.repository.TeacherRepository;
import finku.ukim.mk.eduai.repository.UserRepository;
import finku.ukim.mk.eduai.security.JwtUtil;
import finku.ukim.mk.eduai.service.CustomUserDetailsService;
import finku.ukim.mk.eduai.service.interfaces.AuthServiceInterface;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class AuthService implements AuthServiceInterface {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil,
                       CustomUserDetailsService customUserDetailsService,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder, StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    @Transactional
    public User register(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        if (userRepository.findUserByEmail(email).isPresent())
            throw new IllegalArgumentException("Email Already Registered");
        if (!isPasswordStrong(registerRequest.getPassword()))
            throw new IllegalArgumentException("Password does not meet strength requirements.");
        Role role = determineRoleFromEmail(email);
        User newUser = new User(
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                email,
                passwordEncoder.encode(registerRequest.getPassword()),
                LocalDateTime.now(),
                role
        );
        userRepository.save(newUser);
        userRepository.flush();
        createRoleSpecificEntity(newUser);
        return newUser;
    }

    @Override
    public void createRoleSpecificEntity(User user) {
        Role role = user.getRole();
        switch(role) {
            case STUDENT ->  {
                Student student = new Student();
                student.setUser(user);
                studentRepository.save(student);
            }
            case PROFESSOR ->
            {
                Teacher teacher = new Teacher();
                teacher.setUser(user);
                teacher.setEmploymentStatus(EmploymentStatus.FULL_TIME);
                teacherRepository.save(teacher);
            }
            default -> throw new InvalidRoleException("Invalid role: " + role);
        }
    }

    @Override
    public LoginResponse authenticate(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid email or password");
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());
        String roleName = userDetails.getAuthorities().iterator().next().getAuthority();

        return new LoginResponse(jwtUtil.generateToken(userDetails.getUsername(), roleName, request.getEmail()));
    }

    private boolean isPasswordStrong(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[^a-zA-Z0-9].*");
    }

    private Role determineRoleFromEmail(String email) {
        String domain = email.substring(email.indexOf("@") + 1);
        return switch (domain) {
            case "students.finki.ukim.mk" -> Role.STUDENT;
            case "finki.ukim.mk" -> Role.PROFESSOR;
            default -> throw new IllegalArgumentException("Unsupported email domain: " + domain);
        };
    }
}
