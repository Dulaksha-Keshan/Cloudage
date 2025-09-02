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
    EMAIL_ALREADY_EXITS("Entered Email is already registered ", HttpStatus.BAD_REQUEST ),
    BAD_CREDENTIALS("Invalid User Name or Password" ,HttpStatus.UNAUTHORIZED),
    INTERNAL_SERVER_ERROR("Internal Server error has occurred",HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED_ACTION("No Authorization to perform this action ",HttpStatus.UNAUTHORIZED),
    DISABLED_USER_ERROR("User account is disabled, please activate your account",HttpStatus.UNAUTHORIZED),
    IMAGE_NOT_FOUND("Image Not found on key  %s",HttpStatus.NOT_FOUND),
    INVALID_MIME_TYPE("Invalid file format Type %s ", HttpStatus.BAD_REQUEST ),
    INTERNAL_S3_ERROR("Internal exception with S3 service",HttpStatus.SERVICE_UNAVAILABLE),
    INTERNAL_SERVICE_IO_ERROR("Internal IO exception on edit service",HttpStatus.SERVICE_UNAVAILABLE),
    USERNAME_NOT_FOUND( "Cannot find user with the provided username", HttpStatus.NOT_FOUND);



    @Getter
    private final String errorMessage;
    @Getter
    private final HttpStatus httpStatus;




    CustomExceptionCode(String errorMessage, HttpStatus httpStatus) {
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }




}
