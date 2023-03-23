package com.example.recipeservice.service;

import com.example.recipeservice.entity.UserEntity;
import com.example.recipeservice.model.RegisterInfoDto;
import com.example.recipeservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findUserEntityByEmail(email)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user with this email doesn't exist!")
                );
    }

    public ResponseEntity<RegisterInfoDto> createNewUser(RegisterInfoDto registerInfo) {
        if (userRepository.findUserEntityByEmail(registerInfo.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user with this email already exists!");
        } else {
            userRepository.save(
                    UserEntity.builder()
                            .email(registerInfo.getEmail())
                            .encodedPassword(encoder.encode(registerInfo.getPassword()))
                            .build()
            );

            return ResponseEntity.ok(registerInfo);
        }
    }

}


