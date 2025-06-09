package com.accountmanager.auth.services;

import com.accountmanager.auth.domain.dto.CreateUserDto;
import com.accountmanager.auth.domain.dto.LoginUserDto;
import com.accountmanager.auth.domain.dto.RecoverJwtTokenDto;

public interface IUserService {
    RecoverJwtTokenDto authenticateUser(LoginUserDto loginUserDto);

    void createUser(CreateUserDto createUserDto);
}
