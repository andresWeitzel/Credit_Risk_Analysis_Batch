# Credit Risk Analysis Batch Application

## Descripción
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