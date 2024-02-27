package asavershin.note.controllers;

import asavershin.note.exceptions.EntityNotFoundException;
import asavershin.note.exceptions.ExceptionBody;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleValidationException(MethodArgumentNotValidException ex) {
        var exceptionBody = new ExceptionBody("Validation failed: ");
        Map<String, Object> body = new HashMap<>();
        body.put("errors",
                ex.getBindingResult().getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage).filter(Objects::nonNull)
                        .toList());
        exceptionBody.setErrors(body);
        return exceptionBody;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleConstraintViolationException(ConstraintViolationException ex) {
        var exceptionBody = new ExceptionBody("Validation failed: ");
        Map<String, Object> body = new HashMap<>();
        body.put("errors",
                ex.getConstraintViolations().stream()
                        .map(ConstraintViolation::getMessage).filter(Objects::nonNull)
                        .toList());
        exceptionBody.setErrors(body);
        return exceptionBody;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionBody handleEntityNotFoundException(EntityNotFoundException ex){
        return new ExceptionBody(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBody handleException(
            final Exception e
    ) {
        e.printStackTrace();
        return new ExceptionBody("Internal error");
    }
}
