Clean Sweep

Group 8: 
	Dylan Neal
	Huy Hoang Phan
	Zhihong He
	Faizan Moin Lateefuddin

Clean-Sweep is a control system for a robotic vacuum cleaner, designed to automate cleaning tasks in a simulated environment.
The project integrates various functionalities such as power management, navigation, sensor detection, dirt handling, and logging mechanisms. 

Features
- Autonomous Navigation: Navigate through a grid based floor plan while avoiding obstacles. 
- Dirt Detection and Cleaning: Detect and clean dirt in the environment using simulated sensors. 
- Power Management: Monitor and manage battery life, including returning to the charging station when necessary.
- User Interface: Visualize the robot’s movement and environment using JavaFX
- Logging: Log activities such as movements, cleaning actions, and power usage for diagnostics. 

Project Structure
- Control: Contains controllers for navigation, power management, dirt handling, and movement. 
- Sensor: Simulates sensor data for obstacle and dirt detection. 
- UI: User interface components built using JavaFX. 
- Visualization: Vizualizers for the floor plan and the robot. 
- Logging: Logging mechanisms for debugging 


Getting Started 

Prerequisites: 
- Java Development Kit
- Apache Maven 

Installation
1. Clone the repository
2. Navigate to the project directory: cd clean-sweep
3. Build the project: mvn clean install
4. Run the simulation: mvn javafx:run 
5. Run tests: mvn test


Usage
Upon launching the application, you will be prompted to start the simulation. 

Controls: 
	Start Simulation: Begin the cleaning simulation
	Stop Simulation: Pause the simulation 

Visual Indicators: 
	Robot: Blue Circle, moving around the room
	Brown Square: A dirty section of the floor
	Black Square: An obstacle that must be avoided


Components 

Controllers: 
- NavigationController: Manages the robot’s position and movement across the grid
- Power ManagementController: Handles battery consumption, monitoring, and recharging behaviors. 
- DirtHandler: Manages detection and cleaning of dirt within the environment, as well as dirt capacity. 
- MovementHandler: Processes movement commands and updates the robot’s position accordingly. 

Sensors:
- SensorSimulator: Simulates sensor inputs for detecting obstacles and dirt

Visualization: 
- FloorPlanVisualizer: Responsible for rendering the floor plan grid, including obstacles and dirt locations
- Robot Visualizer: Visualizes the robot’s current position on the floor plan 

User Interface: 
- SimulationUI: The main application class that initializes and runs the JavaFX user interface, integrating all components
- StatusPanel: Provides real-time updates on battery status and dirt capacity to the user 
- HUDController: Manages the heads-up display elements in the interface

Logging: 
- Activity Logger: Records various activities such as movements, battery usage, and dirt cleaning actions
- LogEntry: Represents individual log entries, potentially mapping to a database entity for persistent storage



