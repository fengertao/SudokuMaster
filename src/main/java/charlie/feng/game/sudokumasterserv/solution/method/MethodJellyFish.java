/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.solution.method;

import charlie.feng.game.sudokumasterserv.solution.Grid;
import charlie.feng.game.sudokumasterserv.solution.Region;

import java.util.Set;

/**
 * 四链数删减法 (Jellyfish)
 * 与Swordfish类似，只是再进一步扩展到四行、四列。
 */
public class MethodJellyFish implements IMethod {

    public void apply(Grid grid) {
        for (int k = 1; k <= 9; k++) {
            checkJellyFish(grid, k, true);
            checkJellyFish(grid, k, false);
        }
    }

    private void checkJellyFish(Grid grid, int value, boolean isRow) {
        Set<Integer> possibleOffsetSet1;
        Set<Integer> possibleOffsetSet2;
        Set<Integer> possibleOffsetSet3;
        Set<Integer> possibleOffsetSet4;

        for (int i1 = 3; i1 < 9; i1++) {
            Region region1 = (isRow ? grid.rows[i1] : grid.columns[i1]);
            if (region1.isDigitGained(value))
                continue;
            possibleOffsetSet1 = region1.getPossibleOffset(value);
            if (possibleOffsetSet1.size() < 2 || possibleOffsetSet1.size() > 4)
                continue;
            for (int i2 = 2; i2 < i1; i2++) {
                Region region2 = (isRow ? grid.rows[i2] : grid.columns[i2]);
                if (region2.isDigitGained(value))
                    continue;
                possibleOffsetSet2 = region2.getPossibleOffset(value);
                if (possibleOffsetSet2.size() < 2 || possibleOffsetSet2.size() > 4)
                    continue;
                possibleOffsetSet2.addAll(possibleOffsetSet1);
                if (possibleOffsetSet2.size() > 4)
                    continue;
                for (int i3 = 1; i3 < i2; i3++) {
                    Region region3 = (isRow ? grid.rows[i3] : grid.columns[i3]);
                    if (region3.isDigitGained(value))
                        continue;
                    possibleOffsetSet3 = region3.getPossibleOffset(value);
                    if (possibleOffsetSet3.size() < 2 || possibleOffsetSet3.size() > 4)
                        continue;
                    possibleOffsetSet3.addAll(possibleOffsetSet2);
                    if (possibleOffsetSet3.size() > 4)
                        continue;
                    for (int i4 = 0; i4 < i3; i4++) {
                        Region region4 = (isRow ? grid.rows[i4] : grid.columns[i4]);
                        if (region4.isDigitGained(value))
                            continue;
                        possibleOffsetSet4 = region4.getPossibleOffset(value);
                        if (possibleOffsetSet4.size() < 2 || possibleOffsetSet4.size() > 4)
                            continue;
                        possibleOffsetSet4.addAll(possibleOffsetSet3);
                        if (possibleOffsetSet4.size() != 4)
                            continue;

                        //JellyFish detected, start clear
                        Integer[] offsetArray = possibleOffsetSet4.toArray(new Integer[4]);
                        for (int iOther = 0; iOther < 9; iOther++) {
                            if ((iOther == i1) || (iOther == i2) || (iOther == i3) || (iOther == i4))
                                continue;
                            Region regionOther = (isRow ? grid.rows[iOther] : grid.columns[iOther]);
                            regionOther.cells[offsetArray[0]].removeDigitFromCandidate(value, this.getClass().getSimpleName());
                            regionOther.cells[offsetArray[1]].removeDigitFromCandidate(value, this.getClass().getSimpleName());
                            regionOther.cells[offsetArray[2]].removeDigitFromCandidate(value, this.getClass().getSimpleName());
                            regionOther.cells[offsetArray[3]].removeDigitFromCandidate(value, this.getClass().getSimpleName());
                        }
                    }
                }
            }
        }
    }
}