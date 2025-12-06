package com.daksh.xeno.backend.events;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShopEventRepository extends JpaRepository<ShopEvent, Long> {

    @Query("""
        select e.type as type, count(e) as count
        from ShopEvent e
        where e.tenantId = :tenantId
          and e.occurredAt between :from and :to
        group by e.type
        """)
    List<Object[]> aggregateByType(Long tenantId, OffsetDateTime from, OffsetDateTime to);
}
