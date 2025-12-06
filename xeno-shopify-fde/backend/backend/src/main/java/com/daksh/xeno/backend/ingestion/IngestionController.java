package com.daksh.xeno.backend.ingestion;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ingest")
public class IngestionController {

    private final IngestionService ingestionService;

    public IngestionController(IngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    public record IngestRequest(Long tenantId) {}

    @PostMapping("/run")
    public ResponseEntity<Void> run(@RequestBody IngestRequest request) {
        ingestionService.ingestForTenant(request.tenantId());
        return ResponseEntity.accepted().build();
    }
}
