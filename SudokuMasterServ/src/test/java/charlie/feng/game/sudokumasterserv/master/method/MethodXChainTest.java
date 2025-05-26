/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Cell;
import charlie.feng.game.sudokumasterserv.master.Grid;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class MethodXChainTest {

    private void setupStrongLink(Grid grid, int row1, int col1, int row2, int col2, int digit) {
        // For a weak link, the digit can appear in both cells
        // But we need to ensure it can only appear in these two cells in their shared unit
        // If cells are in the same row
        if (row1 == row2) {
            for (int col = 0; col < 9; col++) {
                if (col != col1 && col != col2) {
                    grid.getCells()[row1][col].removeDigitFromCandidate(digit, "Test", null);
                }
            }
        } else if (col1 == col2) {
            for (int row = 0; row < 9; row++) {
                if (row != row1 && row != row2) {
                    grid.getCells()[row][col1].removeDigitFromCandidate(digit, "Test", null);
                }
            }
        } else {
            int blockRow1 = row1 / 3;
            int blockCol1 = col1 / 3;
            int blockRow2 = row2 / 3;
            int blockCol2 = col2 / 3;
            if (blockRow1 == blockRow2 && blockCol1 == blockCol2) {
                for (int r = blockRow1 * 3; r < blockRow1 * 3 + 3; r++) {
                    for (int c = blockCol1 * 3; c < blockCol1 * 3 + 3; c++) {
                        if ((r != row1 || c != col1) && (r != row2 || c != col2)) {
                            grid.getCells()[r][c].removeDigitFromCandidate(digit, "Test", null);
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testChainLength4SameColumn() {
        // Test case for a chain of length 4 (Strong-Weak-Strong)
        // Chain: (0,0)-(0,3)-(3,3)-(3,0) for digit 5
        // This forms a rectangle pattern in the grid
        Grid grid = new Grid(Grid.EMPTY_GRID);
        int digit = 5;

        // Setup the chain cells
        Cell cell1 = grid.getCells()[0][0]; // Start cell
        Cell cell2 = grid.getCells()[0][3]; // Strong link
        Cell cell3 = grid.getCells()[3][3]; // Weak link
        Cell cell4 = grid.getCells()[3][0]; // Strong link

        setupStrongLink(grid, 0, 0, 0, 3, digit);
        // Weak link do not need setup
        setupStrongLink(grid, 3, 3, 3, 0, digit);

        // Setup target cells that should have the digit eliminated
        // Each target cell must be able to see both ends of the chain (cell1 and cell4)
        Cell[] targetCells = {grid.getCells()[1][0], // In column 0, can see both cell1 and cell4
            grid.getCells()[2][0], // In column 0, can see both cell1 and cell4
            grid.getCells()[4][0], // In column 0, can see both cell1 and cell4
            grid.getCells()[5][0], // In column 0, can see both cell1 and cell4
            grid.getCells()[6][0], // In column 0, can see both cell1 and cell4
            grid.getCells()[7][0], // In column 0, can see both cell1 and cell4
            grid.getCells()[8][0]  // In column 0, can see both cell1 and cell4
        };

        // Apply the method
        new MethodXChain().apply(grid);

        // Verify that digit was eliminated from all target cells
        for (Cell targetCell : targetCells) {
            assertFalse(targetCell.isSupportCandidate(digit), "Digit " + digit + " should be eliminated from target cell at (" + targetCell.getRowId() + "," + targetCell.getColumnId() + ")");
        }
    }

    @Test
    public void testChainLength4SameRow() {
        // Test case for a chain of length 4 (Strong-Weak-Strong)
        // Chain: (0,0)-(3,0)-(3,3)-(0,3) for digit 5
        // This forms a rectangle pattern in the grid, rotated 90 degrees
        Grid grid = new Grid(Grid.EMPTY_GRID);
        int digit = 5;

        // Setup the chain cells
        Cell cell1 = grid.getCells()[0][0]; // Start cell
        Cell cell2 = grid.getCells()[3][0]; // Strong link
        Cell cell3 = grid.getCells()[3][3]; // Weak link
        Cell cell4 = grid.getCells()[0][3]; // Strong link

        setupStrongLink(grid, 0, 0, 3, 0, digit);
        // Weak link do not need setup
        setupStrongLink(grid, 3, 3, 0, 3, digit);

        // Setup target cells that should have the digit eliminated
        // Each target cell must be able to see both ends of the chain (cell1 and cell4)
        Cell[] targetCells = {grid.getCells()[0][1], // In row 0, can see both cell1 and cell4
            grid.getCells()[0][2], // In row 0, can see both cell1 and cell4
            grid.getCells()[0][4], // In row 0, can see both cell1 and cell4
            grid.getCells()[0][5], // In row 0, can see both cell1 and cell4
            grid.getCells()[0][6], // In row 0, can see both cell1 and cell4
            grid.getCells()[0][7], // In row 0, can see both cell1 and cell4
            grid.getCells()[0][8]  // In row 0, can see both cell1 and cell4
        };

        // Apply the method
        new MethodXChain().apply(grid);

        // Verify that digit was eliminated from all target cells
        for (Cell targetCell : targetCells) {
            assertFalse(targetCell.isSupportCandidate(digit), "Digit " + digit + " should be eliminated from target cell at (" + targetCell.getRowId() + "," + targetCell.getColumnId() + ")");
        }
    }

    @Test
    public void testChainLength4SameBlockSameColumn() {
        // Test case for a chain of length 4 (Strong-Weak-Strong)
        // Chain: (0,0)-(0,2)-(2,2)-(2,0) for digit 5
        // This forms a rectangle pattern within the top-left 3x3 block
        Grid grid = new Grid(Grid.EMPTY_GRID);
        int digit = 5;

        // Setup the chain cells
        Cell cell1 = grid.getCells()[0][0]; // Start cell
        Cell cell2 = grid.getCells()[0][2]; // Strong link
        Cell cell3 = grid.getCells()[2][2]; // Weak link
        Cell cell4 = grid.getCells()[2][0]; // Strong link

        setupStrongLink(grid, 0, 0, 0, 2, digit);
        // Weak link do not need setup
        setupStrongLink(grid, 2, 2, 2, 0, digit);

        // Setup target cells that should have the digit eliminated
        // Each target cell must be able to see both ends of the chain (cell1 and cell4)
        Cell[] targetCells = {grid.getCells()[0][1], // In row 0, can see both cell1 and cell4
            grid.getCells()[1][0], // In column 0, can see both cell1 and cell4
            grid.getCells()[1][1], // In block, can see both cell1 and cell4
            grid.getCells()[1][2], // In column 2, can see both cell1 and cell4
            grid.getCells()[2][1], // In row 2, can see both cell1 and cell4
            grid.getCells()[3][0], // In column 0, can see both cell1 and cell4
            grid.getCells()[4][0], // In column 0, can see both cell1 and cell4
            grid.getCells()[5][0], // In column 0, can see both cell1 and cell4
            grid.getCells()[6][0], // In column 0, can see both cell1 and cell4
            grid.getCells()[7][0], // In column 0, can see both cell1 and cell4
            grid.getCells()[8][0]  // In column 0, can see both cell1 and cell4
        };

        // Apply the method
        new MethodXChain().apply(grid);

        // Verify that digit was eliminated from all target cells
        for (Cell targetCell : targetCells) {
            assertFalse(targetCell.isSupportCandidate(digit), "Digit " + digit + " should be eliminated from target cell at (" + targetCell.getRowId() + "," + targetCell.getColumnId() + ")");
        }
    }

    @Test
    public void testChainLength6SameColumn() {
        // Test case for a chain of length 6 (Strong-Weak-Strong-Weak-Strong)
        // Chain: (0,0)-(0,3)-(3,3)-(3,6)-(6,6)-(6,0) for digit 5
        // This forms a zigzag pattern in the grid
        Grid grid = new Grid(Grid.EMPTY_GRID);
        int digit = 5;

        // Setup the chain cells
        Cell cell1 = grid.getCells()[0][0]; // Start cell
        Cell cell2 = grid.getCells()[0][3]; // Strong link
        Cell cell3 = grid.getCells()[3][3]; // Weak link
        Cell cell4 = grid.getCells()[3][6]; // Strong link
        Cell cell5 = grid.getCells()[6][6]; // Weak link
        Cell cell6 = grid.getCells()[6][0]; // Strong link

        setupStrongLink(grid, 0, 0, 0, 3, digit);
        // Weak link do not need setup
        setupStrongLink(grid, 3, 3, 3, 6, digit);
        // Weak link do not need setup
        setupStrongLink(grid, 6, 6, 6, 0, digit);

        // Setup target cells that should have the digit eliminated
        // Each target cell must be able to see both ends of the chain (cell1 and cell6)
        Cell[] targetCells = {grid.getCells()[1][0], // In column 0, can see both cell1 and cell6
            grid.getCells()[2][0], // In column 0, can see both cell1 and cell6
            grid.getCells()[4][0], // In column 0, can see both cell1 and cell6
            grid.getCells()[5][0], // In column 0, can see both cell1 and cell6
            grid.getCells()[7][0], // In column 0, can see both cell1 and cell6
            grid.getCells()[8][0]  // In column 0, can see both cell1 and cell6
        };

        // Apply the method
        new MethodXChain().apply(grid);

        // Verify that digit was eliminated from all target cells
        for (Cell targetCell : targetCells) {
            assertFalse(targetCell.isSupportCandidate(digit), "Digit " + digit + " should be eliminated from target cell at (" + targetCell.getRowId() + "," + targetCell.getColumnId() + ")");
        }
    }
}
