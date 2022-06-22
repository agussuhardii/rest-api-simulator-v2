package com.agussuhardi.restapisimulator.config.exception;

import com.agussuhardi.restapisimulator.config.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

  public static <T> String getValidationMessage(ConstraintViolation<T> violation) {
    String className = violation.getRootBeanClass().getSimpleName();
    String property = violation.getPropertyPath().toString();
    // Object invalidValue = violation.getInvalidValue();
    String message = violation.getMessage();
    return String.format("%s.%s %s", className, property, message);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<?> handleNoHandlerFound(NoHandlerFoundException ce) {
    return ApiResponse.builder()
        .message(ce.getMessage())
        .status(HttpStatus.NOT_FOUND.value())
        .error(HttpStatus.NOT_FOUND.getReasonPhrase())
        .build()
        .entity();
  }

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<?> handleCustomException(CustomException ce) {
    return ApiResponse.builder()
        .message(ce.getMessage())
        .status(ce.getHttpStatus().value())
        .error(ce.getHttpStatus().getReasonPhrase())
        .build()
        .entity();
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleException(Exception e) {
    return ApiResponse.builder()
        .message(e.getMessage())
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
        .build()
        .entity();
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException e) {
    Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

    List<String> messages =
        constraintViolations.stream()
            .map(constraintViolation -> String.format("%s", constraintViolation.getMessage()))
            .collect(Collectors.toList());

    return ApiResponse.builder()
        .message(messages.get(0))
        .status(HttpStatus.BAD_REQUEST.value())
        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .build()
        .entity();
  }
}

/*
@ExceptionHandler(ConstraintViolationException.class)
ResponseEntity<Set<String>> handleConstraintViolation(ConstraintViolationException e) {
    Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

Set<String> messages = new HashSet<>(constraintViolations.size());
messages.addAll(constraintViolations.stream()
        .map(constraintViolation -> String.format("%s value '%s' %s", constraintViolation.getPropertyPath(),
                constraintViolation.getInvalidValue(), constraintViolation.getMessage()))
        .collect(Collectors.toList()));

return new ResponseEntity<>(messages, HttpStatus.BAD_REQUEST);

}
 */
