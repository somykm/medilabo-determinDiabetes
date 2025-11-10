package com.abernathyclinic.medilabo_determinRisk.service;

import com.abernathyclinic.medilabo_determinRisk.model.Patient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RemotePatientService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String patUrl = "http://localhost:8081/api/patient";

    public Patient getPatientById(Integer id) {
        try {
            ResponseEntity<Patient> response = restTemplate.getForEntity(
                    patUrl + "/" + id, Patient.class);
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

}
