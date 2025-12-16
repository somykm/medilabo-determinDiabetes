package com.abernathyclinic.medilabo_determinRisk.controller;

import com.abernathyclinic.medilabo_determinRisk.service.DiabetesReportService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "http://localhost:8082")
@RestController
@RequestMapping("/api/diabetes")
public class DiabetesSignController {
    private final DiabetesReportService reportingService;

    @Autowired
    public DiabetesSignController(DiabetesReportService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/{patId}")
    public ResponseEntity<String> getDiabetesReport(@PathVariable Integer patId,
                                                    HttpServletRequest request) {
        String token = null;
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("AUTH_TOKEN".equals(c.getName())) {
                    token = c.getValue();
                    break;
                }
            }
        }

        String riskLevel = reportingService.diagnoseRisk(patId, token);
        if ("Patient not found".equals(riskLevel)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found!");
        }
        return ResponseEntity.ok("Diabetes risk level for patient " + patId + ": " + riskLevel);
    }
}