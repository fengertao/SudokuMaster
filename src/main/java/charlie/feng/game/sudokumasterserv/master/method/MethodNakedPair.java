/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Cell;
import charlie.feng.game.sudokumasterserv.master.Grid;
import charlie.feng.game.sudokumasterserv.master.Region;
import com.google.common.collect.Lists;

/**
 * 显式数对法
 * 在一个行，列或区块中，如果有两个单元格都包含且只包含相同的两个候选数，
 * 则这两个候选数字不能再出现在该行，列或区块的其他单元格的候选数中。
 */
public class MethodNakedPair implements IMethod {

    public void apply(Grid grid) {
        for (int i = 0; i < 9; i++) {
            checkNakedPair(grid.rows[i]);
            checkNakedPair(grid.columns[i]);
            checkNakedPair(grid.blocks[i]);
        }
    }

    private void checkNakedPair(Region region) {
        Cell[] cells = region.cells;
        for (int i1 = 1; i1 < 9; i1++) {
            if (cells[i1].getValue() != null)
                continue;
            if (cells[i1].getNumberOfCandidates() != 2)
                continue;
            for (int i2 = 0; i2 < i1; i2++) {
                if (cells[i2].getValue() != null)
                    continue;
                if (cells[i2].getNumberOfCandidates() != 2)
                    continue;
                boolean match = true;
                for (int k = 1; k <= 9; k++) {
                    if (region.cells[i1].isSupportCandidate(k) != region.cells[i2].isSupportCandidate(k)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    for (int k = 1; k <= 9; k++) {
                        if (region.cells[i1].isSupportCandidate(k)) {
                            for (int otherCell = 0; otherCell < 9; otherCell++) {
                                if ((otherCell != i1) && (otherCell != i2)) {
                                    if ((region.cells[otherCell].getValue() == null) && (region.cells[otherCell].isSupportCandidate(k))) {
                                        region.cells[otherCell].removeDigitFromCandidate(k, this.getClass().getSimpleName(), Lists.newArrayList(cells[i1], cells[i2]));
                                        if (region.cells[otherCell].getNumberOfCandidates() == 1) {
                                            region.cells[otherCell].resolvedByNakedSingle(Lists.newArrayList(cells[i1], cells[i2]));
                                            //must return here because change always done, some key cell maybe changed.
                                            //                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
