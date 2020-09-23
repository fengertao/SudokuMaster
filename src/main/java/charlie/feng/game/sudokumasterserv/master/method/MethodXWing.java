/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Cell;
import charlie.feng.game.sudokumasterserv.master.Grid;
import charlie.feng.game.sudokumasterserv.master.AbstractRegion;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 矩形对角线法 (X-wing)
 * 如果一个数字正好出现且只出现在某两行的相同的两列上，则这个数字就可以从这两列上其他的单元格的候选数中删除。
 * 如果一个数字正好出现且只出现在某两列的相同的两行上，则这个数字就可以从这两行上的其他单元格的候选数中删除
 * 又名矩形排除法? (Rectangle Elimination Technique)
 * 如果一个数字在某两行中能填入的位置正好在同样的两列中，则这两列的其他的单元格中将不可能再出现这个数字；
 * 如果一个数字在某两列中能填入的位置正好在同样的两行中，则这两行的其他的单元格中将不可能再出现这个数字。
 */

public class MethodXWing implements IMethod {

    @Override
    public void apply(Grid grid) {
        for (int k = 1; k <= 9; k++) {
            checkXWing(grid, k, true);
            checkXWing(grid, k, false);
        }
    }

    private void checkXWing(Grid grid, int value, boolean isRow) {
        Set<Integer> possibleOffsetSet1;
        Set<Integer> possibleOffsetSet2;

        for (int i1 = 1; i1 < 9; i1++) {
            AbstractRegion region1 = (isRow ? grid.getRows()[i1] : grid.getColumns()[i1]);
            if (region1.isDigitGained(value)) {
                continue;
            }
            possibleOffsetSet1 = region1.getPossibleOffset(value);
            if (possibleOffsetSet1.size() != 2) {
                continue;
            }
            for (int i2 = 0; i2 < i1; i2++) {
                AbstractRegion region2 = (isRow ? grid.getRows()[i2] : grid.getColumns()[i2]);
                if (region2.isDigitGained(value)) {
                    continue;
                }
                possibleOffsetSet2 = region2.getPossibleOffset(value);
                if (possibleOffsetSet2.size() != 2) {
                    continue;
                }
                if (!possibleOffsetSet1.equals(possibleOffsetSet2)) {
                    continue;
                }
                //X-Wing detected, start clear
                Integer[] offsetArray = possibleOffsetSet1.toArray(new Integer[2]);
                List<Cell> refCells = new ArrayList<>();
                for (Integer offset : offsetArray) {
                    if (isRow) {
                        refCells.add(grid.getCells()[i1][offset]);
                        refCells.add(grid.getCells()[i2][offset]);
                    } else {
                        refCells.add(grid.getCells()[offset][i1]);
                        refCells.add(grid.getCells()[offset][i2]);
                    }
                }

                for (int iOther = 0; iOther < 9; iOther++) {
                    if ((iOther == i1) || (iOther == i2)) {
                        continue;
                    }
                    AbstractRegion regionOther = (isRow ? grid.getRows()[iOther] : grid.getColumns()[iOther]);
                    regionOther.getCells()[offsetArray[0]].removeDigitFromCandidate(value, this.getClass().getSimpleName(), refCells);
                    regionOther.getCells()[offsetArray[1]].removeDigitFromCandidate(value, this.getClass().getSimpleName(), refCells);
                }
            }
        }
    }
}
