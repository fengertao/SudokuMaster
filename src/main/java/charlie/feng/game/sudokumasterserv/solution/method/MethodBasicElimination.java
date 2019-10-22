/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.solution.method;

import charlie.feng.game.sudokumasterserv.solution.*;

/**
 * 单元排除法
 * 在某一单元（即行，列或区块）中找到能填入某一数字的唯一位置，换句话说，就是把单元中其他的空白位置都排除掉。它对应于候选数法中的隐式唯一法。
 */
public class MethodBasicElimination implements IMethod {
    public void apply(Grid grid) {
        for (int i = 0; i < 9; i++) {
            Row row = grid.rows[i];
            checkBasicElimination(row);
            Column column = grid.columns[i];
            checkBasicElimination(column);
            Block block = grid.blocks[i];
            checkBasicElimination(block);
        }
    }

    private void checkBasicElimination(Region region) {
        for (int digit = 1; digit <= 9; digit++) {
            boolean alreadyFound = false;
            int cellsContainsCandidate = 0;
            Cell cellContainsCandidate = null;

            for (int i = 0; i < 9; i++) {
                if ((region.cells[i].getValue() != null) && (region.cells[i].getValue() == digit)) {
                    alreadyFound = true;
                    break;
                }
                if (region.cells[i].isSupportCandidate(digit)) {
                    cellsContainsCandidate++;
                    cellContainsCandidate = region.cells[i];
                }
            }
            if (alreadyFound)
                continue;
            if (cellsContainsCandidate == 1) {
                cellContainsCandidate.gainValue(digit, this.getClass().getSimpleName());

                //must return here because change always done, some key cell maybe changed.
                //                return;
            }
        }
    }
}
