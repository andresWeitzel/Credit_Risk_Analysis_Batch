<div align="center">
  <img src="./src/main/resources/static/img/credit_risk.png" >
</div>

  <div align="right">
    <img width="24" height="24" src="./src/main/resources/static/icons/backend/java/png/java.png" />
    <img width="20" height="20" src="./src/main/resources/static/icons/devops/png/maven.png" />
    <img width="22" height="22" src="./src/main/resources/static/icons/devops/png/postman.png" />
    <img width="22" height="22" src="./src/main/resources/static/icons/devops/png/git.png" />
    <img width="20" height="20" src="./src/main/resources/static/icons/backend/java/png/junit.png" />
    <img width="20" height="20" src="./src/main/resources/static/icons/backend/java/png/spring-boot.png" /> 
    <img width="20" height="20" src="./src/main/resources/static/icons/backend/java/png/spring-batch.png" />    
  </div>

<br>

<br>


<div align="right"> 
  <a href="./src/main/resources/static/translation/README.es.md">
    <img width="65" height="40" src="./src/main/resources/static/icons/translation/arg-flag.jpg" />
  </a> 
  <a href="./README.md">
    <img width="65" height="40" src="./src/main/resources/static/icons/translation/eeuu-flag.jpg" />
  </a> 
</div>

<br>

<br>

<div align="center">

# Credit Risk Analysis Batch ![Status](./src/main/resources/static/icons/badges/status-completed.svg)

</div>

This Spring Batch application is designed to analyze and evaluate customer credit risk using a sophisticated multifactorial scoring system. The system processes customer data from a CSV file, performs detailed risk analysis, and generates results in both an H2 database and an output CSV file.

* [Functional tests video](https://www.youtube.com/watch?v=9IEHzHfXZbo) <a href="https://www.youtube.com/watch?v=9IEHzHfXZbo" target="_blank"> <img src="./src/main/resources/static/icons/social-networks/yt.png" width="25" /></a>


## Sections

<details>
<summary>1. Main Features</summary>

### 1.1 Multifactorial Risk Analysis
- **Base Credit Score**: Credit score normalization and weighting
- **Risk Factors**:
  - Income Factor
  - Debt-to-Income Ratio Factor
  - Payment History Factor
- **Default Probability Calculation**: Using sigmoid function
- **Risk Categorization**: LOW, MEDIUM, HIGH, VERY_HIGH

### 1.2 Recommendation System
- Suggested credit limits
- Recommended interest rates
- Credit terms
- Specific recommendations by risk factor

### 1.3 Batch Processing
- CSV data reading
- Configurable chunk processing
- Database and CSV file writing
- Error handling and retries

### 1.4 Monitoring and Metrics
- Actuator endpoints for monitoring
- Prometheus integration
- Detailed logging
- Performance metrics
</details>

<details>
<summary>2. Technical Requirements and Structure</summary>

### 2.1 Technical Requirements
- Java 17 or higher
- Maven 3.6 or higher
- Spring Boot 3.x
- Spring Batch 5.x
- H2 Database
- Caffeine Cache

### 2.2 Project Structure
```
src/main/java/com/example/creditrisk/
├── config/
│   └── BatchConfig.java         # Batch job configuration
├── model/
│   └── CreditRiskData.java      # Data model
├── processor/
│   └── CreditRiskProcessor.java # Risk processor
├── reader/
│   └── CreditRiskItemReader.java # Data reader
├── service/
│   └── RiskAnalysisService.java # Risk analysis service
├── writer/
│   └── CreditRiskFileWriter.java # Results writer
└── CreditRiskBatchApplication.java
```
</details>

<details>
<summary>3. Configuration and Execution</summary>

1. Clone the repository:
```bash
git clone [repository-url]
cd credit-risk-batch
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```
</details>

<details>
<summary>4. Data Format and Examples</summary>

### 4.1 Input Data Format

The input file must be in CSV format with the following columns:

#### Basic Customer Information
- customerId: Unique customer identifier
- customerName: Customer name
- birthDate: Birth date (YYYY-MM-DD)
- age: Customer age
- maritalStatus: Marital status (SINGLE, MARRIED, DIVORCED)
- educationLevel: Education level (HIGH_SCHOOL, BACHELORS, MASTERS, DOCTORATE)

#### Financial Information
- creditScore: Credit score (0-850)
- income: Annual income
- debtToIncomeRatio: Debt-to-income ratio (%)
- monthlyExpenses: Monthly expenses
- savingsBalance: Savings balance
- propertyValue: Property value (if applicable)

#### Employment Information
- employmentType: Employment type (FULL_TIME, PART_TIME)
- employmentYears: Years of employment
- industry: Industry/Sector

#### Credit History
- paymentHistory: Payment history (0-100)
- creditHistoryYears: Years of credit history
- numberOfCreditCards: Number of credit cards
- creditCardUtilization: Credit card utilization (%)
- hasBankruptcy: Bankruptcy indicator
- bankruptcyYearsAgo: Years since bankruptcy (if applicable)
- hasForeclosure: Foreclosure indicator
- foreclosureYearsAgo: Years since foreclosure (if applicable)

#### Loan Information
- loanAmount: Loan amount
- loanPurpose: Loan purpose (MORTGAGE, CAR, PERSONAL, BUSINESS, EDUCATION, CONSOLIDATION, HOME_IMPROVEMENT, INVESTMENT)
- existingLoans: Number of existing loans
- loanTerm: Loan term (SHORT_TERM, MEDIUM_TERM, LONG_TERM)
- interestRate: Interest rate (%)
- collateralType: Collateral type (NONE, REAL_ESTATE, VEHICLE, BUSINESS)
- collateralValue: Collateral value

#### Residence Information
- residenceType: Residence type (OWN, RENT)
- yearsAtCurrentAddress: Years at current address

#### Guarantor Information
- guarantorStatus: Guarantor status (NONE, REQUIRED)
- guarantorCreditScore: Guarantor credit score
- guarantorIncome: Guarantor income
- guarantorRelationship: Relationship with guarantor (FAMILY, FRIEND, NA)

#### Analysis Fields
- riskCategory: Risk category (LOW, MEDIUM, HIGH, VERY_HIGH)
- status: Application status (ACTIVE, INACTIVE)
- additionalInfo: Additional information
- recommendations: Specific recommendations

### 4.2 Input File Example
```csv
customerId,customerName,creditScore,income,debtToIncomeRatio,paymentHistory,employmentYears,loanAmount,loanPurpose,existingLoans,propertyValue,maritalStatus,educationLevel,industry,riskCategory,status,additionalInfo,recommendations,birthDate,age,employmentType,monthlyExpenses,savingsBalance,creditHistoryYears,numberOfCreditCards,creditCardUtilization,hasBankruptcy,bankruptcyYearsAgo,hasForeclosure,foreclosureYearsAgo,residenceType,yearsAtCurrentAddress,loanTerm,interestRate,collateralType,collateralValue,guarantorStatus,guarantorCreditScore,guarantorIncome,guarantorRelationship
CUST001,John Smith,720,85000,0.35,95,8,150000,Mortgage,1,350000,Married,Bachelors,Technology,Low,Approved,Stable employment history,Consider refinancing in 2 years,1980-05-15,43,Full-time,3500,50000,15,2,0.25,false,0,false,0,Mortgage,5,Long-term,4.5,Real Estate,350000,None,,,,
CUST002,Maria Garcia,680,65000,0.45,88,5,120000,Car,2,250000,Single,Masters,Healthcare,Medium,Approved,Recent job change,Monitor debt ratio,1988-08-22,35,Full-time,2800,25000,8,3,0.45,false,0,false,0,Rent,2,Medium-term,5.2,Vehicle,45000,None,,,,
CUST003,Robert Johnson,580,45000,0.65,75,3,80000,Personal,3,180000,Divorced,High School,Retail,High,Rejected,Multiple late payments,Improve credit score,1975-11-30,48,Part-time,2200,5000,5,4,0.85,true,3,false,0,Rent,1,Short-term,7.5,None,0,Required,650,55000,Parent
CUST004,Sarah Williams,750,120000,0.25,98,12,200000,Business,0,500000,Married,PhD,Finance,Low,Approved,Excellent credit history,Consider investment opportunities,1972-03-10,51,Self-employed,4500,150000,20,2,0.15,false,0,false,0,Own,10,Long-term,4.0,Real Estate,500000,None,,,,
CUST005,Michael Brown,620,55000,0.55,82,4,95000,Education,1,220000,Single,Bachelors,Education,Medium,Approved,Student loan debt,Consolidate loans,1990-07-18,33,Full-time,2300,15000,6,2,0.60,false,0,false,0,Rent,3,Medium-term,5.8,None,0,None,,,,
CUST006,Lisa Chen,710,95000,0.30,92,7,180000,Home Improvement,2,400000,Married,Masters,Engineering,Low,Approved,High property value,Consider home equity,1985-12-05,38,Full-time,3800,75000,12,3,0.30,false,0,false,0,Mortgage,6,Long-term,4.2,Real Estate,400000,None,,,,
CUST007,David Miller,590,48000,0.60,78,2,70000,Debt Consolidation,4,150000,Single,Associates,Manufacturing,High,Rejected,High debt load,Debt management plan,1982-09-25,41,Contract,2100,8000,4,5,0.90,false,0,true,2,Rent,1,Short-term,8.0,None,0,Required,680,60000,Sibling
CUST008,Emma Wilson,730,110000,0.28,96,10,250000,Investment,1,600000,Married,Bachelors,Real Estate,Low,Approved,Investment property,Portfolio diversification,1978-04-12,45,Self-employed,4200,200000,18,2,0.20,false,0,false,0,Own,8,Long-term,4.8,Real Estate,600000,None,,,,
CUST009,James Taylor,650,72000,0.40,85,6,130000,Medical,2,280000,Married,Bachelors,Healthcare,Medium,Approved,Medical expenses,Health insurance review,1987-06-30,36,Full-time,3000,35000,9,3,0.50,false,0,false,0,Mortgage,4,Medium-term,5.5,Real Estate,280000,None,,,,
CUST010,Sophia Martinez,670,68000,0.42,87,5,115000,Wedding,1,260000,Engaged,Bachelors,Marketing,Medium,Approved,Upcoming wedding,Budget planning,1992-02-14,31,Full-time,2900,20000,7,2,0.40,false,0,false,0,Rent,2,Short-term,6.0,None,0,Provided,700,75000,Fiance
```
</details>

<details>
<summary>5. Use Cases and Analysis</summary>

### 5.1 Use Cases

#### Low Risk Customer (CUST001)
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
    "creditLimit": "Increase credit limit",
    "interestRate": "Offer preferred rate",
    "terms": "Flexible terms"
  }
}
```

#### Medium Risk Customer (CUST002)
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
    "creditLimit": "Maintain current limit",
    "interestRate": "Standard rate",
    "terms": "Standard terms",
    "payment": "Suggest structured payment plan"
  }
}
```

#### High Risk Customer (CUST003)
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
    "creditLimit": "Reduce credit limit",
    "interestRate": "Higher rate",
    "terms": "Stricter terms",
    "income": "Request additional income verification",
    "debt": "Recommend debt reduction",
    "payment": "Suggest structured payment plan"
  }
}
```

### 5.2 Analysis Factors

#### Base Credit Score
- Normalization: (creditScore / 850) * 100
- Weight: 40% of final score
- Example: 750 points → 88.2 normalized points

#### Income Factor
- Normalization: min(income / 200000, 1.0)
- Weight: 30% of final score
- Example: $120,000 → 0.6 normalized

#### Debt-to-Income Ratio Factor
- Normalization: min(debtToIncomeRatio / 100, 1.0)
- Weight: 20% of final score
- Example: 0.35 (35%) → 0.35 normalized

#### Payment History Factor
- Normalization: paymentHistory / 100
- Weight: 10% of final score
- Example: 95% → 0.95 normalized
</details>

<details>
<summary>6. Results and Monitoring</summary>

### 6.1 Results

The application generates two types of output:

#### H2 Database
- **Table**: credit_risk_data
- **Location**: Memory (H2 in-memory database)
- **Access**: http://localhost:8080/h2-console
- **Credentials**: 
  - JDBC URL: jdbc:h2:mem:creditriskdb
  - Username: sa
  - Password: (empty)

#### Output CSV File
- **Location**: src/main/resources/output/credit-risk-results.csv
- **Format**: CSV with the following columns:

##### Original Fields
- All fields from the input file

##### Analysis Fields
- baseScore: Normalized base score (0-100)
- incomeRiskFactor: Income risk factor (0-1)
- debtRiskFactor: Debt risk factor (0-1)
- paymentRiskFactor: Payment history risk factor (0-1)
- finalScore: Weighted final score (0-100)
- defaultProbability: Default probability (0-1)
- riskCategory: Risk category (LOW, MEDIUM, HIGH, VERY_HIGH)

##### Recommendation Fields
- creditLimitRecommendation: Credit limit recommendation
- interestRateRecommendation: Interest rate recommendation
- termsRecommendation: Terms recommendation
- incomeRecommendation: Income-related recommendation
- debtRecommendation: Debt-related recommendation
- paymentRecommendation: Payment-related recommendation

### 6.2 Monitoring

The application exposes several monitoring endpoints:

- `/actuator/health`: Application status
- `/actuator/metrics`: Performance metrics
- `/actuator/prometheus`: Prometheus format metrics
</details>


<details>
<summary>7. Functionality Test</summary>

#### [Watch video](https://www.youtube.com/watch?v=9IEHzHfXZbo)

  <a href="https://www.youtube.com/watch?v=9IEHzHfXZbo">
    <img src="./src/main/resources/static/img/credit_risk_yt.png" />
  </a> 


</details>

<details>
<summary>8. Contribution and License</summary>

### 8.1 Contribution

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### 8.2 License

This project is under the MIT License. See the `LICENSE` file for more details. 
