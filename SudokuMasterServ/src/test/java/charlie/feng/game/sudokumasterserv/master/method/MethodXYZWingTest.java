/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import charlie.feng.game.sudokumasterserv.master.Cell;
import charlie.feng.game.sudokumasterserv.master.Grid;

/**
 * Test cases for XYZ-Wing method.
 */
public class MethodXYZWingTest {

    @Test
    public void checkXYZWingColumn() {
        // Test XYZ-Wing with XZ in same column
        // X:1 Y:2 Z:3
        Grid grid = new Grid(Grid.EMPTY_GRID);
        // Set up XYZ cell (1,2,3) at (1,1)
        Cell cellXYZ = grid.getCells()[1][1];
        cellXYZ.removeDigitFromCandidate(4, "Mock", null);
        cellXYZ.removeDigitFromCandidate(5, "Mock", null);
        cellXYZ.removeDigitFromCandidate(6, "Mock", null);
        cellXYZ.removeDigitFromCandidate(7, "Mock", null);
        cellXYZ.removeDigitFromCandidate(8, "Mock", null);
        cellXYZ.removeDigitFromCandidate(9, "Mock", null);

        // Set up YZ cell (2,3) at (0,0) in same block
        Cell cellYZ = grid.getCells()[0][0];
        cellYZ.removeDigitFromCandidate(1, "Mock", null);
        cellYZ.removeDigitFromCandidate(4, "Mock", null);
        cellYZ.removeDigitFromCandidate(5, "Mock", null);
        cellYZ.removeDigitFromCandidate(6, "Mock", null);
        cellYZ.removeDigitFromCandidate(7, "Mock", null);
        cellYZ.removeDigitFromCandidate(8, "Mock", null);
        cellYZ.removeDigitFromCandidate(9, "Mock", null);

        // Set up XZ cell (1,3) at (4,1) in same column
        Cell cellXZ = grid.getCells()[4][1];
        cellXZ.removeDigitFromCandidate(2, "Mock", null);
        cellXZ.removeDigitFromCandidate(4, "Mock", null);
        cellXZ.removeDigitFromCandidate(5, "Mock", null);
        cellXZ.removeDigitFromCandidate(6, "Mock", null);
        cellXZ.removeDigitFromCandidate(7, "Mock", null);
        cellXZ.removeDigitFromCandidate(8, "Mock", null);
        cellXZ.removeDigitFromCandidate(9, "Mock", null);

        new MethodXYZWing().apply(grid);

        // Verify Z (3) is removed from affected cells
        Assertions.assertFalse(grid.getCells()[0][1].isSupportCandidate(3), "Cell at row 0 col 1 should not have candidate 3");
        Assertions.assertFalse(grid.getCells()[2][1].isSupportCandidate(3), "Cell at row 2 col 1 should not have candidate 3");

    }

    @Test
    public void checkXYZWingRow() {
        // Test XYZ-Wing with XZ in same row
        // X:4 Y:5 Z:6
        Grid grid = new Grid(Grid.EMPTY_GRID);
        // Set up XYZ cell (4,5,6) at (1,1)
        Cell cellXYZ = grid.getCells()[1][1];
        cellXYZ.removeDigitFromCandidate(1, "Mock", null);
        cellXYZ.removeDigitFromCandidate(2, "Mock", null);
        cellXYZ.removeDigitFromCandidate(3, "Mock", null);
        cellXYZ.removeDigitFromCandidate(7, "Mock", null);
        cellXYZ.removeDigitFromCandidate(8, "Mock", null);
        boolean mock = cellXYZ.removeDigitFromCandidate(9, "Mock", null);

        // Set up YZ cell (5,6) at (0,0) in same block
        Cell cellYZ = grid.getCells()[0][0];
        cellYZ.removeDigitFromCandidate(1, "Mock", null);
        cellYZ.removeDigitFromCandidate(2, "Mock", null);
        cellYZ.removeDigitFromCandidate(3, "Mock", null);
        cellYZ.removeDigitFromCandidate(4, "Mock", null);
        cellYZ.removeDigitFromCandidate(7, "Mock", null);
        cellYZ.removeDigitFromCandidate(8, "Mock", null);
        cellYZ.removeDigitFromCandidate(9, "Mock", null);

        // Set up XZ cell (4,6) at (1,4) in same row
        Cell cellXZ = grid.getCells()[1][4];
        cellXZ.removeDigitFromCandidate(1, "Mock", null);
        cellXZ.removeDigitFromCandidate(2, "Mock", null);
        cellXZ.removeDigitFromCandidate(3, "Mock", null);
        cellXZ.removeDigitFromCandidate(5, "Mock", null);
        cellXZ.removeDigitFromCandidate(7, "Mock", null);
        cellXZ.removeDigitFromCandidate(8, "Mock", null);
        cellXZ.removeDigitFromCandidate(9, "Mock", null);

        new MethodXYZWing().apply(grid);

        // Verify Z (6) is removed from affected cells
        Assertions.assertFalse(grid.getCells()[1][0].isSupportCandidate(6), "Cell at row 1 col 0 should not have candidate 6");
        Assertions.assertFalse(grid.getCells()[1][2].isSupportCandidate(6), "Cell at row 1 col 2 should not have candidate 6");
    }
}
