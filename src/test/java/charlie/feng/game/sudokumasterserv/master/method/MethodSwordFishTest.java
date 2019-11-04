/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Grid;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MethodSwordFishTest {

    @Test
    public void checkSwordFishRow() {

        // X:4 Y:5 Z;6
        Grid grid = new Grid(Grid.EMPTY_GRID);

        for (int col = 3; col < 9; col++) {
            grid.cells[0][col].removeDigitFromCandidate(4, "mock", null);
            grid.cells[1][col].removeDigitFromCandidate(4, "mock", null);
            grid.cells[2][col].removeDigitFromCandidate(4, "mock", null);
        }
        grid.cells[0][0].removeDigitFromCandidate(4, "mock", null);
        grid.cells[1][1].removeDigitFromCandidate(4, "mock", null);
        grid.cells[2][2].removeDigitFromCandidate(4, "mock", null);

        new MethodSwordFish().apply(grid);

        for (int row = 3; row < 9; row++) {
            Assertions.assertFalse(grid.cells[row][0].isSupportCandidate(4), "Star Cell candidates should removed digit 4");
            Assertions.assertFalse(grid.cells[row][1].isSupportCandidate(4), "Star Cell candidates should removed digit 4");
            Assertions.assertFalse(grid.cells[row][2].isSupportCandidate(4), "Star Cell candidates should removed digit 4");
        }
    }

    @Test
    public void checkSwordFishColumn() {

        // X:4 Y:5 Z;6
        Grid grid = new Grid(Grid.EMPTY_GRID);

        for (int row = 3; row < 9; row++) {
            grid.cells[row][0].removeDigitFromCandidate(5, "mock", null);
            grid.cells[row][0].removeDigitFromCandidate(6, "mock", null);
            grid.cells[row][1].removeDigitFromCandidate(5, "mock", null);
            grid.cells[row][1].removeDigitFromCandidate(6, "mock", null);
            grid.cells[row][2].removeDigitFromCandidate(5, "mock", null);
            grid.cells[row][2].removeDigitFromCandidate(6, "mock", null);
        }
        grid.cells[0][1].removeDigitFromCandidate(5, "mock", null);
        grid.cells[1][2].removeDigitFromCandidate(5, "mock", null);
        grid.cells[2][0].removeDigitFromCandidate(5, "mock", null);

        new MethodSwordFish().apply(grid);

        for (int col = 3; col < 9; col++) {
            Assertions.assertFalse(grid.cells[0][col].isSupportCandidate(5), "Star Cell candidates should removed digit 5");
            Assertions.assertFalse(grid.cells[0][col].isSupportCandidate(6), "Star Cell candidates should removed digit 6");
            Assertions.assertFalse(grid.cells[1][col].isSupportCandidate(5), "Star Cell candidates should removed digit 5");
            Assertions.assertFalse(grid.cells[1][col].isSupportCandidate(6), "Star Cell candidates should removed digit 6");
            Assertions.assertFalse(grid.cells[2][col].isSupportCandidate(5), "Star Cell candidates should removed digit 5");
            Assertions.assertFalse(grid.cells[2][col].isSupportCandidate(6), "Star Cell candidates should removed digit 6");
        }

    }
}