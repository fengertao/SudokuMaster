/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Cell;
import charlie.feng.game.sudokumasterserv.master.Grid;
import charlie.feng.game.sudokumasterserv.master.AbstractRegion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 隐式四数集法
 * 在同一行，列或块中，如果四个数字正好只出现且都出现在四个单元格中，
 * 则这四个单元格的候选数中的其他数字可以被删除。
 */
public class MethodHiddenQuad implements IMethod {

    @Override
    public void apply(Grid grid) {
        for (int i = 0; i < 9; i++) {
            checkHiddenQuad(grid.getRows()[i]);
            checkHiddenQuad(grid.getColumns()[i]);
            checkHiddenQuad(grid.getBlocks()[i]);
        }
    }

    private void checkHiddenQuad(AbstractRegion region) {
        Cell[] cells = region.getCells();
        Set<Integer> k1PossibleOffsetSet;
        Set<Integer> k2PossibleOffsetSet;
        Set<Integer> k3PossibleOffsetSet;
        Set<Integer> k4PossibleOffsetSet;
        Set<Integer> mergedOffsetSet = new HashSet<>();

        for (int k1 = 4; k1 <= 9; k1++) {
            if (region.isDigitGained(k1)) {
                continue;
            }
            k1PossibleOffsetSet = region.getPossibleOffset(k1);
            if (k1PossibleOffsetSet.size() > 4) {
                continue;
            }
            for (int k2 = 3; k2 < k1; k2++) {
                if (region.isDigitGained(k2)) {
                    continue;
                }
                k2PossibleOffsetSet = region.getPossibleOffset(k2);
                if (k2PossibleOffsetSet.size() > 4) {
                    continue;
                }

                for (int k3 = 2; k3 < k2; k3++) {
                    if (region.isDigitGained(k3)) {
                        continue;
                    }
                    k3PossibleOffsetSet = region.getPossibleOffset(k3);
                    if (k3PossibleOffsetSet.size() > 4) {
                        continue;
                    }

                    for (int k4 = 1; k4 < k3; k4++) {
                        if (region.isDigitGained(k4)) {
                            continue;
                        }
                        k4PossibleOffsetSet = region.getPossibleOffset(k4);
                        if (k4PossibleOffsetSet.size() > 4) {
                            continue;
                        }

                        mergedOffsetSet.clear();
                        mergedOffsetSet.addAll(k1PossibleOffsetSet);
                        mergedOffsetSet.addAll(k2PossibleOffsetSet);
                        mergedOffsetSet.addAll(k3PossibleOffsetSet);
                        mergedOffsetSet.addAll(k4PossibleOffsetSet);

                        //Hidden Quad detected
                        if (mergedOffsetSet.size() == 4) {
                            for (Integer offset : mergedOffsetSet) {
                                for (int kOther = 1; kOther <= 9; kOther++) {
                                    if ((kOther == k1) || (kOther == k2) || (kOther == k3) || (kOther == k4)) {
                                        continue;
                                    }
                                    List<Integer> refCellsIndex = new ArrayList<>(mergedOffsetSet);
                                    refCellsIndex.remove(offset);
                                    List<Cell> refCells = refCellsIndex.stream().map(index -> cells[index]).collect(Collectors.toList());
                                    cells[offset].removeDigitFromCandidate(kOther, this.getClass().getSimpleName(), refCells);
                                    //must return here because change always done, some key cell maybe changed.
                                    //                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
