package com.daksh.xeno.backend.metrices;

public record SummaryMetrics(
        long totalCustomers,
        long totalOrders,
        double totalRevenue
) {}

