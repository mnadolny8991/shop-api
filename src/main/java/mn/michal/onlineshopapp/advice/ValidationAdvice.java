package mn.michal.onlineshopapp.advice;

import jakarta.servlet.annotation.HandlesTypes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidationAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Map<String, String> handleNotValidArgumentException(MethodArgumentNotValidException ex) {
        Map<String, String> jsonMessage = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> jsonMessage.put(error.getField(), error.getDefaultMessage()));
        return jsonMessage;
    }
}
