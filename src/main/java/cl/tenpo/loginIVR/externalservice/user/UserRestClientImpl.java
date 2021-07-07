package cl.tenpo.loginIVR.externalservice.user;

import cl.tenpo.loginIVR.externalservice.user.dto.UserResponse;
import cl.tenpo.loginIVR.externalservice.user.dto.UserResponseDTO;
import cl.tenpo.loginIVR.properties.UsersProps;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class UserRestClientImpl implements UserRestClient {

    @Autowired
    private UsersProps config;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Optional<UserResponse> getUserByRUT(String rut) {
        try {
            log.info("[getUser] get user by Id: [{}]", rut.toString());
            Map<String, String> map = new HashMap<>();
            map.put("rut", rut.toString());
            log.debug("[getUser] URL [{}]", config.getGetUserByRut());
            // Recibiendo respuesta como string y luego mapeandola
            String response = restTemplate.getForObject(config.getGetUserByRut(), String.class, map);
            log.debug("[getUser] Response: [{}]", response);
            ObjectMapper objectMapper = new ObjectMapper();
            UserResponseDTO userResponseDto = objectMapper.readValue(response, UserResponseDTO.class);
            log.debug("[getUser] userResponseDto: [{}]", userResponseDto);
            return Optional.of(userResponseDto.getUser());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("[getUser] User not found ",e);
        }catch (Exception e){
            log.error("[getUser] Exception:",e);
        }
        return Optional.empty();
    }
}
