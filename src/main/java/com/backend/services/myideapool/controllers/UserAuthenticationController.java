package com.backend.services.myideapool.controllers;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.backend.services.myideapool.entities.User;
import com.backend.services.myideapool.exceptions.InvalidRefreshTokenException;
import com.backend.services.myideapool.repositories.UserRepository;
import com.backend.services.myideapool.request.models.RefreshJWTRequest;
import com.backend.services.myideapool.request.models.UserLoginRequest;
import com.backend.services.myideapool.request.models.UserLogoutRequest;
import com.backend.services.myideapool.response.models.RefreshJWTResponse;
import com.backend.services.myideapool.response.models.UserLoginResponse;
import com.backend.services.myideapool.utils.CustomUserDetails;
import com.backend.services.myideapool.utils.JwtUtil;
import com.backend.services.myideapool.utils.MyUserDetailsService;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(maxAge = 3600, origins = "*")
public class UserAuthenticationController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/access-tokens/refresh", method = RequestMethod.POST)
    public ResponseEntity<RefreshJWTResponse> refreshJWT(@RequestBody @Valid RefreshJWTRequest request) {

        User user = userRepository.findUserByRefreshToken(request.getRefresh_token());

        if (user == null) {
            throw new InvalidRefreshTokenException();
        }

        return new ResponseEntity<>(
                RefreshJWTResponse.builder()
                        .jwt(jwtUtil.generateToken(new CustomUserDetails(user)))
                        .build(),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/access-tokens", method = RequestMethod.POST)
    public ResponseEntity<UserLoginResponse> userLogin(@RequestBody @Valid UserLoginRequest request) {

        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        final CustomUserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

		if (userDetails.getUser().getRefresh_token() == null) {
			userDetails.getUser().setRefresh_token(UUID.randomUUID().toString());
			userRepository.save(userDetails.getUser());
		}

		final String jwt = jwtUtil.generateToken(userDetails);

        return new ResponseEntity<>(UserLoginResponse.builder()
                .jwt(jwt)
                .refresh_token(userDetails.getUser()
                        .getRefresh_token())
                .build(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/access-tokens", method = RequestMethod.DELETE)
    public ResponseEntity<Void> userLogout(@RequestHeader(value = "X-Access-Token", required = false) String token,
                                           @RequestBody @Valid UserLogoutRequest request) {

        User user = userRepository.findById(jwtUtil.extractUserId(token))
                .get();

        if (!user.getRefresh_token()
                .equals(request.getRefresh_token())) {
            throw new InvalidRefreshTokenException();
        }

        user.setRefresh_token(null);
        userRepository.save(user);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}