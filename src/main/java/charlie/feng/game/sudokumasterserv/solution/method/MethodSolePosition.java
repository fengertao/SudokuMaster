/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.solution.method;

import charlie.feng.game.sudokumasterserv.solution.*;

/**
 * 单元唯一法
 * 某单元的8个格子的数字已经确定了，则剩下格子的数字可以确定
 */
public class MethodSolePosition implements IMethod {
    public void apply(Grid grid) {
        for (int i = 0; i < 9; i++) {
            Row row = grid.rows[i];
            checkSolePositionLeft(row);
            Column column = grid.columns[i];
            checkSolePositionLeft(column);
            Block block = grid.blocks[i];
            checkSolePositionLeft(block);
        }
    }

    private void checkSolePositionLeft(Region region) {
        Cell leftCell = null;
        int counter = 0;
        int summary = 0;
        int expectSummary = 45;  //Sum(1..9)=45
        for (int i = 0; i < 9; i++) {
            Cell cell = region.cells[i];
            if (cell.getValue() != null) {
                counter++;
                summary += cell.getValue();
            } else {
                leftCell = cell;
            }
        }
        if ((counter == 8) && (leftCell != null)) {
            leftCell.gainValue(expectSummary - summary, this.getClass().getSimpleName());
        }

    }
}
