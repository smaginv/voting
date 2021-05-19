package ru.graduation.voting.web.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.graduation.voting.util.error.ErrorInfo;
import ru.graduation.voting.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;

import java.time.DateTimeException;
import java.util.Objects;


@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ErrorInfo> handleEntityNotFound(NotFoundException ex, HttpServletRequest request) {
        return getErrorInfo(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorInfo> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return getErrorInfo(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateTimeException.class)
    protected ResponseEntity<ErrorInfo> handleDateTime(DateTimeException ex, HttpServletRequest request) {
        return getErrorInfo(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ErrorInfo> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                                     HttpServletRequest request) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.CONFLICT, request.getRequestURI(),
                Objects.requireNonNull(ex.getRootCause()).getMessage());
        return ResponseEntity.status(errorInfo.getStatus()).body(errorInfo);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        ErrorInfo errorInfo = new ErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY, getURI(request), getErrors(ex));
        return ResponseEntity.status(errorInfo.getStatus()).body(errorInfo);
    }

    private ResponseEntity<ErrorInfo> getErrorInfo(Exception ex, HttpServletRequest request, HttpStatus status) {
        return ResponseEntity.status(status).body(new ErrorInfo(status, request.getRequestURI(), ex.getMessage()));
    }

    private String[] getErrors(BindException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
                .toArray(String[]::new);
    }

    private String getURI(WebRequest request) {
        StringBuilder sb = new StringBuilder(request.getDescription(true));
        return sb.substring(sb.indexOf("/"), sb.indexOf(";"));
    }
}