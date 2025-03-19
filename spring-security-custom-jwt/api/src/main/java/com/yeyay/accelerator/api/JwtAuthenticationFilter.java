package com.yeyay.accelerator.api;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String TOKEN_HEADER_NAME = "Authorization";

    @Resource
    private JwtTokenService jwtTokenService;
    @Resource
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            Optional<String> authentication = getAuthentication(request);
            if (authentication.isEmpty()) return;

            String token = authentication.get();
            jwtTokenService.validateToken(token);

            String username = jwtTokenService.getSubjectFromToken(token);
            UserDetails user = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken auth = createAuth(user, request);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (SecurityException | MalformedJwtException | ExpiredJwtException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    private Optional<String> getAuthentication(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(TOKEN_HEADER_NAME))
                .filter(auth -> auth.startsWith(TOKEN_PREFIX))
                .map(auth -> auth.substring(TOKEN_PREFIX.length()));
    }

    private UsernamePasswordAuthenticationToken createAuth(UserDetails user, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authToken;
    }
}
