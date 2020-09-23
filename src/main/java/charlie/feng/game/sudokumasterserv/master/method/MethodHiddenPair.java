/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Cell;
import charlie.feng.game.sudokumasterserv.master.Grid;
import charlie.feng.game.sudokumasterserv.master.AbstractRegion;

import java.util.Collections;
import java.util.Set;

/**
 * 隐式数对法
 * 在同一行，列或块中，如果一个数对（两个数字）正好只出现且都出现在两个单元格中，
 * 则这两个单元格的候选数中的其他数字可以被删除。
 * The position of those 2 hidden cell not as strict as "BiHiddenCellExclude".
 * In the latter case, those cells must be in same line or same column.
 * while in this method, in same block is OK.
 * And the usage are different. in the latter case, it is used to exclude those numbers from other cells.
 * while in this method, it is used to exclude other numbers for those cells.
 */
public class MethodHiddenPair implements IMethod {

    @Override
    public void apply(Grid grid) {
        for (int i = 0; i < 9; i++) {
            checkHiddenPair(grid.getRows()[i]);
            checkHiddenPair(grid.getColumns()[i]);
            checkHiddenPair(grid.getBlocks()[i]);
        }
    }

    private void checkHiddenPair(AbstractRegion region) {
        Cell[] cells = region.getCells();
        Set<Integer> k1PossibleOffsetSet;
        Set<Integer> k2PossibleOffsetSet;
        for (int k1 = 2; k1 <= 9; k1++) {
            if (region.isDigitGained(k1)) {
                continue;
            }
            k1PossibleOffsetSet = region.getPossibleOffset(k1);
            if (k1PossibleOffsetSet.size() != 2) {
                continue;
            }
            for (int k2 = 1; k2 < k1; k2++) {
                if (region.isDigitGained(k2)) {
                    continue;
                }
                k2PossibleOffsetSet = region.getPossibleOffset(k2);
                if (k2PossibleOffsetSet.size() != 2) {
                    continue;
                }

                // Hidden Pair Detected
                if (k1PossibleOffsetSet.equals(k2PossibleOffsetSet)) {
                    for (Integer offset : k1PossibleOffsetSet) {
                        for (int kOther = 1; kOther <= 9; kOther++) {
                            if ((kOther == k1) || (kOther == k2)) {
                                continue;
                            }
                            int refIndex = k2PossibleOffsetSet.stream().reduce(Integer::sum).get() - offset;
                            cells[offset].removeDigitFromCandidate(kOther, this.getClass().getSimpleName(), Collections.singletonList(cells[refIndex]));
                            //must return here because change always done, some key cell maybe changed.
                            //                            return;
                        }
                    }
                }
            }
        }

    }

}
