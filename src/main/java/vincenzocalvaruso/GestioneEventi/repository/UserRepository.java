package vincenzocalvaruso.GestioneEventi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vincenzocalvaruso.GestioneEventi.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    // Utile per il login
    Optional<User> findByEmail(String email);
}