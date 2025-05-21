package com.example.creditrisk.service;

import com.example.creditrisk.model.CreditRiskData;
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
        String riskCategory = determineRiskCategory(finalScore);
        
        // Calcular probabilidad de incumplimiento
        double defaultProbability = calculateDefaultProbability(finalScore);
        
        // Agregar resultados al análisis
        analysis.put("baseScore", baseScore);
        analysis.put("incomeRiskFactor", incomeRiskFactor);
        analysis.put("debtRiskFactor", debtRiskFactor);
        analysis.put("paymentRiskFactor", paymentRiskFactor);
        analysis.put("finalScore", finalScore);
        analysis.put("riskCategory", riskCategory);
        analysis.put("defaultProbability", defaultProbability);
        analysis.put("recommendations", generateRecommendations(analysis));
        
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

    private String determineRiskCategory(double finalScore) {
        if (finalScore >= lowThreshold) {
            return "LOW";
        } else if (finalScore >= mediumThreshold) {
            return "MEDIUM";
        } else if (finalScore >= highThreshold) {
            return "HIGH";
        } else {
            return "VERY_HIGH";
        }
    }

    private double calculateDefaultProbability(double finalScore) {
        // Calcular probabilidad de incumplimiento basada en la puntuación final
        // Usar una función sigmoide para mapear la puntuación a una probabilidad
        return 1.0 / (1.0 + Math.exp((finalScore - 50) / 10));
    }

    private Map<String, String> generateRecommendations(Map<String, Object> analysis) {
        Map<String, String> recommendations = new HashMap<>();
        String riskCategory = (String) analysis.get("riskCategory");
        double defaultProbability = (Double) analysis.get("defaultProbability");
        
        switch (riskCategory) {
            case "LOW":
                recommendations.put("creditLimit", "Aumentar límite de crédito");
                recommendations.put("interestRate", "Ofrecer tasa preferencial");
                recommendations.put("terms", "Términos flexibles");
                break;
            case "MEDIUM":
                recommendations.put("creditLimit", "Mantener límite actual");
                recommendations.put("interestRate", "Tasa estándar");
                recommendations.put("terms", "Términos estándar");
                break;
            case "HIGH":
                recommendations.put("creditLimit", "Reducir límite de crédito");
                recommendations.put("interestRate", "Tasa más alta");
                recommendations.put("terms", "Términos más estrictos");
                break;
            case "VERY_HIGH":
                recommendations.put("creditLimit", "Rechazar solicitud");
                recommendations.put("interestRate", "No aplicable");
                recommendations.put("terms", "No aplicable");
                break;
        }
        
        // Agregar recomendaciones específicas basadas en factores de riesgo
        if ((Double) analysis.get("incomeRiskFactor") > 0.7) {
            recommendations.put("income", "Solicitar comprobante de ingresos adicional");
        }
        if ((Double) analysis.get("debtRiskFactor") > 0.7) {
            recommendations.put("debt", "Recomendar reducción de deuda");
        }
        if ((Double) analysis.get("paymentRiskFactor") > 0.7) {
            recommendations.put("payment", "Sugerir plan de pago estructurado");
        }
        
        return recommendations;
    }
} 