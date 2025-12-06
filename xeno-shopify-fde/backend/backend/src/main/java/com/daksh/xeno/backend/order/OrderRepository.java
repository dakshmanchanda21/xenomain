package com.daksh.xeno.backend.order;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByTenantId(Long tenantId);

    @Query("""
           SELECT o
           FROM Order o
           WHERE o.tenantId = :tenantId
             AND o.orderDate BETWEEN :from AND :to
           """)
    List<Order> findByTenantIdAndDateRange(Long tenantId,
                                           OffsetDateTime from,
                                           OffsetDateTime to);
}
