package kerjapraktik.facerecbe.controllers;

import kerjapraktik.facerecbe.dtos.LoginRequest;
import kerjapraktik.facerecbe.dtos.LoginResponse;
import kerjapraktik.facerecbe.dtos.MessageResponse;
import kerjapraktik.facerecbe.entities.User;
import kerjapraktik.facerecbe.services.AuthService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(
            path = "/api/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public LoginResponse loginResponse(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping(
            path = "/api/auth/logout",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public MessageResponse logout(User user) {
        return authService.logout(user);
    }
}
