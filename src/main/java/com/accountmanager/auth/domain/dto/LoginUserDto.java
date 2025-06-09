package com.accountmanager.auth.domain.dto;

public record LoginUserDto(
        String email,
        String password
) {
}
