package com.accountmanager.auth.infrastructure.entities;

import com.accountmanager.auth.domain.enums.RoleName;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "tb_role", schema = "auth")
@Getter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Enumerated(EnumType.STRING)
    private RoleName name;
}
