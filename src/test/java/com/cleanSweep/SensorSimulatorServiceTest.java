package com.cleanSweep;

import com.cleanSweep.backend.application.SensorSimulatorService;
import com.cleanSweep.backend.common.FloorType;
import com.cleanSweep.backend.domain.Cell;
import com.cleanSweep.backend.domain.FloorMap;
import com.cleanSweep.backend.infrastructure.ActivityLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SensorSimulatorServiceTest {

    @Mock
    private FloorMap floorMap;
    @Mock
    private ActivityLogger activityLogger;

    @InjectMocks
    private SensorSimulatorService sensorSimulatorService;

    private Cell[][] cells;
    private boolean[][] obstacleGrid;

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

        // Properly initialize obstacleGrid to match the size
        obstacleGrid = new boolean[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                obstacleGrid[i][j] = false;  // No obstacles initially
            }
        }
        sensorSimulatorService.initializeObstacleAndDirt();
    }

    @Test
    void testIsDirtPresent() {
        // Simulate dirt at (2, 2)
        cells[2][2].setDirtLevel(2);
        assertTrue(sensorSimulatorService.isDirtPresent(2, 2));

        // No dirt at (3, 3)
        cells[3][3].setDirtLevel(0);
        assertFalse(sensorSimulatorService.isDirtPresent(3, 3));
    }

    @Test
    void testCleanNoDirt() {
        // No dirt at (3, 3)
        cells[3][3].setDirtLevel(0);

        // Attempt to clean (should log "No dirt")
        sensorSimulatorService.cleanDirt(3, 3);

        verify(activityLogger).logNoDirtAtPosition(3, 3);
    }


    @Test
    void testCleanDirt() {
        // Set dirt level at (2, 2) and clean it
        cells[2][2].setDirtLevel(3);
        sensorSimulatorService.cleanDirt(2, 2);

        // Verify dirt level is reduced
        assertEquals(0, cells[2][2].getDirtLevel());
        verify(activityLogger).logCleaning(2, 2);
    }

    @Test
    void testGetSurfacePowerCost() {
        cells[1][1].setFloorType(FloorType.BARE_FLOOR);
        cells[2][2].setFloorType(FloorType.HIGH_PILE_CARPET);

        assertEquals(1, sensorSimulatorService.getSurfacePowerCost(1, 1));
        assertEquals(3, sensorSimulatorService.getSurfacePowerCost(2, 2));
    }
}
