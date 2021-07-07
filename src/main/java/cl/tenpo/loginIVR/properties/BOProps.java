package cl.tenpo.loginIVR.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "bo")
public class BOProps {
    private String url;
    private String invalidUrl;
}