package kerjapraktik.facerecbe.services;

import kerjapraktik.facerecbe.dtos.LoginRequest;
import kerjapraktik.facerecbe.dtos.LoginResponse;
import kerjapraktik.facerecbe.dtos.MessageResponse;
import kerjapraktik.facerecbe.entities.User;
import kerjapraktik.facerecbe.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final ValidationService validationService;
    private final UserRepository userRepository;
    private final JWTService jwtService;

    public AuthService(ValidationService validationService, UserRepository userRepository, JWTService jwtService) {
        this.validationService = validationService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public LoginResponse login (LoginRequest request) {
        validationService.validate(request);
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credential!"));
        String token = jwtService.generateToken(user.getUsername());
        user.setToken(token);
        user.setTokenExpiredAt(nextOneHour());
        userRepository.save(user);

        return LoginResponse.builder()
                .message("Login Success")
                .token(token)
                .build();
    }

    @Transactional
    public MessageResponse logout (User user) {
        user.setToken(null);
        user.setTokenExpiredAt(null);
        userRepository.save(user);

        return MessageResponse.builder().message("Logout Success").build();
    }

    private Long nextOneHour() {
        return System.currentTimeMillis() + (1000 * 60 * 60 * 1);
    }

}
