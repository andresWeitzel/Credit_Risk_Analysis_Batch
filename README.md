<div align = "center">
  <img src="./src/main/resources/static/img/credit_risk.png" >
</div>

# Credit Risk Analysis Batch Application

Esta aplicación Spring Batch está diseñada para analizar y evaluar el riesgo crediticio de clientes utilizando un sistema sofisticado de puntuación multifactorial. El sistema procesa datos de clientes desde un archivo CSV, realiza un análisis de riesgo detallado y genera resultados tanto en una base de datos H2 como en un archivo CSV de salida.

## Características Principales

### 1. Análisis de Riesgo Multifactorial
- **Puntuación Base de Crédito**: Normalización y ponderación del puntaje de crédito
- **Factores de Riesgo**:
  - Factor de Ingresos
  - Factor de Ratio Deuda-Ingreso
  - Factor de Historial de Pagos
- **Cálculo de Probabilidad de Incumplimiento**: Utilizando función sigmoide
- **Categorización de Riesgo**: LOW, MEDIUM, HIGH, VERY_HIGH

### 2. Sistema de Recomendaciones
- Límites de crédito sugeridos
- Tasas de interés recomendadas
- Términos de crédito
- Recomendaciones específicas por factor de riesgo

### 3. Procesamiento por Lotes
- Lectura de datos desde CSV
- Procesamiento en chunks configurables
- Escritura en base de datos y archivo CSV
- Manejo de errores y reintentos

### 4. Monitoreo y Métricas
- Endpoints de Actuator para monitoreo
- Integración con Prometheus
- Logging detallado
- Métricas de rendimiento

## Requisitos Técnicos

- Java 17 o superior
- Maven 3.6 o superior
- Spring Boot 3.x
- Spring Batch 5.x
- H2 Database
- Caffeine Cache

## Estructura del Proyecto

```
src/main/java/com/example/creditrisk/
├── config/
│   └── BatchConfig.java         # Configuración del batch job
├── model/
│   └── CreditRiskData.java      # Modelo de datos
├── processor/
│   └── CreditRiskProcessor.java # Procesador de riesgo
├── reader/
│   └── CreditRiskItemReader.java # Lector de datos
├── service/
│   └── RiskAnalysisService.java # Servicio de análisis de riesgo
├── writer/
│   └── CreditRiskFileWriter.java # Escritor de resultados
└── CreditRiskBatchApplication.java
```

## Configuración

### Archivo application.yml
```yaml
spring:
  batch:
    job:
      enabled: true
    chunk-size: 100
    max-threads: 4
    retry-policy:
      max-attempts: 3
      backoff:
        initial-interval: 1000
        multiplier: 2.0

  datasource:
    url: jdbc:h2:mem:creditriskdb
    driver-class-name: org.h2.Driver
    username: sa
    password: 

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        batch_size: 100
        order_inserts: true
        order_updates: true

app:
  batch:
    risk:
      thresholds:
        low: 80
        medium: 60
        high: 40
      weights:
        credit-score: 0.4
        income: 0.3
        debt-ratio: 0.2
        payment-history: 0.1
```

## Ejecución

1. Clonar el repositorio:
```bash
git clone [url-del-repositorio]
cd credit-risk-batch
```

2. Compilar el proyecto:
```bash
mvn clean install
```

3. Ejecutar la aplicación:
```bash
mvn spring-boot:run
```

## Formato de Datos de Entrada

El archivo de entrada debe estar en formato CSV con las siguientes columnas:
- customerId: Identificador único del cliente
- customerName: Nombre del cliente
- creditScore: Puntaje de crédito (0-850)
- income: Ingresos anuales
- debtToIncomeRatio: Ratio deuda-ingreso (%)
- paymentHistory: Historial de pagos (0-100)

### Ejemplo de Archivo de Entrada (credit-risk-data.csv)
```csv
customerId,customerName,creditScore,income,debtToIncomeRatio,paymentHistory,employmentYears,loanAmount,loanPurpose,existingLoans,propertyValue,maritalStatus,educationLevel,industry,riskCategory,status,additionalInfo,recommendations,birthDate,age,employmentType,monthlyExpenses,savingsBalance,creditHistoryYears,numberOfCreditCards,creditCardUtilization,hasBankruptcy,bankruptcyYearsAgo,hasForeclosure,foreclosureYearsAgo,residenceType,yearsAtCurrentAddress,loanTerm,interestRate,collateralType,collateralValue,guarantorStatus,guarantorCreditScore,guarantorIncome,guarantorRelationship
CUST001,John Doe,750,120000,0.35,95,5,50000,MORTGAGE,1,250000,MARRIED,BACHELORS,TECHNOLOGY,LOW,ACTIVE,Good payment history,Consider increasing credit limit,1980-01-15,43,FULL_TIME,3000,50000,15,2,0.25,false,0,false,0,OWN,10,LONG_TERM,3.5,REAL_ESTATE,250000,NONE,0,0,NA
CUST002,Jane Smith,680,85000,0.45,85,3,30000,CAR,2,150000,SINGLE,MASTERS,FINANCE,MEDIUM,ACTIVE,Recent job change,Monitor payment history,1985-06-20,38,FULL_TIME,2500,30000,10,3,0.40,false,0,false,0,RENT,3,MEDIUM_TERM,4.5,VEHICLE,35000,NONE,0,0,NA
CUST003,Bob Johnson,580,45000,0.65,70,1,15000,PERSONAL,3,0,DIVORCED,HIGH_SCHOOL,RETAIL,HIGH,ACTIVE,Past late payments,High risk profile,1990-03-10,33,PART_TIME,2000,5000,5,4,0.75,false,0,false,0,RENT,1,SHORT_TERM,7.5,NONE,0,REQUIRED,650,40000,FAMILY
CUST004,Maria Garcia,820,180000,0.25,98,8,75000,BUSINESS,0,500000,MARRIED,DOCTORATE,HEALTHCARE,LOW,ACTIVE,Excellent credit history,Prime customer,1975-05-30,48,FULL_TIME,4500,200000,20,1,0.10,false,0,false,0,OWN,15,LONG_TERM,3.0,BUSINESS,500000,NONE,0,0,NA
CUST005,David Chen,720,95000,0.40,88,4,40000,EDUCATION,1,0,SINGLE,MASTERS,EDUCATION,MEDIUM,ACTIVE,Stable employment,Good payment record,1988-11-12,35,FULL_TIME,2800,25000,8,2,0.35,false,0,false,0,RENT,4,MEDIUM_TERM,4.0,NONE,0,NONE,0,0,NA
CUST006,Sarah Williams,650,78000,0.50,82,2,25000,CONSOLIDATION,4,120000,DIVORCED,BACHELORS,SERVICES,MEDIUM,ACTIVE,Multiple loans,High debt load,1992-03-25,31,FULL_TIME,2200,15000,6,3,0.60,false,0,false,0,RENT,2,MEDIUM_TERM,5.0,VEHICLE,25000,NONE,0,0,NA
CUST007,Michael Brown,550,42000,0.70,65,1,10000,PERSONAL,2,0,SINGLE,HIGH_SCHOOL,CONSTRUCTION,HIGH,ACTIVE,Recent credit issues,High risk,1995-07-18,28,PART_TIME,1800,3000,3,2,0.80,false,0,false,0,RENT,1,SHORT_TERM,8.0,NONE,0,REQUIRED,600,35000,FRIEND
CUST008,Emily Davis,780,150000,0.30,92,6,60000,INVESTMENT,1,300000,MARRIED,MASTERS,LEGAL,LOW,ACTIVE,Strong financial profile,Preferred customer,1982-09-05,41,FULL_TIME,4000,100000,12,2,0.20,false,0,false,0,OWN,8,LONG_TERM,3.2,REAL_ESTATE,300000,NONE,0,0,NA
CUST009,James Wilson,600,48000,0.60,75,2,20000,CONSOLIDATION,3,0,SINGLE,BACHELORS,RETAIL,HIGH,ACTIVE,Multiple late payments,High risk,1993-12-20,30,FULL_TIME,2000,8000,4,3,0.70,false,0,false,0,RENT,2,MEDIUM_TERM,6.5,NONE,0,REQUIRED,620,40000,FAMILY
CUST010,Lisa Anderson,700,88000,0.38,86,3,35000,HOME_IMPROVEMENT,1,200000,MARRIED,BACHELORS,MANUFACTURING,MEDIUM,ACTIVE,Stable profile,Good customer,1987-04-15,36,FULL_TIME,2600,40000,9,2,0.30,false,0,false,0,OWN,5,MEDIUM_TERM,4.2,REAL_ESTATE,200000,NONE,0,0,NA
```

Estos ejemplos cubren una amplia gama de perfiles de clientes:

1. **Clientes de Bajo Riesgo (CUST001, CUST004, CUST008)**:
   - Alto puntaje de crédito (>750)
   - Ingresos estables y altos
   - Bajo ratio deuda-ingreso
   - Excelente historial de pagos
   - Propiedades de alto valor como garantía

2. **Clientes de Riesgo Medio (CUST002, CUST005, CUST006, CUST010)**:
   - Puntaje de crédito moderado (650-720)
   - Ingresos estables pero moderados
   - Ratio deuda-ingreso aceptable
   - Buen historial de pagos con algunas variaciones
   - Mezcla de garantías y sin garantías

3. **Clientes de Alto Riesgo (CUST003, CUST007, CUST009)**:
   - Puntaje de crédito bajo (<600)
   - Ingresos bajos o inestables
   - Alto ratio deuda-ingreso
   - Historial de pagos problemático
   - Requieren garantías adicionales

Cada perfil incluye variaciones en:
- Estado civil
- Nivel educativo
- Tipo de empleo
- Propósito del préstamo
- Historial crediticio
- Garantías disponibles
- Relación con garantes

## Casos de Uso y Ejemplos

### 1. Cliente de Bajo Riesgo (CUST001)
```json
{
  "customerId": "CUST001",
  "riskCategory": "LOW",
  "baseScore": 88.2,
  "incomeRiskFactor": 0.2,
  "debtRiskFactor": 0.15,
  "paymentRiskFactor": 0.05,
  "finalScore": 85.0,
  "defaultProbability": 0.12,
  "recommendations": {
    "creditLimit": "Aumentar límite de crédito",
    "interestRate": "Ofrecer tasa preferencial",
    "terms": "Términos flexibles"
  }
}
```

### 2. Cliente de Riesgo Medio (CUST002)
```json
{
  "customerId": "CUST002",
  "riskCategory": "MEDIUM",
  "baseScore": 80.0,
  "incomeRiskFactor": 0.35,
  "debtRiskFactor": 0.25,
  "paymentRiskFactor": 0.15,
  "finalScore": 65.0,
  "defaultProbability": 0.28,
  "recommendations": {
    "creditLimit": "Mantener límite actual",
    "interestRate": "Tasa estándar",
    "terms": "Términos estándar",
    "payment": "Sugerir plan de pago estructurado"
  }
}
```

### 3. Cliente de Alto Riesgo (CUST003)
```json
{
  "customerId": "CUST003",
  "riskCategory": "HIGH",
  "baseScore": 68.2,
  "incomeRiskFactor": 0.55,
  "debtRiskFactor": 0.45,
  "paymentRiskFactor": 0.30,
  "finalScore": 45.0,
  "defaultProbability": 0.62,
  "recommendations": {
    "creditLimit": "Reducir límite de crédito",
    "interestRate": "Tasa más alta",
    "terms": "Términos más estrictos",
    "income": "Solicitar comprobante de ingresos adicional",
    "debt": "Recomendar reducción de deuda",
    "payment": "Sugerir plan de pago estructurado"
  }
}
```

## Factores de Análisis

### 1. Puntuación Base de Crédito
- Normalización: (creditScore / 850) * 100
- Peso: 40% del puntaje final
- Ejemplo: 750 puntos → 88.2 puntos normalizados

### 2. Factor de Ingresos
- Normalización: min(income / 200000, 1.0)
- Peso: 30% del puntaje final
- Ejemplo: $120,000 → 0.6 normalizado

### 3. Factor de Ratio Deuda-Ingreso
- Normalización: min(debtToIncomeRatio / 100, 1.0)
- Peso: 20% del puntaje final
- Ejemplo: 0.35 (35%) → 0.35 normalizado

### 4. Factor de Historial de Pagos
- Normalización: paymentHistory / 100
- Peso: 10% del puntaje final
- Ejemplo: 95% → 0.95 normalizado

## Resultados

La aplicación genera dos tipos de salida:

1. **Base de Datos H2**:
   - Tabla: credit_risk_data
   - Campos: Todos los campos del modelo más resultados del análisis

2. **Archivo CSV de Salida**:
   - Ubicación: src/main/resources/output/credit-risk-results.csv
   - Incluye: Datos originales más resultados del análisis

## Monitoreo

La aplicación expone varios endpoints de monitoreo:

- `/actuator/health`: Estado de la aplicación
- `/actuator/metrics`: Métricas de rendimiento
- `/actuator/prometheus`: Métricas en formato Prometheus

## Contribución

1. Fork el repositorio
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles. 