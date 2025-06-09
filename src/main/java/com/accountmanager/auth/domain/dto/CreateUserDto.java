package com.accountmanager.auth.domain.dto;

import com.accountmanager.auth.domain.enums.RoleName;

public record CreateUserDto(
        String email,
        String password,
        RoleName role
) {
}
