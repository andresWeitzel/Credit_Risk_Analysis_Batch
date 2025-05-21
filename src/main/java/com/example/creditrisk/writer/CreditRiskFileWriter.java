package com.example.creditrisk.writer;

import com.example.creditrisk.model.CreditRiskData;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class CreditRiskFileWriter extends FlatFileItemWriter<CreditRiskData> {

    public CreditRiskFileWriter() {
        setupWriter();
    }

    private void setupWriter() {
        try {
            // Obtener la ruta del directorio resources
            String projectDir = System.getProperty("user.dir");
            Path resourcesPath = Paths.get(projectDir, "src", "main", "resources", "output");
            
            // Crear el directorio output si no existe
            if (!Files.exists(resourcesPath)) {
                Files.createDirectories(resourcesPath);
                System.out.println("Directorio creado: " + resourcesPath);
            }

            // Configurar el archivo de salida
            File outputFile = resourcesPath.resolve("credit-risk-results.csv").toFile();
            if (outputFile.exists()) {
                outputFile.delete();
                System.out.println("Archivo existente eliminado: " + outputFile.getAbsolutePath());
            }
            
            System.out.println("Archivo de salida configurado en: " + outputFile.getAbsolutePath());
            setResource(new FileSystemResource(outputFile));
            setAppendAllowed(false);

            // Configurar el formato del archivo
            DelimitedLineAggregator<CreditRiskData> lineAggregator = new DelimitedLineAggregator<>();
            lineAggregator.setDelimiter(",");

            BeanWrapperFieldExtractor<CreditRiskData> fieldExtractor = new BeanWrapperFieldExtractor<>();
            fieldExtractor.setNames(new String[]{"customerId", "customerName", "creditScore", "income", "debtToIncomeRatio", "paymentHistory", "riskCategory"});
            lineAggregator.setFieldExtractor(fieldExtractor);

            setLineAggregator(lineAggregator);
            setHeaderCallback(writer -> writer.write("customerId,customerName,creditScore,income,debtToIncomeRatio,paymentHistory,riskCategory"));
            setFooterCallback(writer -> writer.write("Pie de archivo"));
            afterPropertiesSet();
            
            System.out.println("Recurso configurado para: " + outputFile.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException("Error configurando el writer: " + e.getMessage(), e);
        }
    }
} 