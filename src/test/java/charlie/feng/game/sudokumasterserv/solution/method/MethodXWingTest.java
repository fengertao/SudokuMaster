/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.solution.method;

import charlie.feng.game.sudokumasterserv.solution.Grid;
import org.junit.Assert;
import org.junit.Test;

public class MethodXWingTest {

    @Test
    public void checkXYWingRow() {

        // X:4 Y:6
        Grid grid = new Grid(Grid.EMPTY_GRID);

        for (int col = 2; col < 9; col++) {
            grid.cells[0][col].removeDigitFromCandidate(4, "mock");
            grid.cells[1][col].removeDigitFromCandidate(4, "mock");
        }
        new MethodXWing().apply(grid);

        for (int row = 2; row < 9; row++) {
            Assert.assertFalse("Star Cell candidates should removed digit 4", grid.cells[row][0].isSupportCandidate(4));
            Assert.assertFalse("Star Cell candidates should removed digit 4", grid.cells[row][1].isSupportCandidate(4));
        }
    }

    @Test
    public void checkXYWingColumn() {

        // X:4 Y:6
        Grid grid = new Grid(Grid.EMPTY_GRID);

        for (int row = 2; row < 9; row++) {
            grid.cells[row][0].removeDigitFromCandidate(6, "mock");
            grid.cells[row][1].removeDigitFromCandidate(6, "mock");
        }
        new MethodXWing().apply(grid);

        for (int col = 2; col < 9; col++) {
            Assert.assertFalse("Star Cell candidates should removed digit 6", grid.cells[0][col].isSupportCandidate(6));
            Assert.assertFalse("Star Cell candidates should removed digit 6", grid.cells[1][col].isSupportCandidate(6));
        }
    }

}