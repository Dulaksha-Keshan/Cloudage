package com.keshan.cloudage.org.common;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class EmailDomainValidator implements ConstraintValidator<NonDisposableEmail,String> {

    private final Set<String> blocked =Set.of(
            "mailinator.com",
            "tempmail.com",
            "10minutemail.com",
            "guerrillamail.com",
            "yopmail.com",
            "trashmail.com",
            "sharklasers.com",
            "getnada.com",
            "maildrop.cc",
            "dispostable.com",
            "fakeinbox.com",
            "mintemail.com");

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {



        if(email == null || !email.contains("@"))return false;

        final int atIndex = email.indexOf("@")+1;

        if (atIndex <= 0 || atIndex>= email.length())return false;

        final String domain = email.substring(atIndex);
        return !blocked.contains(domain);


    }
}
