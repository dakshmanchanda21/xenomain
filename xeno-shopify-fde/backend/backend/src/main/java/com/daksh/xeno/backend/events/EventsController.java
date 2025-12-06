package com.daksh.xeno.backend.events;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
public class EventsController {

    private final ShopEventRepository repo;

    public EventsController(ShopEventRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/funnel")
    public Map<String, Long> funnel(
            @RequestParam Long tenantId,
            @RequestParam String from,
            @RequestParam String to
    ) {
        OffsetDateTime fromDt = OffsetDateTime.parse(from + "T00:00:00Z");
        OffsetDateTime toDt = OffsetDateTime.parse(to + "T23:59:59Z");

        List<Object[]> raw = repo.aggregateByType(tenantId, fromDt, toDt);

        return raw.stream()
                .collect(Collectors.toMap(
                        r -> (String) r[0], // type
                        r -> (Long) r[1]    // count
                ));
    }
}
