package com.abernathyclinic.medilabo_determinDiabetes.controller;

import com.abernathyclinic.medilabo_determinDiabetes.service.DiabetesReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/diabetes")
public class DiabetesSignController {

    private final DiabetesReportService reportingService;

    @Autowired
    public DiabetesSignController(DiabetesReportService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/{patId}")
    public ResponseEntity<String> getDiabetesReport(
            @PathVariable Integer patId,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        log.info("Diabetes risk request for patient {}", patId);
        String riskLevel = reportingService.diagnoseRisk(patId, authHeader);

        if ("Patient not found".equals(riskLevel)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found!");
        }

        return ResponseEntity.ok(
                "Diabetes risk level for patient " + patId + ": " + riskLevel);
    }
}