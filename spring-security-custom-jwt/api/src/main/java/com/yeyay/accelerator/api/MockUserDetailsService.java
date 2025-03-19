package com.yeyay.accelerator.api;

import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class MockUserDetailsService implements UserDetailsService {
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return switch (username) {
            case "customer" -> User.builder()
                    .username("customer")
                    .password("customer")
                    .passwordEncoder(passwordEncoder::encode)
                    .build();
            default -> throw new UsernameNotFoundException("User not found");
        };
    }
}
