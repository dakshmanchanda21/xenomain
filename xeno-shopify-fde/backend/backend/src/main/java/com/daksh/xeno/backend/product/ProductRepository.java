package com.daksh.xeno.backend.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByTenantId(Long tenantId);
}
