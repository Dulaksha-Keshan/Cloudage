package com.keshan.cloudage.org.common;

import com.keshan.cloudage.org.model.enums.CustomExceptionCode;
import lombok.Getter;


@Getter
public class CustomException extends RuntimeException{

    private final CustomExceptionCode code;

    private final Object [] args;

    public CustomException(CustomExceptionCode code, Object... args) {
        super(getFormattedMessage(code,args));
        this.code = code;
        this.args = args;
    }

    public static String getFormattedMessage(final CustomExceptionCode code, final  Object... args) {
        if(args!=null && args.length>0){
            return String.format(code.getErrorMessage(),args);
        }

        return code.getErrorMessage();

    }
}
