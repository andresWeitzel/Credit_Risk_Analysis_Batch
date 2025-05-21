package com.example.creditrisk.processor;

import com.example.creditrisk.model.CreditRiskData;
import com.example.creditrisk.service.RiskAnalysisService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CreditRiskProcessor implements ItemProcessor<CreditRiskData, CreditRiskData> {

    private final RiskAnalysisService riskAnalysisService;

    public CreditRiskProcessor(RiskAnalysisService riskAnalysisService) {
        this.riskAnalysisService = riskAnalysisService;
    }

    @Override
    public CreditRiskData process(CreditRiskData item) throws Exception {
        // Realizar análisis de riesgo
        Map<String, Object> analysis = riskAnalysisService.analyzeRisk(item);
        
        // Actualizar el objeto con los resultados del análisis
        item.setRiskCategory((String) analysis.get("riskCategory"));
        item.setStatus("ANALYZED");
        
        // Agregar información adicional al objeto
        item.setAdditionalInfo(String.format(
            "Base Score: %.2f, Final Score: %.2f, Default Probability: %.2f%%",
            (Double) analysis.get("baseScore"),
            (Double) analysis.get("finalScore"),
            (Double) analysis.get("defaultProbability") * 100
        ));
        
        // Agregar recomendaciones
        Map<String, String> recommendations = (Map<String, String>) analysis.get("recommendations");
        item.setRecommendations(String.join(", ", recommendations.values()));
        
        return item;
    }
} 