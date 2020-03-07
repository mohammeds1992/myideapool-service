package com.backend.services.myideapool.controllers;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.backend.services.myideapool.entities.User;
import com.backend.services.myideapool.exceptions.DuplicateEmailFoundException;
import com.backend.services.myideapool.repositories.UserRepository;
import com.backend.services.myideapool.request.models.UserSignupRequest;
import com.backend.services.myideapool.response.models.UserLoginResponse;
import com.backend.services.myideapool.utils.CustomUserDetails;
import com.backend.services.myideapool.utils.JwtUtil;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(maxAge = 3600, origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<UserLoginResponse> createUser(@RequestBody @Valid UserSignupRequest request) {


        User userByEmail = userRepository.findUserByEmail(request.getEmail());

        if (userByEmail != null) {
            throw new DuplicateEmailFoundException(request.getEmail());
        }

        User user = new User();

        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setCreated_at(System.currentTimeMillis());
        user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
        user.setRefresh_token(UUID.randomUUID()
                .toString());

        userRepository.save(user);

        return new ResponseEntity<>(UserLoginResponse.builder()
                .jwt(jwtUtil.generateToken(new CustomUserDetails(user)))
                .refresh_token(user.getRefresh_token())
                .build(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public ResponseEntity<User> getCurrentUser(
            @RequestHeader(value = "X-Access-Token", required = false) String token) {
        User user = userRepository.findById(jwtUtil.extractUserId(token))
                .get();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}