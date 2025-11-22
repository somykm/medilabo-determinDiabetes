package com.abernathyclinic.medilabo_determinRisk.service;

import com.abernathyclinic.medilabo_determinRisk.model.PatientHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RemoteHistoryService {
    private final RestTemplate restTemplate;
    private final String historyUrl = "http://localhost:8083/api/history";

    @Autowired
    public RemoteHistoryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<String> getNoteTextsByPatientId(Integer id) {
        try {
            ResponseEntity<PatientHistory[]> response = restTemplate.getForEntity(
                    historyUrl + "/patient/" + id, PatientHistory[].class);
            return Arrays.stream(response.getBody())
                    .flatMap(history -> history.getNotes().stream())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}