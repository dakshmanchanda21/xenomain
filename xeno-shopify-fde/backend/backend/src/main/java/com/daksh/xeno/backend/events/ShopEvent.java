package com.daksh.xeno.backend.events;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "shop_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tenantId;
    private Long customerId;

    @Column(length = 50)
    private String type;   // CART_ABANDONED, CHECKOUT_STARTED, CHECKOUT_COMPLETED

    private OffsetDateTime occurredAt;
}
