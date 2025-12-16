package com.abernathyclinic.medilabo_determinRisk.model;

import lombok.Data;

@Data
public class Diabetes {
    private Integer patId;
    private String expectedRisks;
}