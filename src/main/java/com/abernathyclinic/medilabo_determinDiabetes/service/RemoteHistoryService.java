package com.abernathyclinic.medilabo_determinDiabetes.service;

import com.abernathyclinic.medilabo_determinDiabetes.model.PatientHistory;
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
import java.util.Objects;

@Slf4j
@Service
public class RemoteHistoryService {

    private final RestTemplate restTemplate;
    //private final String historyUrl = "http://medilabo-physiciannotes:8083/api/history";
    private static final String HISTORY_URL = "http://medilabo-physiciannotes:8083/api/history";

    @Autowired
    public RemoteHistoryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<String> getNoteTextsByPatientId(Integer patId, String authHeader) {
        HttpHeaders headers = new HttpHeaders();
        if (authHeader != null && !authHeader.isBlank()) {
            headers.set(HttpHeaders.AUTHORIZATION, authHeader);
        }
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<PatientHistory[]> response = restTemplate.exchange(
                    HISTORY_URL + "/patient/" + patId,
                    HttpMethod.GET,
                    entity,
                    PatientHistory[].class
            );

            PatientHistory[] body = response.getBody();
            if (body == null) {
                return List.of();
            }
            return Arrays.stream(body)
                    .map(PatientHistory::getNotes)
                    .filter(Objects::nonNull)
                    .flatMap(List::stream)
                    .toList();

        } catch (Exception e) {
            log.error("Error fetching notes for patient {}: {}", patId, e.getMessage());
            return List.of();
        }
    }
}