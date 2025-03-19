package com.yeyay.accelerator.api;

import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private JwtTokenService jwtTokenService;

    @GetMapping()
    public String mainEndpoint() {
        return "Hello, everyone";
    }

    @GetMapping("/customer")
    public String customerEndpoint() {
        return "Hello, Customer";
    }

    @GetMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginDto login) {
        UserDetails user = userDetailsService.loadUserByUsername(login.getUsername());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                login.getUsername(), login.getPassword(), user.getAuthorities()));
        String token = jwtTokenService.generateToken(user.getUsername());
        return ResponseEntity.ok(token);
    }

    public static class LoginDto {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
