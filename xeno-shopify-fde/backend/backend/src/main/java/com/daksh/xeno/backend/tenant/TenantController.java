package com.daksh.xeno.backend.tenant;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tenants")
@CrossOrigin(origins = "*")
public class TenantController {

    private final TenantRepository tenantRepository;

    public TenantController(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public record ConnectShopifyRequest(
            Long tenantId,
            String shopifyDomain,
            String accessToken
    ) {}

    @PostMapping("/connect-shopify")
    public Tenant connectShopify(@RequestBody ConnectShopifyRequest request) {
        Tenant tenant = tenantRepository.findById(request.tenantId())
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        tenant.setShopifyDomain(request.shopifyDomain());
        tenant.setShopifyAccessToken(request.accessToken());
        return tenantRepository.save(tenant);
    }
}

