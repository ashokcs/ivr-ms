package cl.tenpo.loginIVR.service.impl;

import cl.tenpo.loginIVR.api.dto.*;
import cl.tenpo.loginIVR.database.entity.IVRInteractionDTO;
import cl.tenpo.loginIVR.database.entity.UserDTO;
import cl.tenpo.loginIVR.database.repository.UserRepository;
import cl.tenpo.loginIVR.externalservice.user.UserRestClientImpl;
import cl.tenpo.loginIVR.externalservice.user.dto.UserResponse;
import cl.tenpo.loginIVR.model.ErrorConstants;
import cl.tenpo.loginIVR.model.ErrorConstantsEnum;
import cl.tenpo.loginIVR.api.controller.LoginIVRController;
import cl.tenpo.loginIVR.properties.UsersProps;
import cl.tenpo.loginIVR.util.ValidationUtil;
import cl.tenpo.loginIVR.model.UserStatusEnum;
import cl.tenpo.loginIVR.database.repository.IVRInteractionRepository;
import cl.tenpo.loginIVR.service.RUTValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RUTValidationServiceImpl implements RUTValidationService
{
    static Logger logger = LoggerFactory.getLogger(LoginIVRController.class);

    @Autowired
    private IVRInteractionRepository ivrintrepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserRestClientImpl userRestClient;

    @Autowired
    private UsersProps usersProps;

    public ResponseEntity validateRut(RequestDTO requestDTO)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        if(!ValidationUtil.validate(requestDTO, responseDTO, false))
        {
            return new ResponseEntity<Object>(responseDTO, HttpStatus.BAD_REQUEST);
        }

        String rut = requestDTO.getRut();
        Optional<UserDTO> userDTO = userRepo.findByRut(rut);
        if(!userDTO.isPresent())
        {
            Optional<UserResponse> userResponse = userRestClient.getUserByRUT(rut);
            if(userResponse.isPresent())
            {
                userDTO = Optional.of(UserDTO.builder().id(userResponse.get().getId())
                        .email(userResponse.get().getEmail())
                        .rut(rut)
                        .status(userResponse.get().getState().getStatus())
                        .build());
                userRepo.save(userDTO.get());
            }
        }

        if (!userDTO.isPresent()) {
            responseDTO.setCode(ErrorConstants.CLIENT_NOT_FOUND);
            responseDTO.setStatus(ErrorConstantsEnum.getStatus(ErrorConstants.CLIENT_NOT_FOUND));
            responseDTO.setMessage(ErrorConstantsEnum.getStatusMessage(ErrorConstants.CLIENT_NOT_FOUND));
            ivrintrepo.save(new IVRInteractionDTO(requestDTO.getCall_id(), requestDTO.getRut(), "validateRut", "CLIENT_NOT_FOUND"));
            return new ResponseEntity<Object>(responseDTO, HttpStatus.NOT_FOUND);
        } else {
            if (userDTO.get().getStatus() == UserStatusEnum.ACTIVE.getStatus()) {
                responseDTO.setCode(201);
                responseDTO.setStatus("CLIENT_EXISTS");
                responseDTO.setMessage("Rut belongs to a Tenpo user");
                ivrintrepo.save(new IVRInteractionDTO(requestDTO.getCall_id(), requestDTO.getRut(), "validateRut", "CLIENT_EXISTS"));
                return new ResponseEntity<Object>(responseDTO, HttpStatus.OK);
            } else {
                responseDTO.setCode(ErrorConstants.CLIENT_IS_BLOCKED);
                responseDTO.setStatus(ErrorConstantsEnum.getStatus(ErrorConstants.CLIENT_IS_BLOCKED));
                responseDTO.setMessage(ErrorConstantsEnum.getStatusMessage(ErrorConstants.CLIENT_IS_BLOCKED));
                ivrintrepo.save(new IVRInteractionDTO(requestDTO.getCall_id(), requestDTO.getRut(), "validateRut", "CLIENT_IS_BLOCKED"));
                return new ResponseEntity<Object>(responseDTO, HttpStatus.NOT_FOUND);
            }
        }
    }
}
