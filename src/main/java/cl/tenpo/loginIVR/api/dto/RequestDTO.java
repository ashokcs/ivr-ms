package cl.tenpo.loginIVR.api.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDTO {
    private String rut;
    private String call_id;
    private String email;
    private String password;
}