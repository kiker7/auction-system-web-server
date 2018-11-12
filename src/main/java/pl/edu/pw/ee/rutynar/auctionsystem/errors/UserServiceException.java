package pl.edu.pw.ee.rutynar.auctionsystem.errors;

import org.springframework.http.HttpStatus;

public class UserServiceException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;


    public UserServiceException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
