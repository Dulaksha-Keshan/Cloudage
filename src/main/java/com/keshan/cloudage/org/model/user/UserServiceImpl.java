package com.keshan.cloudage.org.model.user;

import com.keshan.cloudage.org.common.CustomException;
import com.keshan.cloudage.org.dto.UpdatePasswordReq;
import com.keshan.cloudage.org.dto.UpdateProfileInfoReq;
import com.keshan.cloudage.org.model.enums.CustomExceptionCode;
import com.keshan.cloudage.org.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() ->new CustomException(CustomExceptionCode.USER_NOT_FOUND,email));
    }

    @Override
    public void UpdateProfileInfo(UpdateProfileInfoReq req, String userId) {
        final User savedUser = this.userRepository.findById(userId)
                .orElseThrow(() ->new CustomException(CustomExceptionCode.USER_NOT_FOUND_ON_ID,userId));
        this.userMapper.mergeUser(savedUser, req );

        this.userRepository.save(savedUser);
    }

    @Override
    public void ChangePassword(UpdatePasswordReq req, String userId) {
        if( req.getNewPassword() == null
               || !req.getNewPassword().equals(req.getConfirmPassword())){
            throw new CustomException(CustomExceptionCode.PASSWORD_MISMATCH);
        }

        final User savedUser = this.userRepository.findById(userId)
                .orElseThrow(() ->new CustomException(CustomExceptionCode.USER_NOT_FOUND_ON_ID,userId));

       if(!this.passwordEncoder.matches(req.getCurrentPassword(), savedUser.getPassword())){
           throw new CustomException(CustomExceptionCode.PASSWORD_INCORRECT);
       }


       final String encoded = this.passwordEncoder.encode(req.getNewPassword());

       savedUser.setPassword(encoded);

       this.userRepository.save(savedUser);
    }

    @Override
    public void deactivateAccount(String userId) {
        final User savedUser = this.userRepository.findById(userId)
                .orElseThrow(() ->new CustomException(CustomExceptionCode.USER_NOT_FOUND_ON_ID,userId));

        if(!savedUser.isAccountEnabled()){
            throw new CustomException(CustomExceptionCode.ACCOUNT_ALREADY_DEACTIVATED);
        }

        savedUser.setAccountEnabled(false);
        this.userRepository.save(savedUser);

    }

    @Override
    public void reactivateAccount(String userId) {
        final User savedUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.USER_NOT_FOUND_ON_ID,userId));

        if(savedUser.isAccountEnabled()){
            throw new CustomException(CustomExceptionCode.ACCOUNT_ALREADY_ACTIVATED);
        }

        savedUser.setAccountEnabled(true);
        this.userRepository.save(savedUser);
    }

    @Override
    public void deleteAccount(String userId) {

    }


}
