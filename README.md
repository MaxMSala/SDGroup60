# Software Design 
# Green Home System

#### Software Design Group 60

## Project Overview
The Green Home software is built as a modular Java application that processes household data to calculate environmental and financial impacts. Users interact with the system through a simple Swing-based interface, where they can input details such as appliance types, usage timeframes, number of residents, and electricity tariff.

Once the data is entered, the system performs a series of calculations based on values retrieved from an internal appliance database and carbon intensity estimates. These calculations include:

Total carbon emissions per appliance, based on usage duration, power consumption, and average carbon intensity

Total cost per appliance, based on electricity tariff and energy usage

User and household EcoScores, which gamify sustainability by rating low-emission behaviors

The system stores and retrieves user data using a JSON-based format, allowing households to save and reload configurations. The central House class ensures that all components, users, appliances, timeframes, and tariffs remain consistent across the application.

In addition to real-time reports, the software allows users to simulate alternative scenarios using a dedicated What-If tool. This enables households to explore the effects of removing or adjusting appliances or even general inputs, without altering the main dataset.

Finally, Green Home generates a comprehensive report that:

Ranks appliances by carbon emissions and cost

Compares the household’s footprint to the global per capita average

Provides personalized recommendations, including the worst emitters, optimal usage times, and suggestions for more efficient alternatives

All calculations and recommendations are based on live or averaged carbon intensity data, and though users can select from multiple regions, real-time data is currently supported only for The Netherlands due to API limitations. Other regions are included for simulation purposes only.


### Key Aspects of the System
-**Real-Time and Averaged Carbon Data** ⟹ The system estimates carbon intensity using averaged values retrieved from the Electricity Maps API (limited to the past 24 hours and a single country). These are applied at the start and end of each appliance usage interval to provide realistic, session-based emission estimates.

-**Tailored Reports and EcoScores** ⟹ Users receive detailed carbon and cost reports, including personalized User EcoScores and a Household EcoScore. These scores promote awareness and encourage lower emissions by ranking users and usage efficiency.

-**Interactive Scenario Simulation** ⟹ The built-in What-If Scenarios tool allows users to add, remove, or reconfigure appliances and timeframes, helping them visualize the environmental and financial impact of potential lifestyle changes.

-**Emission Timing Recommendations** ⟹ The system identifies optimal 3-hour intervals with lower carbon intensity and highlights periods to avoid, guiding users toward more sustainable usage schedules based on real-time or average carbon data.

## Prerequisites
Before running the project, ensure you have the following installed:
- SDK: corretto-21 Amazon Corretto 21.0.6 (Java 21)
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
│       │   └── Average.java
│       ├── household/
│       │   ├── Appliance.java
│       │   ├── CarbonIntensity.java
│       │   ├── House.java
│       │   ├── Parser.java
│       │   ├── Timeframe.java
│       │   └── User.java
│       ├── input/
│       │   ├── Form.java
│       │   ├── Report.java
│       │   └── WhatIfScenarios.java
│       ├── reporting/
│       │   └── Recommendations.java
│       ├── time/
│       │   └── DateTime.java
│       └── validation/
│           └── Validator.java
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
