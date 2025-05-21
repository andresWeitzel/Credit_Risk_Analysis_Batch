package com.example.creditrisk.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class CreditRiskData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String customerId;
    private String customerName;
    private double creditScore;
    private double income;
    private double debtToIncomeRatio;
    private int paymentHistory;
    private String riskCategory;
    private String status;
    private String additionalInfo;
    private String recommendations;
} 