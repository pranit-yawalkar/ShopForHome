package com.wipro.shopforhome.controller;

import com.wipro.shopforhome.dto.ResponseDTO;
import com.wipro.shopforhome.dto.user.SigninDTO;
import com.wipro.shopforhome.dto.user.SignupDTO;
import com.wipro.shopforhome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseDTO signup(@RequestBody SignupDTO signupDTO) {
        return this.userService.signup(signupDTO);
    }

    @PostMapping("/signin")
    public ResponseDTO signin(@RequestBody SigninDTO signinDTO) {
        return this.userService.signin(signinDTO);
    }
}
