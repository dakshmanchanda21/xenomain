package com.daksh.xeno.backend.metrices;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/metrics")
@CrossOrigin(origins = "*")
public class MetricsController {

    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping("/dashboard")
    public MetricsResponse dashboard(
            @RequestParam Long tenantId,
            @RequestParam String from,
            @RequestParam String to
    ) {
        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate = LocalDate.parse(to);
        return metricsService.getMetrics(tenantId, fromDate, toDate);
    }
}
