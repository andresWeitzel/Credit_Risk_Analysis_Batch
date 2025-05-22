package com.example.creditrisk.service;

import com.example.creditrisk.model.CreditRiskData;
import com.example.creditrisk.enums.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RiskAnalysisService {

    @Value("${app.batch.risk.thresholds.low}")
    private int lowThreshold;

    @Value("${app.batch.risk.thresholds.medium}")
    private int mediumThreshold;

    @Value("${app.batch.risk.thresholds.high}")
    private int highThreshold;

    @Value("${app.batch.risk.weights.credit-score}")
    private double creditScoreWeight;

    @Value("${app.batch.risk.weights.income}")
    private double incomeWeight;

    @Value("${app.batch.risk.weights.debt-ratio}")
    private double debtRatioWeight;

    @Value("${app.batch.risk.weights.payment-history}")
    private double paymentHistoryWeight;

    @Cacheable(value = "riskScores", key = "#creditRiskData.customerId")
    public Map<String, Object> analyzeRisk(CreditRiskData creditRiskData) {
        Map<String, Object> analysis = new HashMap<>();
        
        // Calcular puntuación base
        double baseScore = calculateBaseScore(creditRiskData);
        
        // Calcular factores de riesgo adicionales
        double incomeRiskFactor = calculateIncomeRiskFactor(creditRiskData);
        double debtRiskFactor = calculateDebtRiskFactor(creditRiskData);
        double paymentRiskFactor = calculatePaymentRiskFactor(creditRiskData);
        
        // Calcular puntuación final ponderada
        double finalScore = calculateFinalScore(baseScore, incomeRiskFactor, debtRiskFactor, paymentRiskFactor);
        
        // Determinar categoría de riesgo
        RiskCategory riskCategory = determineRiskCategory(finalScore);
        
        // Calcular probabilidad de incumplimiento
        double defaultProbability = calculateDefaultProbability(finalScore);
        
        // Agregar resultados al análisis
        analysis.put(AnalysisKey.BASE_SCORE.getValue(), baseScore);
        analysis.put(AnalysisKey.INCOME_RISK_FACTOR.getValue(), incomeRiskFactor);
        analysis.put(AnalysisKey.DEBT_RISK_FACTOR.getValue(), debtRiskFactor);
        analysis.put(AnalysisKey.PAYMENT_RISK_FACTOR.getValue(), paymentRiskFactor);
        analysis.put(AnalysisKey.FINAL_SCORE.getValue(), finalScore);
        analysis.put(AnalysisKey.RISK_CATEGORY.getValue(), riskCategory.getValue());
        analysis.put(AnalysisKey.DEFAULT_PROBABILITY.getValue(), defaultProbability);
        analysis.put(AnalysisKey.RECOMMENDATIONS.getValue(), generateRecommendations(analysis));
        
        return analysis;
    }

    private double calculateBaseScore(CreditRiskData data) {
        // Normalizar el puntaje de crédito a una escala de 0-100
        double normalizedCreditScore = (data.getCreditScore() / 850.0) * 100;
        
        // Aplicar peso al puntaje de crédito
        return normalizedCreditScore * creditScoreWeight;
    }

    private double calculateIncomeRiskFactor(CreditRiskData data) {
        // Normalizar el ingreso (asumiendo un máximo de $200,000)
        double normalizedIncome = Math.min(data.getIncome() / 200000.0, 1.0);
        
        // Calcular factor de riesgo basado en el ingreso
        double incomeRisk = 1.0 - normalizedIncome;
        
        return incomeRisk * incomeWeight;
    }

    private double calculateDebtRiskFactor(CreditRiskData data) {
        // Calcular factor de riesgo basado en la relación deuda-ingreso
        double debtRatio = data.getDebtToIncomeRatio();
        
        // Normalizar la relación deuda-ingreso (asumiendo un máximo de 100%)
        double normalizedDebtRatio = Math.min(debtRatio / 100.0, 1.0);
        
        return normalizedDebtRatio * debtRatioWeight;
    }

    private double calculatePaymentRiskFactor(CreditRiskData data) {
        // Normalizar el historial de pagos (asumiendo un máximo de 100%)
        double normalizedPaymentHistory = data.getPaymentHistory() / 100.0;
        
        // Calcular factor de riesgo basado en el historial de pagos
        double paymentRisk = 1.0 - normalizedPaymentHistory;
        
        return paymentRisk * paymentHistoryWeight;
    }

    private double calculateFinalScore(double baseScore, double incomeRisk, double debtRisk, double paymentRisk) {
        // Calcular puntuación final considerando todos los factores
        double riskScore = baseScore - (incomeRisk + debtRisk + paymentRisk) * 100;
        
        // Asegurar que la puntuación esté en el rango 0-100
        return Math.max(0, Math.min(100, riskScore));
    }

    private RiskCategory determineRiskCategory(double finalScore) {
        if (finalScore >= lowThreshold) {
            return RiskCategory.LOW;
        } else if (finalScore >= mediumThreshold) {
            return RiskCategory.MEDIUM;
        } else if (finalScore >= highThreshold) {
            return RiskCategory.HIGH;
        } else {
            return RiskCategory.VERY_HIGH;
        }
    }

    private double calculateDefaultProbability(double finalScore) {
        // Calcular probabilidad de incumplimiento basada en la puntuación final
        // Usar una función sigmoide para mapear la puntuación a una probabilidad
        return 1.0 / (1.0 + Math.exp((finalScore - 50) / 10));
    }

    private Map<String, String> generateRecommendations(Map<String, Object> analysis) {
        Map<String, String> recommendations = new HashMap<>();
        RiskCategory riskCategory = RiskCategory.valueOf((String) analysis.get(AnalysisKey.RISK_CATEGORY.getValue()));
        double defaultProbability = (Double) analysis.get(AnalysisKey.DEFAULT_PROBABILITY.getValue());
        
        switch (riskCategory) {
            case LOW:
                recommendations.put(RecommendationType.CREDIT_LIMIT.getValue(), "Aumentar límite de crédito");
                recommendations.put(RecommendationType.INTEREST_RATE.getValue(), "Ofrecer tasa preferencial");
                recommendations.put(RecommendationType.TERMS.getValue(), "Términos flexibles");
                break;
            case MEDIUM:
                recommendations.put(RecommendationType.CREDIT_LIMIT.getValue(), "Mantener límite actual");
                recommendations.put(RecommendationType.INTEREST_RATE.getValue(), "Tasa estándar");
                recommendations.put(RecommendationType.TERMS.getValue(), "Términos estándar");
                break;
            case HIGH:
                recommendations.put(RecommendationType.CREDIT_LIMIT.getValue(), "Reducir límite de crédito");
                recommendations.put(RecommendationType.INTEREST_RATE.getValue(), "Tasa más alta");
                recommendations.put(RecommendationType.TERMS.getValue(), "Términos más estrictos");
                break;
            case VERY_HIGH:
                recommendations.put(RecommendationType.CREDIT_LIMIT.getValue(), "Rechazar solicitud");
                recommendations.put(RecommendationType.INTEREST_RATE.getValue(), "No aplicable");
                recommendations.put(RecommendationType.TERMS.getValue(), "No aplicable");
                break;
        }
        
        // Agregar recomendaciones específicas basadas en factores de riesgo
        if ((Double) analysis.get(AnalysisKey.INCOME_RISK_FACTOR.getValue()) > 0.7) {
            recommendations.put(RecommendationType.INCOME.getValue(), "Solicitar comprobante de ingresos adicional");
        }
        if ((Double) analysis.get(AnalysisKey.DEBT_RISK_FACTOR.getValue()) > 0.7) {
            recommendations.put(RecommendationType.DEBT.getValue(), "Recomendar reducción de deuda");
        }
        if ((Double) analysis.get(AnalysisKey.PAYMENT_RISK_FACTOR.getValue()) > 0.7) {
            recommendations.put(RecommendationType.PAYMENT.getValue(), "Sugerir plan de pago estructurado");
        }
        
        return recommendations;
    }
} 