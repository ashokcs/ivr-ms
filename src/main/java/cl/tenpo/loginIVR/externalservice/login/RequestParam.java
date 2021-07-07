package cl.tenpo.loginIVR.externalservice.login;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RequestParam {
    private String email;
    private String password;
    private String app;
}
