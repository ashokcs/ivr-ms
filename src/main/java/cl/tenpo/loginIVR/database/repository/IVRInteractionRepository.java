package cl.tenpo.loginIVR.database.repository;

import cl.tenpo.loginIVR.database.entity.IVRInteractionDTO;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IVRInteractionRepository extends JpaRepository<IVRInteractionDTO, Long> {
    List<IVRInteractionDTO> findByRut(String rut);
    Optional<IVRInteractionDTO> findById(Long id);
}