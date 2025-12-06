package com.daksh.xeno.backend.ingestion;

import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;

import com.daksh.xeno.backend.customer.Customer;
import com.daksh.xeno.backend.customer.CustomerRepository;
import com.daksh.xeno.backend.order.Order;
import com.daksh.xeno.backend.order.OrderRepository;
import com.daksh.xeno.backend.product.Product;
import com.daksh.xeno.backend.product.ProductRepository;
import com.daksh.xeno.backend.shopify.ShopifyClient;
import com.daksh.xeno.backend.tenant.Tenant;
import com.daksh.xeno.backend.tenant.TenantRepository;

@Service
public class IngestionService {

    private final ShopifyClient shopifyClient;
    private final TenantRepository tenantRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public IngestionService(ShopifyClient shopifyClient,
                            TenantRepository tenantRepository,
                            CustomerRepository customerRepository,
                            OrderRepository orderRepository,
                            ProductRepository productRepository) {
        this.shopifyClient = shopifyClient;
        this.tenantRepository = tenantRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public void ingestForTenant(Long tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        // Just to show something on the dashboard, we create some dummy data.
        // In a real app you would call Shopify APIs here.

        // 1) Create a demo customer
        Customer customer = Customer.builder()
                .tenantId(tenantId)
                .shopifyCustomerId(1001L)
                .firstName("Demo")
                .lastName("Customer")
                .email(tenant.getEmail())
                .phone("+91-9999999999")
                .build();
        customer = customerRepository.save(customer);

        // 2) Create a demo product
        Product product = Product.builder()
                .tenantId(tenantId)
                .shopifyProductId(2001L)
                .title("Demo Product")
                .sku("DEMO-SKU-1")
                .price(499.0)
                .currency("INR")
                .build();
        productRepository.save(product);

        // 3) Create a couple of demo orders on different dates
        OffsetDateTime now = OffsetDateTime.now();

        Order order1 = Order.builder()
                .tenantId(tenantId)
                .shopifyOrderId(3001L)
                .customerId(customer.getId())
                .totalPrice(499.0)
                .currency("INR")
                .orderDate(now.minusDays(3))
                .build();

        Order order2 = Order.builder()
                .tenantId(tenantId)
                .shopifyOrderId(3002L)
                .customerId(customer.getId())
                .totalPrice(899.0)
                .currency("INR")
                .orderDate(now.minusDays(1))
                .build();

        orderRepository.save(order1);
        orderRepository.save(order2);

        String baseUrl = shopifyClient.getBaseUrl(tenant);
        System.out.println("Dummy ingestion complete for tenant " + tenantId +
                " using base URL: " + baseUrl);
    }
}
