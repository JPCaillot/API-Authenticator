package com.accountmanager.auth.services;

import com.accountmanager.auth.config.JwtTokenService;
import com.accountmanager.auth.config.SecurityConfiguration;
import com.accountmanager.auth.config.UserDetailsImpl;
import com.accountmanager.auth.domain.dto.CreateUserDto;
import com.accountmanager.auth.domain.dto.LoginUserDto;
import com.accountmanager.auth.domain.dto.RecoverJwtTokenDto;
import com.accountmanager.auth.infrastructure.entities.Role;
import com.accountmanager.auth.infrastructure.entities.User;
import com.accountmanager.auth.infrastructure.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;
    private final SecurityConfiguration securityConfiguration;

    @Override
    public RecoverJwtTokenDto authenticateUser(LoginUserDto loginUserDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.password());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new RecoverJwtTokenDto(jwtTokenService.generateToken(userDetails));
    }

    @Override
    public void createUser(CreateUserDto createUserDto) {
        User newUser = User.builder()
                .email(createUserDto.email())
                .password(createUserDto.password())
                .roles(List.of(Role.builder().name(createUserDto.role()).build()))
                .build();

        userRepository.save(newUser);
    }
}
