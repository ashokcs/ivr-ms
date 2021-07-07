package cl.tenpo.loginIVR.database.repository;


import cl.tenpo.loginIVR.database.entity.IVRDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IVRRepository extends JpaRepository<IVRDTO, Long> {

}
