package cl.tenpo.loginIVR.externalservice.user.dto;


import cl.tenpo.loginIVR.model.UserStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse implements Serializable {

    private UUID id;
    private String phone;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    private String email;

    @JsonProperty("regionCode")
    private Integer regionCode;

    @JsonProperty("documentNumber")
    private String documentNumber;

    @JsonProperty("tributaryIdentifier")
    private String tributaryIdentifier;

    private String address;
    private String profession;
    private String nationality;

    @JsonProperty("countryCode")
    private int countryCode;
    private UserStatusEnum state;
}
