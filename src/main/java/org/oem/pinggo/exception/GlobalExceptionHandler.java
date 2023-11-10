package org.oem.pinggo.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {
        return new ResponseEntity<Object>("Access denied message here", new HttpHeaders(), HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(OrderStatusNoEligibleForEditException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<Object> handleOrderStatusNoEligibleForEditException(OrderStatusNoEligibleForEditException orderStatusNoEligibleForEditException, WebRequest request) {
        return buildErrorResponse(orderStatusNoEligibleForEditException, HttpStatus.NOT_FOUND, request);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(HttpServletRequest request, Exception ex) {


        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Controller Validation Error " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(errorResponse);

    }


    @ExceptionHandler({AuthenticationException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception ex) {

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Authentication failed at controller advice");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(errorResponse);
    }


    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Validation error. Check 'errors' field for details.");
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }


    @ExceptionHandler(NoSuchElementFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNoSuchElementFoundException(NoSuchElementFoundException itemNotFoundException, WebRequest request) {
        return buildErrorResponse(itemNotFoundException, HttpStatus.NOT_FOUND, request);
    }
    @ExceptionHandler(ItemOwnerException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleItemOwnerException(ItemOwnerException itemOwnerException, WebRequest request) {
        return buildErrorResponse(itemOwnerException, HttpStatus.NOT_FOUND, request);
    }



    @ExceptionHandler(InsufficientResourceException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<Object> handleInsufficientResourceException(InsufficientResourceException insufficientResourceException, WebRequest request) {
        return buildErrorResponse(insufficientResourceException, HttpStatus.NOT_ACCEPTABLE, request);
    }


    @ExceptionHandler(UserNotFoundWithIdException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleUserNotFoundWithIdException(UserNotFoundWithIdException userNotFoundWithIdException, WebRequest request) {
        return buildErrorResponse(userNotFoundWithIdException, HttpStatus.NOT_FOUND, request);
    }



    @ExceptionHandler(UserNotFoundWithEmailException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleUserNotFoundWithEmailException(UserNotFoundWithEmailException userNotFoundWithEmailException, WebRequest request) {
        return buildErrorResponse(userNotFoundWithEmailException, HttpStatus.NOT_FOUND, request);
    }



    @ExceptionHandler(TimeoutVerificationTokenException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleTimeoutVerificationTokenException(TimeoutVerificationTokenException timeoutVerificationTokenException, WebRequest request) {
        return buildErrorResponse(timeoutVerificationTokenException, HttpStatus.NOT_FOUND, request);
    }


    @ExceptionHandler(InvalidVerificationTokenException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleInvalidVerificationTokenException(InvalidVerificationTokenException invalidVerificationTokenException, WebRequest request) {
        return buildErrorResponse(invalidVerificationTokenException, HttpStatus.NOT_FOUND, request);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(Exception exception, WebRequest request) {


        return buildErrorResponse(exception, "..."+exception.getMessage() + "...", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception exception, HttpStatus httpStatus, WebRequest request) {
        return buildErrorResponse(exception, exception.getMessage(), httpStatus, request);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception exception, String message, HttpStatus httpStatus, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }


    public ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return buildErrorResponse(ex, status, request);
    }
}