package cl.tenpo.loginIVR.util;

import cl.tenpo.loginIVR.model.ErrorConstants;
import cl.tenpo.loginIVR.model.ErrorConstantsEnum;
import cl.tenpo.loginIVR.api.controller.LoginIVRController;
import cl.tenpo.loginIVR.api.dto.RequestDTO;
import cl.tenpo.loginIVR.api.dto.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidationUtil
{
    static Logger logger = LoggerFactory.getLogger(LoginIVRController.class);

    public static boolean validate(RequestDTO requestDTO, ResponseDTO responseDTO, boolean hasPasswordToValidate)
    {

        if (requestDTO == null) {
            responseDTO.setCode(ErrorConstants.MISSING_PARAMETERS);
            responseDTO.setStatus(ErrorConstantsEnum.getStatus(ErrorConstants.MISSING_PARAMETERS));
            responseDTO.setMessage(ErrorConstantsEnum.getStatusMessage(ErrorConstants.MISSING_PARAMETERS));
            logger.info("Request is null");
            return false;
        }

        String rut = requestDTO.getRut();
        String callID = requestDTO.getCall_id();
        String password = requestDTO.getPassword();

        // Parameter rut isempty validation
        if (rut == null || rut.isEmpty()) {
            responseDTO.setCode(ErrorConstants.EMPTY_RUT);
            responseDTO.setStatus(ErrorConstantsEnum.getStatus(ErrorConstants.EMPTY_RUT));
            responseDTO.setMessage(ErrorConstantsEnum.getStatusMessage(ErrorConstants.EMPTY_RUT));
            logger.info("RUT is Empty: "+ rut);
            return false;
        }
        // Parameter call_id isempty validation
        if (callID == null || callID.isEmpty()) {
            responseDTO.setCode(ErrorConstants.EMPTY_CALL_ID);
            responseDTO.setStatus(ErrorConstantsEnum.getStatus(ErrorConstants.EMPTY_CALL_ID));
            responseDTO.setMessage(ErrorConstantsEnum.getStatusMessage(ErrorConstants.EMPTY_CALL_ID));
            return false;
        }

        // RUT format validation total length 10 - Add expressions for format of rut 11111111-1 (length six or seven numbers [0-9] + minus simbol [-] + length one number [0-9] or k letter)
        rut = rut.replaceAll("\\.", "");
        requestDTO.setRut(rut);
        String rutPattern = "^\\d{6,8}+\\-[\\d|k|K]";
        if (rut.matches(rutPattern)) {
            logger.info("RUT_CORRECT_FORMAT: "+rut);
        }

        if (!rut.matches(rutPattern)) {
            responseDTO.setCode(ErrorConstants.INVALID_RUT_FORMAT);
            responseDTO.setStatus(ErrorConstantsEnum.getStatus(ErrorConstants.INVALID_RUT_FORMAT));
            responseDTO.setMessage(ErrorConstantsEnum.getStatusMessage(ErrorConstants.INVALID_RUT_FORMAT));
            logger.info("validation_response: "+ responseDTO);
            return false;
        }

        if(hasPasswordToValidate)
        {
            // Parameter password  isemptyvalidation
            if (password == null || password.isEmpty()) {
                responseDTO.setCode(ErrorConstants.EMPTY_PASSWORD);
                responseDTO.setStatus(ErrorConstantsEnum.getStatus(ErrorConstants.EMPTY_PASSWORD));
                responseDTO.setMessage(ErrorConstantsEnum.getStatusMessage(ErrorConstants.EMPTY_PASSWORD));
                return false;
            }

            // Password format validation total length 4 - Only Number
            if (password.length() != 4  || !password.matches("^\\d{4}")) {
                responseDTO.setCode(ErrorConstants.INVALID_PASS_FORMAT);
                responseDTO.setStatus(ErrorConstantsEnum.getStatus(ErrorConstants.INVALID_PASS_FORMAT));
                responseDTO.setMessage(ErrorConstantsEnum.getStatusMessage(ErrorConstants.INVALID_PASS_FORMAT));
                return false;
            }
        }

        return true;
    }
}
