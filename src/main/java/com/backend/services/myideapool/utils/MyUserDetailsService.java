package com.backend.services.myideapool.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.backend.services.myideapool.entities.User;
import com.backend.services.myideapool.exceptions.UserNotFoundException;
import com.backend.services.myideapool.repositories.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UserNotFoundException(email);
        }
        return new CustomUserDetails(user);
    }
}