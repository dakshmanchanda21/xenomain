package com.daksh.xeno.backend.auth;

public record AuthResponse(
        String token,
        Long tenantId,
        String email
) {}
