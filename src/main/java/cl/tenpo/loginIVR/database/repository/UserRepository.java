package cl.tenpo.loginIVR.database.repository;

import cl.tenpo.loginIVR.database.entity.UserDTO;

import java.util.Optional;
import java.util.UUID;

import cl.tenpo.loginIVR.event.consumer.UserEventDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDTO, Long> {
    Optional<UserDTO> findById(UUID uuid);
    Optional<UserDTO> findByRut(String rut);
    Optional<UserDTO> findByEmail(String email);
    UserDTO save(UserEventDTO userEventDTO);
    UserDTO save(UserDTO userDTO);
}