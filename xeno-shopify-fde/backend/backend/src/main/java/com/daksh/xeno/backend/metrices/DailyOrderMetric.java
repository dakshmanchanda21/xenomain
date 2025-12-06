package com.daksh.xeno.backend.metrices;

import java.time.LocalDate;

public record DailyOrderMetric(
        LocalDate date,
        long orderCount,
        double revenue
) {}

