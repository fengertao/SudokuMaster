/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Cell;
import charlie.feng.game.sudokumasterserv.master.Grid;
import charlie.feng.game.sudokumasterserv.master.AbstractRegion;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 显式三数集法
 * 必须要有3个在同一行，列或块中的单元格，每个单元格中至少要有2个候选数，且它们的所有候选数字也正好都是一个三数集的子集。
 * 由于这个三数集中的3个数字正好可以分别填入这3个单元格中，所以该行，列或区块中其他的单元格中不可能再填入这3个数字。
 */
public class MethodNakedTriplet implements IMethod {

    @Override
    public void apply(Grid grid) {
        for (int i = 0; i < 9; i++) {
            checkNakedTriplet(grid.getRows()[i]);
            checkNakedTriplet(grid.getColumns()[i]);
            checkNakedTriplet(grid.getBlocks()[i]);
        }
    }

    private void checkNakedTriplet(AbstractRegion region) {
        Cell[] cells = region.getCells();
        Set<Integer> supportNumberSet = new HashSet<>();
        for (int i1 = 2; i1 < 9; i1++) {
            if (cells[i1].getValue() != null) {
                continue;
            }
            for (int i2 = 1; i2 < i1; i2++) {
                if (cells[i2].getValue() != null) {
                    continue;
                }
                for (int i3 = 0; i3 < i2; i3++) {
                    if (cells[i3].getValue() != null) {
                        continue;
                    }
                    supportNumberSet.clear();
                    for (int k = 1; k <= 9; k++) {
                        if (cells[i1].isSupportCandidate(k) || cells[i2].isSupportCandidate(k) || cells[i3].isSupportCandidate(k)) {
                            supportNumberSet.add(k);
                        }
                    }

                    if (supportNumberSet.size() <= 3) {
                        Cell[] excludeCells = new Cell[3];
                        excludeCells[0] = cells[i1];
                        excludeCells[1] = cells[i2];
                        excludeCells[2] = cells[i3];

                        for (Integer hiddenNumber : supportNumberSet) {
                            region.removeDigit(hiddenNumber, excludeCells, Arrays.asList(cells[i1], cells[i2], cells[i3]));
                            //must return here because change always done, some key cell maybe changed.
                            //                            return;
                        }
                    }
                }
            }
        }
    }

}
