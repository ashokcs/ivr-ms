package cl.tenpo.loginIVR.service.impl;

import cl.tenpo.loginIVR.component.LoginMSClient;
import cl.tenpo.loginIVR.database.entity.IVRInteractionDTO;
import cl.tenpo.loginIVR.database.entity.IVRDTO;
import cl.tenpo.loginIVR.database.entity.UserDTO;
import cl.tenpo.loginIVR.database.repository.UserRepository;
import cl.tenpo.loginIVR.externalservice.login.dto.TokenResponse;
import cl.tenpo.loginIVR.externalservice.user.UserRestClientImpl;
import cl.tenpo.loginIVR.externalservice.user.dto.UserResponse;
import cl.tenpo.loginIVR.model.ErrorConstants;
import cl.tenpo.loginIVR.model.ErrorConstantsEnum;
import cl.tenpo.loginIVR.api.controller.LoginIVRController;
import cl.tenpo.loginIVR.api.dto.*;
import cl.tenpo.loginIVR.exception.TenpoException;
import cl.tenpo.loginIVR.model.UserStatusEnum;
import cl.tenpo.loginIVR.util.ValidationUtil;
import cl.tenpo.loginIVR.database.repository.IVRInteractionRepository;
import cl.tenpo.loginIVR.database.repository.IVRRepository;
import cl.tenpo.loginIVR.properties.BOProps;
import cl.tenpo.loginIVR.service.PasswordValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordValidationServiceImpl implements PasswordValidationService
{
    static Logger logger = LoggerFactory.getLogger(LoginIVRController.class);

    @Autowired
    private IVRInteractionRepository ivrintrepo;

    @Autowired
    private IVRRepository ivrRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private LoginMSClient loginMSClient;

    @Autowired
    private UserRestClientImpl userRestClient;

    @Autowired
    private BOProps boProps;

    public ResponseEntity validatePassword(RequestDTO requestDTO)
    {
        ResponseDTO responseDTO = new ResponseDTO();
        if(!ValidationUtil.validate(requestDTO, responseDTO, true))
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

        if(!userDTO.isPresent()){
            responseDTO.setCode(ErrorConstants.CLIENT_NOT_FOUND);
            responseDTO.setStatus(ErrorConstantsEnum.getStatus(ErrorConstants.CLIENT_NOT_FOUND));
            responseDTO.setMessage(ErrorConstantsEnum.getStatusMessage(ErrorConstants.CLIENT_NOT_FOUND));
            return new ResponseEntity<Object>(responseDTO, HttpStatus.NOT_FOUND);
        } 
        else if(UserStatusEnum.PASSWORD_LOCKED.getStatus() == userDTO.get().getStatus())
        {
            throw new TenpoException(HttpStatus.UNPROCESSABLE_ENTITY, ErrorConstants.PASSWORD_LOCKED);
        }

        logger.info("Client found:"+ userDTO);
        requestDTO.setEmail(userDTO.get().getEmail());
        UUID userUUID = userDTO.get().getId();
        Object loginResponseObject = validatePasswordInLoginMS(requestDTO);
        if (loginResponseObject instanceof TokenResponse && ((TokenResponse)loginResponseObject).getAccessToken() != null)
        {
            IVRDTO IVRDTO = new IVRDTO(userUUID, requestDTO.getRut());
            logger.info("IVRDTO_TO_BE_PERSISTED: "+ IVRDTO);
            ivrRepo.save(IVRDTO);
            logger.info("IVRDTO_PERSISTED: "+ IVRDTO);
            responseDTO.setCode(201);
            responseDTO.setStatus("RUT_CLIENT_VALIDATED");
            responseDTO.setMessage("Rut and Password are validated successfully.");
            responseDTO.setUrl(boProps.getUrl() + IVRDTO.getToken());
            IVRInteractionDTO ivrInteractionDTO = new IVRInteractionDTO(requestDTO.getCall_id(), requestDTO.getRut(), "validatePassword", "RUT_CLIENT_VALIDATED");
            ivrintrepo.save(ivrInteractionDTO);
            logger.info("IVR_Interaction_DTO_TO_BE_PERSISTED: " + ivrInteractionDTO);
            return new ResponseEntity<Object>(responseDTO, HttpStatus.OK);
        }
        else if(loginResponseObject instanceof ResponseDTO){
            IVRInteractionDTO ivrInteractionDTO = new IVRInteractionDTO(requestDTO.getCall_id(), requestDTO.getRut(), "validatePassword", "INTERNAL_SERVER_ERROR");
            ivrintrepo.save(ivrInteractionDTO);
            logger.info("IVR_Interaction_DTO_TO_BE_PERSISTED: " + ivrInteractionDTO);
            logger.info("[login] responsedto_to_be_printed"+((ResponseDTO)loginResponseObject).toString());
            responseDTO = (ResponseDTO) loginResponseObject;
            HttpStatus httpStatus = responseDTO.getHttpStatus() == null ? HttpStatus.BAD_REQUEST : responseDTO.getHttpStatus();
            responseDTO.setHttpStatus(null);
            responseDTO.setUrl(boProps.getInvalidUrl());
            return new ResponseEntity<Object>(responseDTO, httpStatus);
        }
        else 
        {
            throw new TenpoException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorConstants.INTERNAL_SERVER_ERROR);
        }
    }

    private Object validatePasswordInLoginMS(RequestDTO requestDTO)
    {
        Object responseObject = null;
        try {
            //Try Login in AZ AD
            responseObject = loginMSClient.loginUser(requestDTO.getEmail(), requestDTO.getPassword());
            logger.info("[login] Login Success");
        } catch (TenpoException e) {
           logger.info("[login] If the login fails in login ms, we check if the user exists: "+e.getStackTrace());
            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.setCode(e.getErrorCode());
            responseDTO.setMessage(e.getMessage());
            responseDTO.setHttpStatus(e.getCode());
            return responseDTO;
           
        } catch (Exception e)
        {
            throw new TenpoException(HttpStatus.NOT_FOUND, ErrorConstants.INVALID_CREDENTIALS);
        }

        return responseObject;
    }
}
