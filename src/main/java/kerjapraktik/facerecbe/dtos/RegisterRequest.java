package kerjapraktik.facerecbe.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "Name must not be empty!")
    @Size(max = 255, message = "Name must not be more than 255 characters!")
    private String name;

    @NotBlank
    @Size(max = 255, message = "Username must not be more than 255 characters!")
    private String username;

    @NotNull(message = "File must not be empty!")
    private MultipartFile file;

}
