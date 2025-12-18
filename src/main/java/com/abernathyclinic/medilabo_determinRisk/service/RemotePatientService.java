package com.abernathyclinic.medilabo_determinRisk.service;

import com.abernathyclinic.medilabo_determinRisk.model.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class RemotePatientService {
    private final RestTemplate restTemplate;
    private final String patUrl = "http://localhost:8081/api/patient";

    @Autowired
    public RemotePatientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Patient getPatientById(Integer id, String authToken) {
        log.info("Fetching patient with id={} via gateway", id);

        HttpHeaders headers = new HttpHeaders();
        if (authToken != null && !authToken.isBlank()) {
            headers.add(HttpHeaders.COOKIE, "AUTH_TOKEN=" + authToken);
        }
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Patient> response = restTemplate.exchange(
                    patUrl + "/" + id,
                    HttpMethod.GET,
                    entity,
                    Patient.class
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("Error fetching patient with id={}: {}", id, e.getMessage());
            return null;
        }
    }
}