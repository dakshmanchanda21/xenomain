package com.daksh.xeno.backend.metrices;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.daksh.xeno.backend.customer.CustomerRepository;
import com.daksh.xeno.backend.order.Order;
import com.daksh.xeno.backend.order.OrderRepository;

@Service
public class MetricsService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public MetricsService(OrderRepository orderRepository,
                          CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    public MetricsResponse getMetrics(Long tenantId, LocalDate from, LocalDate to) {
        OffsetDateTime fromTs = from.atStartOfDay().atOffset(OffsetDateTime.now().getOffset());
        OffsetDateTime toTs = to.plusDays(1).atStartOfDay().atOffset(OffsetDateTime.now().getOffset());

        List<Order> orders = orderRepository.findByTenantIdAndDateRange(tenantId, fromTs, toTs);

        long totalOrders = orders.size();
        double totalRevenue = orders.stream().mapToDouble(Order::getTotalPrice).sum();
        long totalCustomers = customerRepository.findByTenantId(tenantId).size();

        Map<LocalDate, List<Order>> map = new HashMap<>();
        for (Order o : orders) {
            LocalDate d = o.getOrderDate().toLocalDate();
            map.computeIfAbsent(d, k -> new ArrayList<>()).add(o);
        }

        List<DailyOrderMetric> list = map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new DailyOrderMetric(
                        e.getKey(),
                        e.getValue().size(),
                        e.getValue().stream().mapToDouble(Order::getTotalPrice).sum()
                )).toList();

        return new MetricsResponse(
                new SummaryMetrics(totalCustomers, totalOrders, totalRevenue),
                list
        );
    }
}
