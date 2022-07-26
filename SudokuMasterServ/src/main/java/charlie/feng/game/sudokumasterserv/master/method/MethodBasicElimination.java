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
 * 单元排除法
 * 在某一单元（即行，列或区块）中找到能填入某一数字的唯一位置，换句话说，就是把单元中其他的空白位置都排除掉。它对应于候选数法中的隐式唯一法。
 */
public class MethodBasicElimination implements IMethod {
    @Override
    public void apply(Grid grid) {
        for (int i = 0; i < 9; i++) {
            Row row = grid.getRows()[i];
            checkBasicElimination(row);
            Column column = grid.getColumns()[i];
            checkBasicElimination(column);
            Block block = grid.getBlocks()[i];
            checkBasicElimination(block);
        }
    }

    private void checkBasicElimination(AbstractRegion region) {
        for (int digit = 1; digit <= 9; digit++) {
            boolean alreadyFound = false;
            int cellsContainsCandidate = 0;
            Cell cellContainsCandidate = null;

            for (int i = 0; i < 9; i++) {
                if ((region.getCells()[i].getValue() != null) && (region.getCells()[i].getValue() == digit)) {
                    alreadyFound = true;
                    break;
                }
                if (region.getCells()[i].isSupportCandidate(digit)) {
                    cellsContainsCandidate++;
                    cellContainsCandidate = region.getCells()[i];
                }
            }
            if (alreadyFound) {
                continue;
            }
            if (cellsContainsCandidate == 1) {
                cellContainsCandidate.gainValue(digit, this.getClass().getSimpleName(), new ArrayList<>());

                //must return here because change always done, some key cell maybe changed.
                //                return;
            }
        }
    }
}
