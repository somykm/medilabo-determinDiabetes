//package com.abernathyclinic.medilabo_determinRisk.testController;
//
//import com.abernathyclinic.medilabo_determinRisk.controller.DiabetesSignController;
//import com.abernathyclinic.medilabo_determinRisk.model.Diabetes;
//import com.abernathyclinic.medilabo_determinRisk.model.Patient;
//import com.abernathyclinic.medilabo_determinRisk.model.PatientHistory;
//import com.abernathyclinic.medilabo_determinRisk.service.DiabetesReportService;
//import com.abernathyclinic.medilabo_determinRisk.service.RemoteHistoryService;
//import com.abernathyclinic.medilabo_determinRisk.service.RemotePatientService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.http.Cookie;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//
//
//import java.time.LocalDate;
//import java.util.List;
//
//@WebMvcTest(controllers = DiabetesSignController.class)
//@AutoConfigureMockMvc(addFilters = false)
//class TestDiabetesSignController {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private DiabetesReportService diabetesReportService;
//
//    @MockitoBean
//    private RemoteHistoryService remoteHistoryService;
//
//    @MockitoBean
//    private RemotePatientService remotePatientService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private Diabetes sampleDiabetes;
//    private Patient samplePatient;
//    private PatientHistory sampleHistory;
//
//    @BeforeEach
//    void setUp() {
//        sampleDiabetes = new Diabetes();
//        sampleDiabetes.setPatId(1);
//        sampleDiabetes.setExpectedRisks("Borderline");
//
//        samplePatient = new Patient();
//        samplePatient.setId(1);
//        samplePatient.setLastName("Jackson");
//        samplePatient.setFirstName("Sean");
//        samplePatient.setBirthdate(LocalDate.of(1990, 2, 20));
//        samplePatient.setGender('M');
//        samplePatient.setAddress("125 Philadelphia Ave");
//        samplePatient.setPhone("666-777-8888");
//
//        sampleHistory = new PatientHistory();
//        sampleHistory.setPatId(2);
//        sampleHistory.setNotes(List.of("Patient has high blood pressure!", "Patient vitamin D is low."));
//    }
//
//    @Test
//    void testGetDiabetesReport_WhenPatientIsValid() throws Exception {
//        Mockito.when(diabetesReportService.diagnoseRisk(1, "test-token"))
//                .thenReturn("Borderline");
//
//        mockMvc.perform(get("/api/diabetes/1")
//                        .cookie(new Cookie("AUTH_TOKEN", "test-token")))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Diabetes risk level for patient 1: Borderline"));
//    }
//
//    @Test
//    void testGetDiabetesReport_WhenPatientNotFound() throws Exception {
//        Mockito.when(diabetesReportService.diagnoseRisk(100, "test-token"))
//                .thenReturn("Patient not found!");
//
//        mockMvc.perform(get("/api/diabetes/100")
//                        .cookie(new Cookie("AUTH_TOKEN", "test-token")))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Patient not found!"));
//    }
//}