package com.abernathyclinic.medilabo_determinDiabetes.service;

import com.abernathyclinic.medilabo_determinDiabetes.model.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class DiabetesReportService {

    private static final Set<String> TRIGGER_TERMS = Set.of(
            "hemoglobin a1c", "hba1c", "microalbumin", "height", "weight",
            "smoking", "smoker", "abnormal", "cholesterol", "dizziness",
            "relapse", "reaction", "antibody"
    );

    private final RemotePatientService remotePatientService;
    private final RemoteHistoryService remoteHistoryService;

    @Autowired
    public DiabetesReportService(RemotePatientService remotePatientService,
                                 RemoteHistoryService remoteHistoryService) {
        this.remotePatientService = remotePatientService;
        this.remoteHistoryService = remoteHistoryService;
    }

    public String diagnoseRisk(Integer patId) {
        log.info("Diagnosing diabetes risk for patient with ID: {}", patId);

        Patient patient = remotePatientService.getPatientById(patId);
        if (patient == null) {
            log.warn("Patient with Id {} not found", patId);
            return "Patient not found";
        }
        List<String> notes = remoteHistoryService.getNoteTextsByPatientId(patId);
        if (notes == null) notes = List.of();

        int age = calculateAge(patient.getBirthdate());
        int triggerCount = countTriggerTerms(notes);

        String genderRaw = patient.getGender() == null
                ? ""
                : patient.getGender().toString().trim().toLowerCase();

        String gender = patient.getGender() != null &&
                patient.getGender().toString().trim().toLowerCase().startsWith("f")
                ? "female"
                : "male";


        log.info("Patient age={}, gender={}, triggerCount={}", age, gender, triggerCount);

        if (triggerCount == 0) return "None";
        if (isBorderline(age, triggerCount)) return "Borderline";
        if (isInDanger(age, gender, triggerCount)) return "In Danger";
        if (isEarlyOnset(age, gender, triggerCount)) return "Early Onset";
        return "None";
    }

    private int calculateAge(LocalDate birthdate) {
        if (birthdate.isAfter(LocalDate.now())) {
            log.warn("Birthdate {} is in the future", birthdate);
            return 0;
        }
        return Period.between(birthdate, LocalDate.now()).getYears();
    }

    private int countTriggerTerms(List<String> notes) {
        int count = 0;
        for (String note : notes) {
            if (note == null || note.isBlank()) continue;
            String lower = note.toLowerCase();
            for (String term : TRIGGER_TERMS) {
                if (lower.contains(term)) count++;
            }
        }
        return count;
    }

    private boolean isBorderline(int age, int triggerCount) {
        return age > 30 && triggerCount >= 2 && triggerCount <= 5;
    }

    private boolean isInDanger(int age, String gender, int triggerCount) {
        if (age < 30 && gender.equals("male")) return triggerCount == 3 || triggerCount == 4;
        if (age < 30 && gender.equals("female")) return triggerCount == 4 || triggerCount == 5;
        return age > 30 && triggerCount >= 6 && triggerCount <= 7;
    }

    private boolean isEarlyOnset(int age, String gender, int triggerCount) {
        if (age < 30 && gender.equals("male")) return triggerCount >= 5;
        if (age < 30 && gender.equals("female")) return triggerCount >= 6;
        return age > 30 && triggerCount >= 8;
    }
}