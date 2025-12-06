package com.daksh.xeno.backend.shopify;

import org.springframework.stereotype.Service;

import com.daksh.xeno.backend.tenant.Tenant;

@Service
public class ShopifyClient {

    // For now we just return the base URL as a String.
    // Later you can add RestTemplate / WebClient and real API calls.
    public String getBaseUrl(Tenant tenant) {
        return "https://" + tenant.getShopifyDomain() + "/admin/api/2024-01";
    }
}
