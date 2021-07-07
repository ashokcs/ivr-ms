package cl.tenpo.loginIVR.service;

import cl.tenpo.loginIVR.api.dto.RequestDTO;
import org.springframework.http.ResponseEntity;

public interface RUTValidationService
{
    ResponseEntity validateRut(RequestDTO requestDTO);
}
