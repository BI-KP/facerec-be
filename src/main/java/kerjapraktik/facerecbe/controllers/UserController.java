package kerjapraktik.facerecbe.controllers;

import kerjapraktik.facerecbe.dtos.MessageResponse;
import kerjapraktik.facerecbe.dtos.RegisterRequest;
import kerjapraktik.facerecbe.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(
            path = "/api/users/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public MessageResponse register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

}
