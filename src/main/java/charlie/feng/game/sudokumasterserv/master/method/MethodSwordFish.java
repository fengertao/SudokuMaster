/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Cell;
import charlie.feng.game.sudokumasterserv.master.Grid;
import charlie.feng.game.sudokumasterserv.master.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 三链数删减法 (Swordfish)
 * 如果某个数字在某三列中只出现在相同的三行中，则这个数字将从这三行上其他的候选数中删除。
 * 如果某个数字在某三行中只出现在相同的三列中，则这个数字也将从这三列上其他的候选数中删除
 */
public class MethodSwordFish implements IMethod {

    public void apply(Grid grid) {
        for (int k = 1; k <= 9; k++) {
            checkSwordFish(grid, k, true);
            checkSwordFish(grid, k, false);
        }
    }

    private void checkSwordFish(Grid grid, int value, boolean isRow) {
        Set<Integer> possibleOffsetSet1;
        Set<Integer> possibleOffsetSet2;
        Set<Integer> possibleOffsetSet3;

        for (int i1 = 2; i1 < 9; i1++) {
            Region region1 = (isRow ? grid.getRows()[i1] : grid.getColumns()[i1]);
            if (region1.isDigitGained(value)) {
                continue;
            }
            possibleOffsetSet1 = region1.getPossibleOffset(value);
            if (possibleOffsetSet1.size() != 2 && possibleOffsetSet1.size() != 3) {
                continue;
            }
            for (int i2 = 1; i2 < i1; i2++) {
                Region region2 = (isRow ? grid.getRows()[i2] : grid.getColumns()[i2]);
                if (region2.isDigitGained(value)) {
                    continue;
                }
                possibleOffsetSet2 = region2.getPossibleOffset(value);
                if (possibleOffsetSet2.size() != 2 && possibleOffsetSet2.size() != 3) {
                    continue;
                }
                possibleOffsetSet2.addAll(possibleOffsetSet1);
                if (possibleOffsetSet2.size() > 3) {
                    continue;
                }
                for (int i3 = 0; i3 < i2; i3++) {
                    Region region3 = (isRow ? grid.getRows()[i3] : grid.getColumns()[i3]);
                    if (region3.isDigitGained(value)) {
                        continue;
                    }
                    possibleOffsetSet3 = region3.getPossibleOffset(value);
                    if (possibleOffsetSet3.size() != 2 && possibleOffsetSet3.size() != 3) {
                        continue;
                    }
                    possibleOffsetSet3.addAll(possibleOffsetSet2);
                    if (possibleOffsetSet3.size() != 3) {
                        continue;
                    }

                    //SwordFish detected, start clear
                    Integer[] offsetArray = possibleOffsetSet3.toArray(new Integer[3]);

                    //Todo Test row and col, and used in other method
                    List<Cell> refCells = new ArrayList<>();
                    for (Integer offset : possibleOffsetSet3) {
                        if (isRow) {
                            refCells.add(grid.getCells()[i1][offset]);
                            refCells.add(grid.getCells()[i2][offset]);
                            refCells.add(grid.getCells()[i3][offset]);
                        } else {
                            refCells.add(grid.getCells()[offset][i1]);
                            refCells.add(grid.getCells()[offset][i2]);
                            refCells.add(grid.getCells()[offset][i3]);
                        }
                    }

                    for (int iOther = 0; iOther < 9; iOther++) {
                        if ((iOther == i1) || (iOther == i2) || (iOther == i3)) {
                            continue;
                        }
                        Region regionOther = (isRow ? grid.getRows()[iOther] : grid.getColumns()[iOther]);
                        regionOther.getCells()[offsetArray[0]].removeDigitFromCandidate(value, this.getClass().getSimpleName(), refCells);
                        regionOther.getCells()[offsetArray[1]].removeDigitFromCandidate(value, this.getClass().getSimpleName(), refCells);
                        regionOther.getCells()[offsetArray[2]].removeDigitFromCandidate(value, this.getClass().getSimpleName(), refCells);
                    }
                }
            }
        }
    }
}
