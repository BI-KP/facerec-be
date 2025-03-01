package kerjapraktik.facerecbe.controllers;

import kerjapraktik.facerecbe.dtos.MessageResponse;
import kerjapraktik.facerecbe.dtos.RegisterRequest;
import kerjapraktik.facerecbe.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(
            path = "/api/users/register",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public MessageResponse register(
            @RequestPart("name") String name,
            @RequestPart("username") String username,
            @RequestPart("file") MultipartFile file
    ) {
        RegisterRequest request = new RegisterRequest();
        request.setUsername(username);
        request.setName(name);
        request.setFile(file);
        return userService.register(request);
    }

}
