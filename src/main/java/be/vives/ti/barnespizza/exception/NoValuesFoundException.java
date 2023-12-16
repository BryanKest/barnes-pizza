package be.vives.ti.barnespizza.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoValuesFoundException extends RuntimeException {

    public NoValuesFoundException(String id) {
        super(generateErrorMessage(id));
    }

    private static String generateErrorMessage(String id) {
        return String.format("Invalid or missing ID: %s", id);
    }
}
