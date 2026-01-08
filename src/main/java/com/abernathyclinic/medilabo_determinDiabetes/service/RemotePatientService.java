package com.abernathyclinic.medilabo_determinDiabetes.service;

import com.abernathyclinic.medilabo_determinDiabetes.model.Patient;
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
    private static final String PAT_URL = "http://localhost:8081/api/patient";

    @Autowired
    public RemotePatientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Patient getPatientById(Integer id) {
        log.info("Fetching patient with id={} via gateway", id);

        try {
            ResponseEntity<Patient> response = restTemplate.getForEntity(
                    PAT_URL + "/" + id,
                    Patient.class
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("Error fetching patient with id={}: {}", id, e.getMessage());
            return null;
        }
    }
}