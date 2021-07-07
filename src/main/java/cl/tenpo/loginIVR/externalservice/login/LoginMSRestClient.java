package cl.tenpo.loginIVR.externalservice.login;

import cl.tenpo.loginIVR.api.dto.ResponseDTO;
import cl.tenpo.loginIVR.exception.TenpoException;
import cl.tenpo.loginIVR.externalservice.login.dto.TokenResponse;
import cl.tenpo.loginIVR.externalservice.user.dto.UserResponse;
import cl.tenpo.loginIVR.externalservice.user.dto.UserResponseDTO;
import cl.tenpo.loginIVR.properties.LoginMSProps;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class LoginMSRestClient {

    private RestTemplate restTemplate;

    @Autowired
    private LoginMSProps loginMSProps;

    public TokenResponse loginUser(RequestParam request)throws JsonProcessingException {

        String response;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.info("LoginMSProps.getvalidateRutPath: "+loginMSProps.getValidatePasswordPath());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String jsonBody = objectMapper.writeValueAsString(request);
            response = restTemplate.postForObject(
                loginMSProps.getValidatePasswordPath(), new HttpEntity<>(jsonBody, headers),
                    String.class);
            log.info("TokenResponse:"+response);
            TokenResponse tokenResponse = objectMapper.readValue(response, TokenResponse.class);
            return tokenResponse;
        } catch (HttpServerErrorException exception) {
            log.info("HttpServerError_inloginms_call:", exception);
            Writer buffer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(buffer);
            exception.printStackTrace(printWriter);
            log.info("HttpServerError_inloginms_call"+ buffer.toString());
        }catch (HttpClientErrorException exception) {
            log.info("HttpClientrError_inloginms_call:", exception);
            Writer buffer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(buffer);
            exception.printStackTrace(printWriter);
            ResponseDTO responseDTO = objectMapper.readValue(exception.getResponseBodyAsString(), ResponseDTO.class);
            log.info("HttpClientrError_inloginms_call:responseDTO"+responseDTO.toString());
            throw new TenpoException(HttpStatus.valueOf(exception.getRawStatusCode()), responseDTO.getCode(), responseDTO.getMessage());
        }
        catch (JsonProcessingException exception) {
            log.error("JsonProcessingException", exception);
        }

        return null;
    }
}