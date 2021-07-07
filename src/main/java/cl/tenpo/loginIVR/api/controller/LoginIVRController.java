package cl.tenpo.loginIVR.api.controller;

import cl.tenpo.loginIVR.api.dto.RequestDTO;
import cl.tenpo.loginIVR.database.repository.IVRInteractionRepository;
import cl.tenpo.loginIVR.database.repository.IVRRepository;

import cl.tenpo.loginIVR.service.impl.PasswordValidationServiceImpl;
import cl.tenpo.loginIVR.service.impl.RUTValidationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/loginivr")
public class LoginIVRController {

    @Autowired
    private IVRRepository ivrRepo;

    @Autowired
    private IVRInteractionRepository ivrintrepo;

    @Autowired
    private RUTValidationServiceImpl rutValidationServiceImpl;

    @Autowired
    private PasswordValidationServiceImpl passwordValidationService;

    @PostMapping(path = "/validaterut", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> validateRut(@RequestBody RequestDTO requestDTO) {
        return rutValidationServiceImpl.validateRut(requestDTO);
    }

    @PostMapping(path = "/validatepass", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> validatePassword(@RequestBody RequestDTO requestDTO) {
        return passwordValidationService.validatePassword(requestDTO);
    }
}