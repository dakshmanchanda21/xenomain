package com.daksh.xeno.backend.auth;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.daksh.xeno.backend.security.JwtService;
import com.daksh.xeno.backend.tenant.Tenant;
import com.daksh.xeno.backend.tenant.TenantRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final TenantRepository tenantRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthController(TenantRepository tenantRepository, JwtService jwtService) {
        this.tenantRepository = tenantRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
    if (tenantRepository.findByEmail(request.email()).isPresent()) {
        throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Email already registered"
        );
    }

    Tenant tenant = Tenant.builder()
            .name(request.name())
            .email(request.email())
            .passwordHash(encoder.encode(request.password()))
            .build();

    tenant = tenantRepository.save(tenant);

    String token = jwtService.generateToken(tenant.getId(), tenant.getEmail());
    return new AuthResponse(token, tenant.getId(), tenant.getEmail());
}


    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        Tenant tenant = tenantRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!encoder.matches(request.password(), tenant.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(tenant.getId(), tenant.getEmail());
        return new AuthResponse(token, tenant.getId(), tenant.getEmail());
    }
}
