package kerjapraktik.facerecbe.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(name = "face_id", nullable = true, columnDefinition = "CHAR(36)", unique = true)
    private UUID faceId;

    @Column(nullable = true)
    private String token;

    @Column(name = "token_expired_at", nullable = true)
    private Long tokenExpiredAt;

}
