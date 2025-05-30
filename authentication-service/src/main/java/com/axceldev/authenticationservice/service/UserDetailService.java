package com.axceldev.authenticationservice.service;

import com.axceldev.authenticationservice.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserDetailService implements ReactiveUserDetailsService {

    private final IUserRepository userRepository;


    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> User.builder()
                                .username(user.getUsername())
                                .password(user.getPassword())
                                .roles(user.getRole())
                                .build());
    }
}
