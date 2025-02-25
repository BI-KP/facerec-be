package kerjapraktik.facerecbe.services;

import kerjapraktik.facerecbe.dtos.MessageResponse;
import kerjapraktik.facerecbe.dtos.RegisterRequest;
import kerjapraktik.facerecbe.entities.User;
import kerjapraktik.facerecbe.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final ValidationService validationService;

    private final UserRepository userRepository;

    public UserService(ValidationService validationService, UserRepository userRepository) {
        this.validationService = validationService;
        this.userRepository = userRepository;
    }

    public MessageResponse register(RegisterRequest request) {
        validationService.validate(request);

        User user = new User();
        user.setName(request.getName());
        userRepository.save(user);

        return MessageResponse.builder().message("Register Success!").build();
    }

}
