package kerjapraktik.facerecbe.repositories;

import kerjapraktik.facerecbe.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findFirstByToken(String token);

    User findByUsername(String username);

    Boolean existsByUsername(String username);
}
