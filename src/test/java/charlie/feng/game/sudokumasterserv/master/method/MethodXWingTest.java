/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Grid;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MethodXWingTest {

    @Test
    public void checkXYWingRow() {

        // X:4 Y:6
        Grid grid = new Grid(Grid.EMPTY_GRID);

        for (int col = 2; col < 9; col++) {
            grid.getCells()[0][col].removeDigitFromCandidate(4, "mock", null);
            grid.getCells()[1][col].removeDigitFromCandidate(4, "mock", null);
        }
        new MethodXWing().apply(grid);

        for (int row = 2; row < 9; row++) {
            Assertions.assertFalse(grid.getCells()[row][0].isSupportCandidate(4), "Star Cell candidates should removed digit 4");
            Assertions.assertFalse(grid.getCells()[row][1].isSupportCandidate(4), "Star Cell candidates should removed digit 4");
        }
    }

    @Test
    public void checkXYWingColumn() {

        // X:4 Y:6
        Grid grid = new Grid(Grid.EMPTY_GRID);

        for (int row = 2; row < 9; row++) {
            grid.getCells()[row][0].removeDigitFromCandidate(6, "mock", null);
            grid.getCells()[row][1].removeDigitFromCandidate(6, "mock", null);
        }
        new MethodXWing().apply(grid);

        for (int col = 2; col < 9; col++) {
            Assertions.assertFalse(grid.getCells()[0][col].isSupportCandidate(6), "Star Cell candidates should removed digit 6");
            Assertions.assertFalse(grid.getCells()[1][col].isSupportCandidate(6), "Star Cell candidates should removed digit 6");
        }
    }

}
