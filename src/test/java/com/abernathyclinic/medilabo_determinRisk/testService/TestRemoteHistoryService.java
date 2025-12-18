package com.abernathyclinic.medilabo_determinRisk.testService;

import com.abernathyclinic.medilabo_determinRisk.model.PatientHistory;
import com.abernathyclinic.medilabo_determinRisk.service.RemoteHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TestRemoteHistoryService {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RemoteHistoryService remoteHistoryService;

    private PatientHistory[] patientHistory;

    @BeforeEach
    void setUp() {
        PatientHistory historyA = new PatientHistory();
        historyA.setPatId(1);
        historyA.setNotes(List.of("Note 1", "Note 2"));

        PatientHistory historyB = new PatientHistory();
        historyB.setPatId(1);
        historyB.setNotes(List.of("Note 3"));

        patientHistory = new PatientHistory[]{historyA, historyB};
    }

    @Test
    void testGetNoteTextsByPatientId_Success() {
        ResponseEntity<PatientHistory[]> mockResponse =
                new ResponseEntity<>(patientHistory, HttpStatus.OK);

        Mockito.when(restTemplate.exchange(
                Mockito.eq("http://medilabo-gateway:8085/api/history/patient/1"),
                Mockito.eq(HttpMethod.GET),
                Mockito.any(HttpEntity.class),
                Mockito.eq(PatientHistory[].class)
        )).thenReturn(mockResponse);

        List<String> result = remoteHistoryService.getNoteTextsByPatientId(1, "test_token");

        assertEquals(3, result.size());
        assertTrue(result.contains("Note 1"));
        assertTrue(result.contains("Note 2"));
        assertTrue(result.contains("Note 3"));
    }

    @Test
    void testGetNoteTextsByPatientId_WhenGetEmptyResponse() {
        ResponseEntity<PatientHistory[]> emptyResponse =
                new ResponseEntity<>(new PatientHistory[0], HttpStatus.OK);

        Mockito.when(restTemplate.exchange(
                Mockito.eq("http://medilabo-demographics:8085/api/history/patient/2"),
                Mockito.eq(HttpMethod.GET),
                Mockito.any(HttpEntity.class),
                Mockito.eq(PatientHistory[].class)
        )).thenReturn(emptyResponse);

        List<String> result = remoteHistoryService.getNoteTextsByPatientId(2, "test_token");

        assertTrue(result.isEmpty());
    }
}