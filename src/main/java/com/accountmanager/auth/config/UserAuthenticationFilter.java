package com.accountmanager.auth.config;

import com.accountmanager.auth.exception.AuthenticationApiException;
import com.accountmanager.auth.infrastructure.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (checkIfEndpointIsNotPublic(request)) {
            String token = recoverTokenFromHeaders(request);
            if (Objects.nonNull(token)) {
                String subject = jwtTokenService.getSubjectFromToken(token);
                userRepository.findByEmail(subject)
                        .ifPresentOrElse(user -> {
                            UserDetailsImpl userDetails = new UserDetailsImpl(user);

                            Authentication authentication = new UsernamePasswordAuthenticationToken(
                                    userDetails.getUsername(),
                                    null,
                                    userDetails.getAuthorities()
                            );

                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        },
                                () -> {
                                    throw new AuthenticationApiException(HttpStatus.FORBIDDEN, "User not found");
                                }
                );
            } else {
                throw new AuthenticationApiException(HttpStatus.FORBIDDEN, "Token not found");
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverTokenFromHeaders(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String headerIdentifier = "Bearer ";
        if (Objects.nonNull(authorizationHeader) && authorizationHeader.startsWith(headerIdentifier)) {
            return authorizationHeader.replace(headerIdentifier, "");
        }
        return null;
    }

    private boolean checkIfEndpointIsNotPublic(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return !Arrays.asList(SecurityConfiguration.ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).contains(requestURI);
    }
}
