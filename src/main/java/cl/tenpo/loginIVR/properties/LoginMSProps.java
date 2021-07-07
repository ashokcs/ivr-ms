package cl.tenpo.loginIVR.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "login-ms")
public class LoginMSProps {
    private String validatePasswordPath;
}