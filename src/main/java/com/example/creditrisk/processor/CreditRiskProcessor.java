package com.example.creditrisk.processor;

import com.example.creditrisk.model.CreditRiskData;
import com.example.creditrisk.enums.*;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Component
public class CreditRiskProcessor implements ItemProcessor<CreditRiskData, CreditRiskData> {

    @Override
    public CreditRiskData process(CreditRiskData item) throws Exception {
        // Calculate age if birthDate is available
        if (item.getBirthDate() != null) {
            int age = Period.between(item.getBirthDate(), LocalDate.now()).getYears();
            item.setAge(age);
        }

        // Generate recommendations based on risk factors
        List<String> recommendations = new ArrayList<>();
        
        // Credit Score Analysis
        if (item.getCreditScore() < 580) {
            recommendations.add("Improve credit score before applying for new loans");
            recommendations.add("Consider credit counseling services");
        } else if (item.getCreditScore() < 670) {
            recommendations.add("Work on improving credit score to get better interest rates");
        }

        // Debt-to-Income Ratio Analysis
        if (item.getDebtToIncomeRatio() > 0.43) {
            recommendations.add("Reduce existing debt before taking on new loans");
            recommendations.add("Consider debt consolidation options");
        }

        // Employment Stability
        if (item.getEmploymentYears() < 2) {
            recommendations.add("Wait until employment history is more established");
        }

        // Loan Purpose Analysis
        switch (item.getLoanPurpose()) {
            case MORTGAGE:
                if (item.getPropertyValue() < item.getLoanAmount()) {
                    recommendations.add("Consider a smaller loan amount or higher down payment");
                }
                break;
            case CAR:
                if (item.getLoanAmount() > 50000) {
                    recommendations.add("Consider a more affordable vehicle");
                }
                break;
            case BUSINESS_EXPANSION:
                if (item.getIncome() < 100000) {
                    recommendations.add("Ensure sufficient cash flow for business expansion");
                }
                break;
            case STARTUP:
                recommendations.add("Prepare detailed business plan and financial projections");
                break;
            default:
                break;
        }

        // Collateral Analysis
        if (item.getCollateralType() != CollateralType.NONE) {
            if (item.getCollateralValue() < item.getLoanAmount() * 0.8) {
                recommendations.add("Consider additional collateral or reduce loan amount");
            }
        }

        // Guarantor Analysis
        if (item.getGuarantorStatus() == GuarantorStatus.REQUIRED) {
            recommendations.add("Ensure guarantor meets all credit and income requirements");
        }

        // Payment History
        if (item.getPaymentHistory() < 90) {
            recommendations.add("Improve payment history before applying for new loans");
        }

        // Credit Card Utilization
        if (item.getCreditCardUtilization() > 0.3) {
            recommendations.add("Reduce credit card balances to improve credit score");
        }

        // Bankruptcy History
        if (item.getHasBankruptcy()) {
            if (item.getBankruptcyYearsAgo() < 7) {
                recommendations.add("Wait until bankruptcy is older than 7 years");
            } else {
                recommendations.add("Consider secured credit options to rebuild credit");
            }
        }

        // Foreclosure History
        if (item.getHasForeclosure()) {
            if (item.getForeclosureYearsAgo() < 3) {
                recommendations.add("Wait until foreclosure is older than 3 years");
            }
        }

        // Education Level Impact
        switch (item.getEducationLevel()) {
            case HIGH_SCHOOL:
                if (item.getIncome() < 50000) {
                    recommendations.add("Consider additional education or training to increase income potential");
                }
                break;
            case BACHELORS:
                if (item.getIncome() < 75000) {
                    recommendations.add("Consider career advancement opportunities");
                }
                break;
            case MASTERS:
            case MBA:
            case PHD:
                if (item.getIncome() < 100000) {
                    recommendations.add("Explore higher-paying positions in your field");
                }
                break;
            default:
                break;
        }

        // Employment Type Analysis
        switch (item.getEmploymentType()) {
            case FULL_TIME:
                if (item.getIncome() < 50000) {
                    recommendations.add("Consider career advancement or additional income sources");
                }
                break;
            case PART_TIME:
                recommendations.add("Consider transitioning to full-time employment for better loan terms");
                break;
            case SELF_EMPLOYED:
                recommendations.add("Maintain detailed financial records and consider business incorporation");
                break;
            case CONTRACT:
                recommendations.add("Ensure stable contract renewals and maintain emergency fund");
                break;
            default:
                break;
        }

        // Marital Status Impact
        switch (item.getMaritalStatus()) {
            case MARRIED:
                if (item.getIncome() < 75000) {
                    recommendations.add("Consider joint application with spouse if they have good credit");
                }
                break;
            case SINGLE:
                if (item.getIncome() < 50000) {
                    recommendations.add("Consider co-signer or additional income sources");
                }
                break;
            default:
                break;
        }

        // Residence Type Analysis
        switch (item.getResidenceType()) {
            case RENT:
                if (item.getYearsAtCurrentAddress() < 2) {
                    recommendations.add("Establish longer residence history before applying");
                }
                break;
            case MORTGAGE:
                if (item.getPropertyValue() < item.getLoanAmount()) {
                    recommendations.add("Consider home equity loan instead of new mortgage");
                }
                break;
            default:
                break;
        }

        // Loan Term Analysis
        switch (item.getLoanTerm()) {
            case SHORT_TERM:
                if (item.getMonthlyExpenses() > item.getIncome() * 0.4) {
                    recommendations.add("Ensure sufficient monthly income for short-term payments");
                }
                break;
            case LONG_TERM:
                if (item.getInterestRate() > 5.0) {
                    recommendations.add("Consider refinancing when interest rates improve");
                }
                break;
            default:
                break;
        }

        // Set recommendations
        item.setRecommendations(String.join("; ", recommendations));
        
        return item;
    }
} 