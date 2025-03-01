package kerjapraktik.facerecbe.services;

import kerjapraktik.facerecbe.dtos.MessageResponse;
import kerjapraktik.facerecbe.dtos.RegisterRequest;
import kerjapraktik.facerecbe.entities.User;
import kerjapraktik.facerecbe.repositories.UserRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserService {

    private final ValidationService validationService;

    private final UserRepository userRepository;

    private final RestTemplate restTemplate;

    public UserService(ValidationService validationService, UserRepository userRepository, RestTemplate restTemplate) {
        this.validationService = validationService;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public MessageResponse register(RegisterRequest request) {
        validationService.validate(request);

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists!");
        }

        String faceStored = storeFaceEmbedding(request.getUsername(), request.getFile());

        User user = new User();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setFaceId(UUID.fromString(faceStored));

        userRepository.save(user);

        return MessageResponse.builder().message("Register Success!").build();
    }

    private String storeFaceEmbedding(String username, MultipartFile file) {
        String fastApiUrl = "http://localhost:8000/register/?face_id=" + username;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        try {
            body.add("file", new ByteArrayResource(file.getBytes()){
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing file");
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(fastApiUrl, requestEntity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return (String) Objects.requireNonNull(response.getBody()).get("face_id");
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Face recognition service failed");
        }
    }


}
