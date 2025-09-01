package com.keshan.cloudage.org.common;

import com.keshan.cloudage.org.model.enums.CustomExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ApplicationExceptionHandler {


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorRes>errorHandler(final CustomException exception){

        final ErrorRes error = ErrorRes.builder()
                            .msg(exception.getMessage())
                            .code(exception.getCode().getHttpStatus())
                            .build();
        return ResponseEntity.status(exception.getCode().getHttpStatus() != null ?
                                    exception.getCode().getHttpStatus() : HttpStatus.BAD_REQUEST
                ).body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorRes>errorHandler(final BadCredentialsException exception){

        final ErrorRes error = ErrorRes.builder()
                    .code(CustomExceptionCode.BAD_CREDENTIALS.getHttpStatus())
                    .msg(CustomExceptionCode.BAD_CREDENTIALS.getErrorMessage())
                    .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorRes>errorHandler(final AuthorizationDeniedException exception){

        final ErrorRes error = ErrorRes.builder()
                .code(CustomExceptionCode.UNAUTHORIZED_ACTION.getHttpStatus())
                .msg(CustomExceptionCode.UNAUTHORIZED_ACTION.getErrorMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorRes>errorHandler() {
        final ErrorRes error = ErrorRes.builder()
                .code(CustomExceptionCode.DISABLED_USER_ERROR.getHttpStatus())
                .msg(CustomExceptionCode.DISABLED_USER_ERROR.getErrorMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorRes>errorHandler(final UsernameNotFoundException exception){

        final ErrorRes error = ErrorRes.builder()
                .msg(CustomExceptionCode.USERNAME_NOT_FOUND.getErrorMessage())
                .code(CustomExceptionCode.USERNAME_NOT_FOUND.getHttpStatus())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ErrorRes>errorHandler(final InternalAuthenticationServiceException exception){

        final ErrorRes error = ErrorRes.builder()
                .msg(CustomExceptionCode.USERNAME_NOT_FOUND.getErrorMessage())
                .code(CustomExceptionCode.USERNAME_NOT_FOUND.getHttpStatus())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRes>errorHandler(final MethodArgumentNotValidException exception){

        Map<String,String> validationErrors = new HashMap<>();

        exception.getBindingResult()
        .getAllErrors().forEach(error ->{
            final String fieldName = ((FieldError) error).getField();
            final String errorMsg =  error.getDefaultMessage();
            validationErrors.put(fieldName,errorMsg);
                });

        ErrorRes error = ErrorRes.builder()
                .validationErrorMap(validationErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRes>errorHandler(final Exception exception){

        final ErrorRes error = ErrorRes.builder()
                .code(CustomExceptionCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .msg(CustomExceptionCode.INTERNAL_SERVER_ERROR.getErrorMessage())
                .build();
        log.error(exception.getMessage(),exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
