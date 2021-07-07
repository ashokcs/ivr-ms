package cl.tenpo.loginIVR.service;

import cl.tenpo.loginIVR.api.dto.RequestDTO;
import org.springframework.http.ResponseEntity;

public interface PasswordValidationService
{
    ResponseEntity validatePassword(RequestDTO requestDTO);
}
