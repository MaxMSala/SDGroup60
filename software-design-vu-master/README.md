# Software Design 
# Green Home System

#### Software Design Group 68

## Project Overview
The Green Home software enables households to calculate and better understand their carbon footprint, helping users make more informed and sustainable choices regarding energy consumption. Although the interface allows users to select from a wide range of countries, the current implementation is restricted to real data for The Netherlands due to financial constraints
Specifically, the costs associated with accessing live environmental and energy APIs. Nevertheless, users can still explore other regions for fictional or educational purposes, simulating how the tool would behave if expanded in the future.

Users can input details about their household appliances and usage schedules, and the system estimates both carbon emissions and energy costs based on a comprehensive internal appliance database. This database contains:

Average power consumption (used to estimate electricity usage and associated emissions)

House EcoScore and UserEcoScore


Additionally, the system enables users to simulate alternative scenarios by adding, removing, or configuring appliances to explore how their choices affect carbon emissions. Finally, the system provides personalized recommendations to help users lower their household's carbon footprint.

### Key Aspects of the System
- **Data-Driven Predictions** ⟹ The system estimates carbon intensity based on historical data from the past three years, ensuring more accurate and reliable predictions.
- **Personalized Insights** ⟹ Users receive tailored emission reports, comparisons with other households, and actionable recommendations.
- **Scenario Exploration** ⟹ Users can simulate "what-if" scenarios to understand the impact of changing appliances or usage habits.
- **Peak and Off-Peak Energy Optimization** ⟹ The system suggests optimal appliance usage times based on carbon intensity fluctuations, helping users reduce emissions.

## Prerequisites
Before running the project, ensure you have the following installed:
- Java Development Kit (JDK) 11
- IntelliJ IDEA installation
- Gradle

## Running the Application
1. Download the whole repository.
2. Open the repository in IntelliJ IDEA.
3. Find:
   ```sh
   software-design-vu-master/src/greenhome/Main.java

4. Run Main.java, and the application will start.

## Project Structure
``````
software-design-vu-master/
├── src/
│   └── greenhome/
│       ├── apiintegration/
│       │   ├── Average.java
│       │   └── CarbonIntensity.java
│       ├── household/
│       │   ├── Appliance.java
│       │   ├── House.java
│       │   ├── Parser.java
│       │   ├── Timeframe.java
│       │   └── User.java
│       ├── input/
│       │   ├── Form.java
│       │   └── WhatIfScenarios.java
│       ├── reporting/
│       │   ├── Recommendations.java
│       │   └── Report.java
│       ├── time/
│       │   └── DateTime.java
│       └── validation/
│           └── [validation classes]
├── out/production/SDGroup60/greenhome/
├── lib/
├── .idea/
├── gradle/wrapper/
├── build.gradle
├── gradlew
├── gradlew.bat
├── settings.gradle
├── .gitignore
├── LICENSE
├── README.md                          # Project documentation
└── json.json
``````

