package kerjapraktik.facerecbe.services;

import kerjapraktik.facerecbe.dtos.MessageResponse;
import kerjapraktik.facerecbe.dtos.RegisterRequest;
import kerjapraktik.facerecbe.entities.User;
import kerjapraktik.facerecbe.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final ValidationService validationService;

    private final UserRepository userRepository;

    public UserService(ValidationService validationService, UserRepository userRepository) {
        this.validationService = validationService;
        this.userRepository = userRepository;
    }

    @Transactional
    public MessageResponse register(RegisterRequest request) {
        validationService.validate(request);

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists!");
        }
        User user = new User();
        user.setName(request.getName());
        user.setUsername(request.getUsername());

        userRepository.save(user);

        return MessageResponse.builder().message("Register Success!").build();
    }

}
