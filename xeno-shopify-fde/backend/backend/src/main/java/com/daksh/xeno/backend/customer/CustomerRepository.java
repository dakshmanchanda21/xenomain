package com.daksh.xeno.backend.customer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByTenantId(Long tenantId);
}

