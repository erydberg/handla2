package se.rydberg.handla.security;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<HandlaUser, Long> {
    Optional<HandlaUser> findByUsername(String usernamne);
}
