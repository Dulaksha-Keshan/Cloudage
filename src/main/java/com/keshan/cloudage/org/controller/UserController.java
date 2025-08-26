package com.keshan.cloudage.org.controller;


import com.keshan.cloudage.org.dto.UpdatePasswordReq;
import com.keshan.cloudage.org.dto.UpdateProfileInfoReq;
import com.keshan.cloudage.org.model.user.User;
import com.keshan.cloudage.org.model.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")
public class UserController {

    final UserService userService;

    @PostMapping("/me")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void updateProfile(
            @RequestBody
            @Valid
            final UpdateProfileInfoReq req,
            final Authentication principal
    ){
        userService.UpdateProfileInfo(req,getUserId(principal));
    }

    @PostMapping("/me/password")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void changePassword (
            @RequestBody
            @Valid
            final UpdatePasswordReq req,
            final Authentication principal
            ){
        userService.ChangePassword(req,getUserId(principal));

    }


    @PatchMapping("/me/deactivate")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deactivateAccount(
           final  Authentication principal
    ){
        userService.deactivateAccount(getUserId(principal));

    }


    @PatchMapping("/me/reactivate")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void reactivateAccount(
           final Authentication principal
    ){
        userService.reactivateAccount(getUserId(principal));

    }

    @DeleteMapping("/me")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteAccount(
           final Authentication principal
    ){
        userService.deleteAccount(getUserId(principal));
    }


    private String getUserId(
           final Authentication principal) {
        return ((User) principal.getPrincipal()).getId();
    }

}
