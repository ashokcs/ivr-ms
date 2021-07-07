package cl.tenpo.loginIVR.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class TenpoException extends RuntimeException {

    private HttpStatus code;
    private String[] reasons;
    private Integer errorCode;

    public TenpoException(String message) {
        super(message);
    }

    public TenpoException(String message, Throwable cause) {
        super(message, cause);
    }

    public TenpoException(HttpStatus code, Integer errorCode) {
        this.errorCode = errorCode;
        this.code = code;
    }

    public TenpoException(HttpStatus code, Integer errorCode, String message) {
        this(message);
        this.code = code;
        this.errorCode = errorCode;
    }

    public TenpoException(Exception cause, HttpStatus code, Integer errorCode) {
        super(cause);
        this.code = code;
        this.errorCode = errorCode;
    }
}