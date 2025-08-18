package com.keshan.cloudage.org.model.user;


import com.keshan.cloudage.org.dto.UpdateProfileInfoReq;
import io.micrometer.common.util.StringUtils;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public void mergeUser(final User savedUser, final UpdateProfileInfoReq req) {
        if (StringUtils.isNotBlank(req.getFullName())
                && !savedUser.getFullName().equals(req.getFullName())
        ){
            savedUser.setFullName(req.getFullName());
        }

        if ( req.getDateOfBirth() != null
                && !req.getDateOfBirth().isEqual(savedUser.getDateOfBirth())
        ){
            savedUser.setDateOfBirth(req.getDateOfBirth());
        }
    }

}
