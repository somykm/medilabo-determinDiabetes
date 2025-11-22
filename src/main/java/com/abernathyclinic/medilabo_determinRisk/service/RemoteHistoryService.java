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
    private final String historyUrl = "http://localhost:8085/api/history";

    @Autowired
    public RemoteHistoryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<String> getNoteTextsByPatientId(Integer id) {
        log.info("Fetching patient history notes for patient with id={}", id);
        try {
            ResponseEntity<PatientHistory[]> response = restTemplate.getForEntity(
                    historyUrl + "/patient/" + id, PatientHistory[].class);
            if(response.getBody()== null){
                log.warn("No patient notes found for patientId={}", id);
                return Collections.emptyList();
            }
            List<String> notes= Arrays.stream(response.getBody())
                    .flatMap(history -> history.getNotes().stream())
                    .collect(Collectors.toList());
            log.debug("Fetched {} notes for patientId={}", notes.size(), id);
            return notes;
        } catch (Exception e) {
            log.error("Error fetching for patient with id={}: {}",id, e.getMessage());
            return Collections.emptyList();
        }
    }
}