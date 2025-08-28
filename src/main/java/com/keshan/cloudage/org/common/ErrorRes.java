package com.keshan.cloudage.org.common;


import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorRes {

    private String msg;
    private HttpStatus code ;
    private Map<String,String> validationErrorMap;
}
