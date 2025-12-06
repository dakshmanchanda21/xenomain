package com.daksh.xeno.backend.metrices;

import java.util.List;

public record MetricsResponse(
        SummaryMetrics summary,
        List<DailyOrderMetric> ordersByDate
) {}

