package com.cleanSweep;

import com.cleanSweep.backend.application.*;
import com.cleanSweep.backend.common.FloorType;
import com.cleanSweep.backend.domain.Cell;
import com.cleanSweep.backend.domain.FloorMap;
import com.cleanSweep.backend.infrastructure.ActivityLogger;
import com.cleanSweep.frontend.visualization.FloorPlanVisualizer;
import com.cleanSweep.frontend.visualization.RobotVisualizer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class NavigationServiceTest {

    @Mock
    private FloorMap floorMap;
    @Mock
    private DirtService dirtService;
    @Mock
    private BatteryService batteryService;
    @Mock
    private SensorSimulatorService sensorSimulatorService;
    @Mock
    private ActivityLogger activityLogger;
    @Mock
    private FloorPlanVisualizer floorPlanVisualizer;
    @Mock
    private RobotVisualizer robotVisualizer;
    @Mock
    private GraphicsContext gc;

    @InjectMocks
    private NavigationService navigationService;

    private Cell[][] cells;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize a 5x5 grid
        cells = new Cell[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                // Initialize with a floor type and default values
                cells[i][j] = new Cell(i, j, FloorType.BARE_FLOOR, false, false, false, false, 0, null);
            }
        }
        when(floorMap.getCells()).thenReturn(cells);

        // Mock the GraphicsContext and Canvas
        Canvas mockCanvas = mock(Canvas.class);
        when(gc.getCanvas()).thenReturn(mockCanvas);
        when(mockCanvas.getWidth()).thenReturn(500.0);
        when(mockCanvas.getHeight()).thenReturn(500.0);

        // Mock batteryService and sensor behaviors
        when(batteryService.hasSufficientPower()).thenReturn(true);
        when(sensorSimulatorService.isObstacle(anyInt(), anyInt())).thenReturn(false);
    }

    @Test
    void testStartNavigation() {
        navigationService.startNavigation(0, 0);

        verify(activityLogger).logMovement(0, 0, "Start");
    }

    @Test
    void testStepNavigationWithDirtCleaning() {
        // Set dirt level and start navigation
        cells[0][0].setDirtLevel(2);
        navigationService.startNavigation(0, 0);
        navigationService.stepNavigation(gc, floorPlanVisualizer, robotVisualizer);

        // Verify the dirt is cleaned, and the robot moves
        verify(dirtService).cleanDirt(0, 0);
        verify(activityLogger).logMovement(0, 0, "Visiting");
    }

    @Test
    void testStepNavigationWithDirt() {
        // Place dirt on cell (0, 0)
        cells[0][0].setDirtLevel(3);

        // Start navigation
        navigationService.startNavigation(0, 0);

        // Simulate one step where dirt is cleaned
        navigationService.stepNavigation(gc, floorPlanVisualizer, robotVisualizer);

        // Verify that the dirt was cleaned and logged
        verify(dirtService).cleanDirt(0, 0);
        verify(activityLogger).logMovement(0, 0, "Visiting");
    }


    @Test
    void testStepNavigationCompletion() {
        // Start navigation at (0, 0)
        navigationService.startNavigation(0, 0);

        // Simulate navigation completion by iterating enough times for DFS traversal to complete
        for (int i = 0; i < 100; i++) {
            navigationService.stepNavigation(gc, floorPlanVisualizer, robotVisualizer);
        }

        // Verify that all cells have been visited and navigation has completed
        verify(activityLogger, atLeastOnce()).logMovement(anyInt(), anyInt(), eq("All cells visited, navigation completed"));
        assertTrue(navigationService.isNavigationCompleted());
    }

}
