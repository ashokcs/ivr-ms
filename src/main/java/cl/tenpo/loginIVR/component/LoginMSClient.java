package cl.tenpo.loginIVR.component;

import cl.tenpo.loginIVR.externalservice.login.LoginMSRestClient;
import cl.tenpo.loginIVR.externalservice.login.RequestParam;
import cl.tenpo.loginIVR.externalservice.login.dto.TokenResponse;
import cl.tenpo.loginIVR.externalservice.user.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class LoginMSClient {

    private final LoginMSRestClient loginMSRestClient;

    public TokenResponse loginUser(String email, String password)throws Exception {
        return loginMSRestClient.loginUser(RequestParam.builder()
            .email(email)
            .password(password)
            .app("APP")
            .build()
        );
    }    
}