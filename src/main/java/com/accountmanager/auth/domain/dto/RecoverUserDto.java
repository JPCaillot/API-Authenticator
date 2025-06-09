package com.accountmanager.auth.domain.dto;

import com.accountmanager.auth.infrastructure.entities.Role;

import java.util.List;

public record RecoverUserDto(
        Long id,
        String email,
        List<Role> roles
) {
}
