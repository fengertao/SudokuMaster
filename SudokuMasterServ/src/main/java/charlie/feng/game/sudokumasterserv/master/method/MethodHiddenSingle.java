/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Grid;
import charlie.feng.game.sudokumasterserv.master.AbstractRegion;

import java.util.ArrayList;

/**
 * 在某单元格，虽然它有多个候选数，但观察它所在的行、列或块，只有本单元格有这个候选数，即可确认本单元格
 */
public class MethodHiddenSingle implements IMethod {

    @Override
    public void apply(Grid grid) {
        for (int i = 0; i < 9; i++) {
            checkHiddenSingle(grid.getRows()[i]);
            checkHiddenSingle(grid.getColumns()[i]);
            checkHiddenSingle(grid.getBlocks()[i]);
        }
    }

    private void checkHiddenSingle(AbstractRegion region) {
        for (int digit = 1; digit <= 9; digit++) {
            if (region.isDigitGained(digit)) {
                continue;
            }
            int numberOfOccurance = 0;
            int cellId = 0;
            for (int i = 0; i < 9; i++) {
                if (region.getCells()[i].isSupportCandidate(digit)) {
                    numberOfOccurance++;
                    cellId = i;
                }
            }
            if (numberOfOccurance == 1) {
                region.getCells()[cellId].gainValue(digit, this.getClass().getSimpleName(), new ArrayList<>());
            }
        }

    }
}
