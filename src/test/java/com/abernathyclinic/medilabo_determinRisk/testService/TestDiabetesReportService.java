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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TestDiabetesReportService {

    @Mock
    private RemotePatientService patientService;

    @Mock
    private RemoteHistoryService historyService;

    @InjectMocks
    private DiabetesReportService diabetesReportService;

    private Patient patientOver30Male;
    private Patient patientOver30Female;
    private Patient patientUnder30Male;
    private Patient patientUnder30Female;

    @BeforeEach
    void setUp() {
        // Patient over 30 male
        patientOver30Male = new Patient();
        patientOver30Male.setId(1);
        patientOver30Male.setFirstName("John");
        patientOver30Male.setLastName("Doe");
        patientOver30Male.setBirthdate(LocalDate.of(1980, 12, 13));
        patientOver30Male.setGender('M');

        // Patient over 30 female
        patientOver30Female = new Patient();
        patientOver30Female.setId(2);
        patientOver30Female.setFirstName("Jane");
        patientOver30Female.setLastName("Smith");
        patientOver30Female.setBirthdate(LocalDate.of(1980, 6, 1));
        patientOver30Female.setGender('F');

        // Patient under 30 male
        patientUnder30Male = new Patient();
        patientUnder30Male.setId(3);
        patientUnder30Male.setFirstName("James");
        patientUnder30Male.setLastName("Wilson");
        patientUnder30Male.setBirthdate(LocalDate.of(1999, 7, 5));
        patientUnder30Male.setGender('M');

        // Patient under 30 female
        patientUnder30Female = new Patient();
        patientUnder30Female.setId(4);
        patientUnder30Female.setFirstName("Emily");
        patientUnder30Female.setLastName("Brown");
        patientUnder30Female.setBirthdate(LocalDate.of(2000, 3, 15));
        patientUnder30Female.setGender('F');
    }

    @Test
    void testDiagnoseRisk_Borderline() {
        Mockito.when(patientService.getPatientById(1, "test_token"))
                .thenReturn(patientOver30Male);
        Mockito.when(historyService.getNoteTextsByPatientId(1, "test_token"))
                .thenReturn(List.of("cholesterol", "weight", "smoking"));

        String result = diabetesReportService.diagnoseRisk(1, "test_token");

        assertEquals("Borderline", result);
    }

    @Test
    void testDiagnoseRisk_PatientNotFound() {
        Mockito.when(patientService.getPatientById(22, "test_token"))
                .thenReturn(null);

        String result = diabetesReportService.diagnoseRisk(22, "test_token");

        assertEquals("Patient not found", result);
    }

    @Test
    void testDiagnoseRisk_None() {
        Mockito.when(patientService.getPatientById(3, "test_token"))
                .thenReturn(patientUnder30Male);
        Mockito.when(historyService.getNoteTextsByPatientId(3, "test_token"))
                .thenReturn(List.of("No issues reported"));

        String result = diabetesReportService.diagnoseRisk(3, "test_token");

        assertEquals("None", result);
    }

    @Test
    void testDiagnoseRisk_InDanger_Under30Male() {
        Mockito.when(patientService.getPatientById(3, "test_token"))
                .thenReturn(patientUnder30Male);
        // 3 trigger terms ;in Danger for male under 30
        Mockito.when(historyService.getNoteTextsByPatientId(3, "test_token"))
                .thenReturn(List.of("cholesterol", "smoking", "weight"));

        String result = diabetesReportService.diagnoseRisk(3, "test_token");

        assertEquals("In Danger", result);
    }

    @Test
    void testDiagnoseRisk_InDanger_Under30Female() {
        Mockito.when(patientService.getPatientById(4, "test_token"))
                .thenReturn(patientUnder30Female);
        // 4 trigger terms ;in Danger for female under 30
        Mockito.when(historyService.getNoteTextsByPatientId(4, "test_token"))
                .thenReturn(List.of("cholesterol", "smoking", "weight", "abnormal"));

        String result = diabetesReportService.diagnoseRisk(4, "test_token");

        assertEquals("In Danger", result);
    }

    @Test
    void testDiagnoseRisk_InDanger_Over30() {
        Mockito.when(patientService.getPatientById(1, "test_token"))
                .thenReturn(patientOver30Male);
        // 6 trigger terms ;in Danger for over 30
        Mockito.when(historyService.getNoteTextsByPatientId(1, "test_token"))
                .thenReturn(List.of("cholesterol", "smoking", "weight", "abnormal", "reaction", "antibody"));

        String result = diabetesReportService.diagnoseRisk(1, "test_token");

        assertEquals("In Danger", result);
    }

    @Test
    void testDiagnoseRisk_EarlyOnset_Under30Male() {
        Mockito.when(patientService.getPatientById(3, "test_token"))
                .thenReturn(patientUnder30Male);
        // 5 trigger terms ;early Onset for male under 30
        Mockito.when(historyService.getNoteTextsByPatientId(3, "test_token"))
                .thenReturn(List.of("cholesterol", "smoking", "weight", "abnormal", "reaction"));

        String result = diabetesReportService.diagnoseRisk(3, "test_token");

        assertEquals("Early Onset", result);
    }

    @Test
    void testDiagnoseRisk_EarlyOnset_Under30Female() {
        Mockito.when(patientService.getPatientById(4, "test_token"))
                .thenReturn(patientUnder30Female);
        // 6 trigger terms;early Onset for female under 30
        Mockito.when(historyService.getNoteTextsByPatientId(4, "test_token"))
                .thenReturn(List.of("cholesterol", "smoking", "weight", "abnormal", "reaction", "antibody"));

        String result = diabetesReportService.diagnoseRisk(4, "test_token");

        assertEquals("Early Onset", result);
    }

    @Test
    void testDiagnoseRisk_EarlyOnset_Over30() {
        Mockito.when(patientService.getPatientById(2, "test_token"))
                .thenReturn(patientOver30Female);
        // 8 trigger terms; Early Onset for over 30
        Mockito.when(historyService.getNoteTextsByPatientId(2, "test_token"))
                .thenReturn(List.of("cholesterol", "smoking", "weight", "abnormal", "reaction", "antibody",
                        "dizziness", "microalbumin"));

        String result = diabetesReportService.diagnoseRisk(2, "test_token");

        assertEquals("Early Onset", result);
    }
}