package com.abernathyclinic.medilabo_determinRisk.testService;

import com.abernathyclinic.medilabo_determinRisk.model.PatientHistory;
import com.abernathyclinic.medilabo_determinRisk.service.RemoteHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        Mockito.when(restTemplate.getForEntity(
                        "http://localhost:8083/api/history/patient/1", PatientHistory[].class))
                .thenReturn(mockResponse);

        List<String> result = remoteHistoryService.getNoteTextsByPatientId(1);

        assertEquals(3, result.size());
        assertTrue(result.contains("Note 1"));
        assertTrue(result.contains("Note 2"));
        assertTrue(result.contains("Note 3"));
    }

    @Test
    void testGetNoteTextsByPatientId_WhenGetEmptyResponse() {
        ResponseEntity<PatientHistory[]> emptyResponse =
                new ResponseEntity<>(new PatientHistory[0], HttpStatus.OK);

        Mockito.when(restTemplate.getForEntity(
                        "http://medilabo-physiciannotes:8083/patient/2", PatientHistory[].class))
                .thenReturn(emptyResponse);

        List<String> result = remoteHistoryService.getNoteTextsByPatientId(2);

        assertTrue(result.isEmpty());
    }
}