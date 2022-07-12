package com.wipro.shopforhome.service;


import com.wipro.shopforhome.dto.ResponseDTO;
import com.wipro.shopforhome.dto.user.SigninDTO;
import com.wipro.shopforhome.dto.user.SignupDTO;
import com.wipro.shopforhome.exception.AuthenticationFailException;
import com.wipro.shopforhome.exception.CustomException;
import com.wipro.shopforhome.model.AuthenticationToken;
import com.wipro.shopforhome.model.User;
import com.wipro.shopforhome.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private AuthenticationService authenticationService;

    @Transactional
    public ResponseDTO signup(SignupDTO signupDTO) {
        // check if user is already present
        try {
            if(userRepository.findByEmail(signupDTO.getEmail())!=null) {
                throw new CustomException("User already exists!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // hash the password
        String encryptedPassword = this.passwordService.passwordEncoder().encode(signupDTO.getPassword());

        // save the user
        User user = new User();
        user.setFirstName(signupDTO.getFirstName());
        user.setLastName(signupDTO.getLastName());
        user.setEmail(signupDTO.getEmail());
        user.setPassword(encryptedPassword);
        this.userRepository.save(user);

        // create the token
        AuthenticationToken authenticationToken = new AuthenticationToken(user);

        this.authenticationService.saveConfirmationToken(authenticationToken);

        return new ResponseDTO("success", "User signed up successfully!");
    }

    public ResponseDTO signin(SigninDTO signinDTO) {
        // find user by email
        User user = this.userRepository.findByEmail(signinDTO.getEmail());

        if(user==null) {
            throw new AuthenticationFailException("User is not valid");
        }

        // hash the password and compare with the password in DB
        try {
            if(user.getPassword()
                    .equals(this.passwordService.passwordEncoder().encode(signinDTO.getPassword()))) {
                throw new AuthenticationFailException("Invalid Credentials");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // if password matches then retrieve the token
        AuthenticationToken token = authenticationService.getToken(user);

        if(token==null) {
            throw new CustomException("Token is not present");
        }
        return new ResponseDTO("success", token.getToken());
    }

}
