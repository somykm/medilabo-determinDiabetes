package com.abernathyclinic.medilabo_determinRisk.testService;

import com.abernathyclinic.medilabo_determinRisk.model.Patient;
import com.abernathyclinic.medilabo_determinRisk.service.RemotePatientService;
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
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class TestRemotePatientService {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RemotePatientService remotePatientService;

    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1);
        patient.setLastName("Fox");
        patient.setFirstName("Emily");
        patient.setBirthdate(LocalDate.of(1998, 5, 11));
        patient.setGender('F');
        patient.setAddress("155 Rosemary St");
        patient.setPhone("202-555-8787");
    }

    @Test
    void testGetPatientById_WhenSucceed() {
        ResponseEntity<Patient> mockResponse = new ResponseEntity<>(patient, HttpStatus.OK);

        // Stub exchange instead of getForEntity
        Mockito.when(restTemplate.exchange(
                Mockito.eq("http://medilabo-gateway:8085/api/patient/1"),
                Mockito.eq(HttpMethod.GET),
                Mockito.any(HttpEntity.class),
                Mockito.eq(Patient.class)
        )).thenReturn(mockResponse);

        Patient result = remotePatientService.getPatientById(1, "test_token");

        assertNotNull(result);
        assertEquals("Fox", result.getLastName());
        assertEquals("Emily", result.getFirstName());
    }

    @Test
    void testGetPatientById_WhenException() {
        Mockito.when(restTemplate.exchange(
                Mockito.eq("http://medilabo-demographics:8085/api/patient/2"),
                Mockito.eq(HttpMethod.GET),
                Mockito.any(HttpEntity.class),
                Mockito.eq(Patient.class)
        )).thenThrow(new RuntimeException("Service Unavailable"));

        Patient result = remotePatientService.getPatientById(2, "test_token");

        assertNull(result);
    }
}