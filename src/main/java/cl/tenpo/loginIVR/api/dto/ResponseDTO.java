package cl.tenpo.loginIVR.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO {
    private Integer code;
    private HttpStatus httpStatus;
    private String message;
    private String status;
    private String url;

    @Override
    public String toString() {
        return "ResponseDTO{" +
                "code=" + this.code +
                ",httpStatus='" + this.httpStatus + "'" +
                ",message='" + this.message + "'" +
                ",status='" + this.status + "'" +
                ",url='" + this.url + "'" +
                '}';
    }
}