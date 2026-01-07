package com.abernathyclinic.medilabo_determinDiabetes.model;

import lombok.Data;

@Data
public class Diabetes {
    private Integer patId;
    private String expectedRisks;
}