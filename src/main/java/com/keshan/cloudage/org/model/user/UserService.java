package com.keshan.cloudage.org.model.user;

import com.keshan.cloudage.org.dto.UpdatePasswordReq;
import com.keshan.cloudage.org.dto.UpdateProfileInfoReq;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void UpdateProfileInfo(UpdateProfileInfoReq req , String userId);

    void ChangePassword(UpdatePasswordReq req, String userId);

    void deactivateAccount(String userId);

    void reactivateAccount(String userId);

    void deleteAccount(String userId);

}
