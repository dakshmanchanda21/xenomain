package com.daksh.xeno.backend.tenant;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
    Optional<Tenant> findByEmail(String email);
}
