package com.abernathyclinic.medilabo_determinRisk.model;

import lombok.Data;

import java.util.List;

@Data
public class PatientHistory {
    private Integer patId;
    private List<String> notes;
}
