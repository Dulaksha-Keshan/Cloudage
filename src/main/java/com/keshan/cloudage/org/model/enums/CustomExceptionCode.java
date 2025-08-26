package com.keshan.cloudage.org.model.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum CustomExceptionCode {


    USER_NOT_FOUND("User Not found on user name %s",HttpStatus.NOT_FOUND),
    USER_NOT_FOUND_ON_ID("User Not found on user ID %s",HttpStatus.NOT_FOUND),
    PASSWORD_MISMATCH("Invalid password Confirmation" ,HttpStatus.BAD_REQUEST),
    PASSWORD_INCORRECT("Incorrect Current password" ,HttpStatus.BAD_REQUEST),
    ACCOUNT_ALREADY_ACTIVATED("Account already Activated" ,HttpStatus.BAD_REQUEST),
    ACCOUNT_ALREADY_DEACTIVATED("Account already Deactivated" ,HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXITS("Entered Email is already registered ", HttpStatus.BAD_REQUEST );



    @Getter
    private final String errorMessage;
    @Getter
    private final HttpStatus httpStatus;




    CustomExceptionCode(String errorMessage, HttpStatus httpStatus) {
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }




}
