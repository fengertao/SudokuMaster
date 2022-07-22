/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Block;
import charlie.feng.game.sudokumasterserv.master.Cell;
import charlie.feng.game.sudokumasterserv.master.Column;
import charlie.feng.game.sudokumasterserv.master.Grid;
import charlie.feng.game.sudokumasterserv.master.AbstractRegion;
import charlie.feng.game.sudokumasterserv.master.Row;

import java.util.ArrayList;

/**
 * 单元唯一法
 * 某单元的8个格子的数字已经确定了，则剩下格子的数字可以确定
 */
public class MethodSolePosition implements IMethod {
    @Override
    public void apply(Grid grid) {
        for (int i = 0; i < 9; i++) {
            Row row = grid.getRows()[i];
            checkSolePositionLeft(row);
            Column column = grid.getColumns()[i];
            checkSolePositionLeft(column);
            Block block = grid.getBlocks()[i];
            checkSolePositionLeft(block);
        }
    }

    private void checkSolePositionLeft(AbstractRegion region) {
        Cell leftCell = null;
        int counter = 0;
        int summary = 0;
        //Sum(1..9)=45
        int expectSummary = 45;
        for (int i = 0; i < 9; i++) {
            Cell cell = region.getCells()[i];
            if (cell.getValue() != null) {
                counter++;
                summary += cell.getValue();
            } else {
                leftCell = cell;
            }
        }
        if ((counter == 8) && (leftCell != null)) {
            leftCell.gainValue(expectSummary - summary, this.getClass().getSimpleName(), new ArrayList<>());
        }

    }
}
