# Clean Sweep System

Group 8: 
	Dylan Neal
	Huy Hoang Phan
	Zhihong He
	Faizan Moin Lateefuddin

The Clean Sweep project simulates an intelligent robotic vacuum designed to autonomously navigate and clean a typical household environment. Key features include obstacle avoidance, power management, dirt detection, and sensor simulation, making it a comprehensive system for robotic vacuum functionality.

**Tech Stack**: Spring Boot, Javafx, Java  
**Architecture**: Modular and scalable, following clean architecture principles for separation of concerns.

## Project Overview
The Clean Sweep system consists of several core modules:

- **Navigation and Control**: The vacuum navigates autonomously, avoiding obstacles and returning to the charging station when power is low.
- **Dirt Detection and Cleaning**: Detects and cleans dirt based on surface type and dirt level.
- **Power Management**: Manages battery levels considering surface types and navigation/cleaning activities.
- **Sensor Simulation**: Simulates real-world sensor data for obstacle detection, surface recognition, and dirt levels.

## Project Structure

### ***BackEnd***
### 1. Common
- **ConfigManager**: Manages system configurations (e.g., file paths, floor plans).
- **Enums**: Direction (UP, DOWN, LEFT, RIGHT), FloorType (BARE_FLOOR, LOW_PILE_CARPET, HIGH_PILE_CARPET).

### 2. Model (Domain)
- **Cell**: Represents a floor map cell, containing floor type, dirt level, and obstacle information.
- **FloorMap**: Represents the floor plan with methods for initialization and loading.

### 3. Controller
- **LogEntryController**: Manages system logs (e.g., movements, cleaning actions, recharging).

### 4. Application (Services)
- **NavigationService**: Controls vacuum navigation using BFS, handles obstacle avoidance.
- **BatteryService**: Manages battery consumption and recharging.
- **DirtService**: Cleans detected dirt and updates the vacuum’s capacity.
- **SensorSimulatorService**: Simulates sensor behavior for dirt, obstacles, and floor types.

### 5. Infrastructure
- **ActivityLogger**: Logs navigation, dirt cleaned, obstacles, and battery usage.
- **LogEntryRepository**: Persists log entries for system activity history.

### ***Frontend (Visualization)***
- **FloorPlanVisualizer**: Renders the floor map, showing vacuum, obstacles, and dirt.
- **RobotVisualizer**: Visualizes the vacuum's movements and actions.
- **HUDController**: Displays battery status, dirt level, and system status.

## Key Functionalities
- **BFS Navigation**: Ensures the vacuum systematically moves, avoiding obstacles.
- **Battery Management**: Returns the vacuum to the charging station when power is low.
- **Dirt Detection**: Cleans dirty cells as the vacuum traverses the floor.
- **Sensor Simulation**: Mimics real-world sensor data (e.g., floor types, dirt levels, obstacles).

## Testing
The system is covered by unit tests for all major modules:

- **NavigationServiceTest**: Tests BFS navigation, logging, and battery management.
- **SensorSimulatorServiceTest**: Validates sensor accuracy for dirt, floor type, and obstacles.
- **CleanSweepSystemApplicationTests**: Ensures seamless module integration.

## Resources
- **Floor Plans**: Stored in `resources/templates/floorPlan.json`.
- **Application Properties**: Configured in `resources/application.properties` (e.g., grid size, battery capacity, dirt levels).

## BFS For Robot Clean (demo)
![move](https://github.com/user-attachments/assets/3430b70d-c87c-4db4-9d94-3a12318a5b2e)

## BackEnd Running Condition
![646d93d542776a98234b648279b806d](https://github.com/user-attachments/assets/bc8bfa85-1152-4e4e-913c-92b88df27ee4)
![3f4848306fbafaf6898552b23c4ab63](https://github.com/user-attachments/assets/bbc345d6-0caf-4f83-8faf-17ccd18aacc5)

## API Test
![aa8b8da91389fc53ccc1c0f39485309](https://github.com/user-attachments/assets/39121677-8d9c-4fa2-a0df-187fb19e42a2)
![0a51b546c3ef78d03a3e96e11460de2](https://github.com/user-attachments/assets/e8b97fbc-13f5-4c42-9a33-eb12ba8b0304)

# H2 Database
![3f549f89b73b821c9cee14299d3c489](https://github.com/user-attachments/assets/0d1411f0-27b1-4e6d-866b-f6393e5c7df2)

# Unit Test
![b5c36954efeedfd77c6303671ea3e80](https://github.com/user-attachments/assets/54661359-e6c0-459a-8073-a91774ae2056)

### Prerequisites
- **Java 19**
- **Javafx 20**
- **Maven**

### Build and Run the Application
To build and run the application, use the following command:
```sh
mvn spring-boot:run






