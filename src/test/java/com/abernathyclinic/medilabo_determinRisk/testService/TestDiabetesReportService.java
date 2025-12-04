package com.abernathyclinic.medilabo_determinRisk.testService;

import com.abernathyclinic.medilabo_determinRisk.model.Patient;
import com.abernathyclinic.medilabo_determinRisk.service.DiabetesReportService;
import com.abernathyclinic.medilabo_determinRisk.service.RemoteHistoryService;
import com.abernathyclinic.medilabo_determinRisk.service.RemotePatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TestDiabetesReportService {
    @Mock
    private RemotePatientService patientService;

    @Mock
    private RemoteHistoryService historyService;

    @InjectMocks
    private DiabetesReportService diabetesReportService;
    private Patient patientOver30;
    private Patient patientUnder30;

    @BeforeEach
    void setUp() {
        // Patient over 30
        patientOver30 = new Patient();
        patientOver30.setId(1);
        patientOver30.setFirstName("John");
        patientOver30.setLastName("Doe");
        patientOver30.setBirthdate(LocalDate.of(1980, 12, 13));
        patientOver30.setGender('M');

        // Patient under 30
        patientUnder30 = new Patient();
        patientUnder30.setId(2);
        patientUnder30.setFirstName("James");
        patientUnder30.setLastName("Wilson");
        patientUnder30.setBirthdate(LocalDate.of(1999, 7, 5));
        patientUnder30.setGender('M');
    }

    @Test
    void testDiagnoseRisk_Borderline() {
        Mockito.when(patientService.getPatientById(1)).thenReturn(patientOver30);
        Mockito.when(historyService.getNoteTextsByPatientId(1)).thenReturn(List.of("Patient with high cholesterol", "Over weight", "High blood pressure"));
        String result = diabetesReportService.diagnoseRisk(1);
        assertEquals("_Borderline", result);
    }

    @Test
    void testDiagnoseRisk_PatientNotFound() {
        Mockito.when(patientService.getPatientById(22)).thenReturn(null);
        String result = diabetesReportService.diagnoseRisk(22);
        assertEquals("Patient not found", result);
    }

    @Test
    void testDiagnoseRisk_None() {
        // Mock patient lookup
        Mockito.when(patientService.getPatientById(2)).thenReturn(patientUnder30);
        // Mock history notes with no trigger terms
        Mockito.when(historyService.getNoteTextsByPatientId(2))
                .thenReturn(List.of("No issues reported"));

        String result = diabetesReportService.diagnoseRisk(2);

        assertEquals("_None", result);
    }
}