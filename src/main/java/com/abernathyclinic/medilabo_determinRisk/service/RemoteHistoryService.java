package com.abernathyclinic.medilabo_determinRisk.service;

import com.abernathyclinic.medilabo_determinRisk.model.PatientHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class RemoteHistoryService {
    private final RestTemplate restTemplate;
    private final String historyUrl = "http://localhost:8083/api/history";

    @Autowired
    public RemoteHistoryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<String> getNoteTextsByPatientId(Integer patId, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        if (authToken != null && !authToken.isBlank()) {
            headers.add(HttpHeaders.COOKIE, "AUTH_TOKEN=" + authToken);
        }
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<PatientHistory[]> response = restTemplate.exchange(
                    historyUrl + "/patient/" + patId,
                    HttpMethod.GET,
                    entity,
                    PatientHistory[].class
            );
            return Arrays.stream(response.getBody())
                    .map(PatientHistory::getNotes)
                    .flatMap(List::stream)
                    .toList();
        } catch (Exception e) {
            log.error("Error fetching notes for patient {}: {}", patId, e.getMessage());
            return List.of();
        }
    }
}