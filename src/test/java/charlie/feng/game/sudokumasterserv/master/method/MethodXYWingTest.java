/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Cell;
import charlie.feng.game.sudokumasterserv.master.Grid;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class MethodXYWingTest {

    @Test
    public void checkXYWingRowColumn() {

        // X:1 Y:2 Z:3
        Grid grid = new Grid(Grid.EMPTY_GRID);
        Cell cellA = grid.getCells()[1][1];
        Cell cellB = grid.getCells()[1][4];
        Cell cellC = grid.getCells()[4][1];
        cellA.removeDigitFromCandidate(3, "Mock", null);
        cellA.removeDigitFromCandidate(4, "Mock", null);
        cellA.removeDigitFromCandidate(5, "Mock", null);
        cellA.removeDigitFromCandidate(6, "Mock", null);
        cellA.removeDigitFromCandidate(7, "Mock", null);
        cellA.removeDigitFromCandidate(8, "Mock", null);
        cellA.removeDigitFromCandidate(9, "Mock", null);

        cellB.removeDigitFromCandidate(2, "Mock", null);
        cellB.removeDigitFromCandidate(4, "Mock", null);
        cellB.removeDigitFromCandidate(5, "Mock", null);
        cellB.removeDigitFromCandidate(6, "Mock", null);
        cellB.removeDigitFromCandidate(7, "Mock", null);
        cellB.removeDigitFromCandidate(8, "Mock", null);
        cellB.removeDigitFromCandidate(9, "Mock", null);

        cellC.removeDigitFromCandidate(1, "Mock", null);
        cellC.removeDigitFromCandidate(4, "Mock", null);
        cellC.removeDigitFromCandidate(5, "Mock", null);
        cellC.removeDigitFromCandidate(6, "Mock", null);
        cellC.removeDigitFromCandidate(7, "Mock", null);
        cellC.removeDigitFromCandidate(8, "Mock", null);
        cellC.removeDigitFromCandidate(9, "Mock", null);

        new MethodXYWing().apply(grid);

        Cell cellStar = grid.getCells()[4][4];

        Assertions.assertFalse(cellStar.isSupportCandidate(3), "Star Cell candidates should removed digit 3");
        Assertions.assertTrue(cellStar.isSupportCandidate(1), "Star Cell candidates should keep 1");
        Assertions.assertTrue(cellStar.isSupportCandidate(2), "Star Cell candidates should keep 2");
        Assertions.assertTrue(cellStar.isSupportCandidate(4), "Star Cell candidates should keep 4");
        Assertions.assertTrue(cellStar.isSupportCandidate(5), "Star Cell candidates should keep 5");
        Assertions.assertTrue(cellStar.isSupportCandidate(6), "Star Cell candidates should keep 6");
        Assertions.assertTrue(cellStar.isSupportCandidate(7), "Star Cell candidates should keep 7");
        Assertions.assertTrue(cellStar.isSupportCandidate(8), "Star Cell candidates should keep 8");
        Assertions.assertTrue(cellStar.isSupportCandidate(9), "Star Cell candidates should keep 9");
    }

    @Test
    public void checkXYWingRowBlockABSameRow() {

        // X:4 Y:5 Z:6
        Grid grid = new Grid(Grid.EMPTY_GRID);
        Cell cellA = grid.getCells()[0][1];
        Cell cellB = grid.getCells()[0][4];
        Cell cellC = grid.getCells()[2][0];
        cellA.removeDigitFromCandidate(1, "Mock", null);
        cellA.removeDigitFromCandidate(2, "Mock", null);
        cellA.removeDigitFromCandidate(3, "Mock", null);
        cellA.removeDigitFromCandidate(6, "Mock", null);
        cellA.removeDigitFromCandidate(7, "Mock", null);
        cellA.removeDigitFromCandidate(8, "Mock", null);
        cellA.removeDigitFromCandidate(9, "Mock", null);

        cellB.removeDigitFromCandidate(1, "Mock", null);
        cellB.removeDigitFromCandidate(2, "Mock", null);
        cellB.removeDigitFromCandidate(3, "Mock", null);
        cellB.removeDigitFromCandidate(5, "Mock", null);
        cellB.removeDigitFromCandidate(7, "Mock", null);
        cellB.removeDigitFromCandidate(8, "Mock", null);
        cellB.removeDigitFromCandidate(9, "Mock", null);

        cellC.removeDigitFromCandidate(1, "Mock", null);
        cellC.removeDigitFromCandidate(2, "Mock", null);
        cellC.removeDigitFromCandidate(3, "Mock", null);
        cellC.removeDigitFromCandidate(4, "Mock", null);
        cellC.removeDigitFromCandidate(7, "Mock", null);
        cellC.removeDigitFromCandidate(8, "Mock", null);
        cellC.removeDigitFromCandidate(9, "Mock", null);

        new MethodXYWing().apply(grid);

        List<Cell> cellStars = new ArrayList<>(5);
        cellStars.add(grid.getCells()[0][0]);
        cellStars.add(grid.getCells()[0][2]);
        cellStars.add(grid.getCells()[2][3]);
        cellStars.add(grid.getCells()[2][4]);
        cellStars.add(grid.getCells()[2][5]);

        for (Cell cellStar : cellStars) {
            Assertions.assertFalse(cellStar.isSupportCandidate(6), "Star Cell candidates should removed digit 6");
            Assertions.assertTrue(cellStar.isSupportCandidate(1), "Star Cell candidates should keep 1");
            Assertions.assertTrue(cellStar.isSupportCandidate(2), "Star Cell candidates should keep 2");
            Assertions.assertTrue(cellStar.isSupportCandidate(3), "Star Cell candidates should keep 3");
            Assertions.assertTrue(cellStar.isSupportCandidate(4), "Star Cell candidates should keep 4");
            Assertions.assertTrue(cellStar.isSupportCandidate(5), "Star Cell candidates should keep 5");
            Assertions.assertTrue(cellStar.isSupportCandidate(7), "Star Cell candidates should keep 7");
            Assertions.assertTrue(cellStar.isSupportCandidate(8), "Star Cell candidates should keep 8");
            Assertions.assertTrue(cellStar.isSupportCandidate(9), "Star Cell candidates should keep 9");
        }

    }

    @Test
    public void checkXYWingRowBlockABSameCol() {

        // X:7 Y:8 Z:9
        Grid grid = new Grid(Grid.EMPTY_GRID);
        Cell cellA = grid.getCells()[2][2];
        Cell cellB = grid.getCells()[3][2];
        Cell cellC = grid.getCells()[1][0];
        cellA.removeDigitFromCandidate(1, "Mock", null);
        cellA.removeDigitFromCandidate(2, "Mock", null);
        cellA.removeDigitFromCandidate(3, "Mock", null);
        cellA.removeDigitFromCandidate(4, "Mock", null);
        cellA.removeDigitFromCandidate(5, "Mock", null);
        cellA.removeDigitFromCandidate(6, "Mock", null);
        cellA.removeDigitFromCandidate(9, "Mock", null);

        cellB.removeDigitFromCandidate(1, "Mock", null);
        cellB.removeDigitFromCandidate(2, "Mock", null);
        cellB.removeDigitFromCandidate(3, "Mock", null);
        cellB.removeDigitFromCandidate(4, "Mock", null);
        cellB.removeDigitFromCandidate(5, "Mock", null);
        cellB.removeDigitFromCandidate(6, "Mock", null);
        cellB.removeDigitFromCandidate(8, "Mock", null);

        cellC.removeDigitFromCandidate(1, "Mock", null);
        cellC.removeDigitFromCandidate(2, "Mock", null);
        cellC.removeDigitFromCandidate(3, "Mock", null);
        cellC.removeDigitFromCandidate(4, "Mock", null);
        cellC.removeDigitFromCandidate(5, "Mock", null);
        cellC.removeDigitFromCandidate(6, "Mock", null);
        cellC.removeDigitFromCandidate(7, "Mock", null);

        new MethodXYWing().apply(grid);

        List<Cell> cellStars = new ArrayList<>(5);
        cellStars.add(grid.getCells()[0][2]);
        cellStars.add(grid.getCells()[1][2]);
        cellStars.add(grid.getCells()[3][0]);
        cellStars.add(grid.getCells()[4][0]);
        cellStars.add(grid.getCells()[5][0]);

        for (Cell cellStar : cellStars) {
            Assertions.assertFalse(cellStar.isSupportCandidate(9), "Star Cell candidates should removed digit 9");
            Assertions.assertTrue(cellStar.isSupportCandidate(1), "Star Cell candidates should keep 1");
            Assertions.assertTrue(cellStar.isSupportCandidate(2), "Star Cell candidates should keep 2");
            Assertions.assertTrue(cellStar.isSupportCandidate(3), "Star Cell candidates should keep 3");
            Assertions.assertTrue(cellStar.isSupportCandidate(4), "Star Cell candidates should keep 4");
            Assertions.assertTrue(cellStar.isSupportCandidate(5), "Star Cell candidates should keep 5");
            Assertions.assertTrue(cellStar.isSupportCandidate(6), "Star Cell candidates should keep 6");
            Assertions.assertTrue(cellStar.isSupportCandidate(7), "Star Cell candidates should keep 7");
            Assertions.assertTrue(cellStar.isSupportCandidate(8), "Star Cell candidates should keep 8");
        }
    }
}
