package kerjapraktik.facerecbe.services;

import kerjapraktik.facerecbe.dtos.LoginRequest;
import kerjapraktik.facerecbe.dtos.LoginResponse;
import kerjapraktik.facerecbe.dtos.MessageResponse;
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

@Service
public class AuthService {

    private final ValidationService validationService;

    private final UserRepository userRepository;

    private final JWTService jwtService;

    private final RestTemplate restTemplate;

    public AuthService(ValidationService validationService, UserRepository userRepository, JWTService jwtService, RestTemplate restTemplate) {
        this.validationService = validationService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public LoginResponse login (LoginRequest request) {
        validationService.validate(request);

        Map recognizeResponse = recognizeFace(request.getFile());
        if ("Face Unknown".equals(recognizeResponse.get("name"))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Face Unknown");
        }
        User user = userRepository.findByUsername((String) recognizeResponse.get("name"));

        String token = jwtService.generateToken(user.getUsername());
        user.setToken(token);
        user.setTokenExpiredAt(nextOneHour());
        userRepository.save(user);

        return LoginResponse.builder()
                .message("Login Success")
                .username(user.getUsername())
                .name(user.getName())
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

    private Map recognizeFace(MultipartFile file) {
        String fastApiUrl = "http://localhost:8000/recognize/";
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to read file");
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(fastApiUrl, requestEntity, Map.class);
            if (response.getStatusCode() == HttpStatus.BAD_REQUEST && Objects.requireNonNull(response.getBody()).get("detail").equals("Spoof detected in the given image.")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Spoof Detected!");
            }
            return response.getBody();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private Long nextOneHour() {
        return System.currentTimeMillis() + (1000 * 60 * 60 * 1);
    }

}
