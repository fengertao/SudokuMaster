/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Grid;
import charlie.feng.game.sudokumasterserv.master.Region;

import java.util.ArrayList;

/**
 * 在某单元格，虽然它有多个候选数，但观察它所在的行、列或块，只有本单元格有这个候选数，即可确认本单元格
 */
public class MethodHiddenSingle implements IMethod {

    public void apply(Grid grid) {
        for (int i = 0; i < 9; i++) {
            checkHiddenSingle(grid.rows[i]);
            checkHiddenSingle(grid.columns[i]);
            checkHiddenSingle(grid.blocks[i]);
        }
    }

    private void checkHiddenSingle(Region region) {
        for (int digit = 1; digit <= 9; digit++) {
            if (region.isDigitGained(digit))
                continue;
            int numberOfOccurance = 0;
            int cellId = 0;
            for (int i = 0; i < 9; i++) {
                if (region.cells[i].isSupportCandidate(digit)) {
                    numberOfOccurance++;
                    cellId = i;
                }
            }
            if (numberOfOccurance == 1) {
                region.cells[cellId].gainValue(digit, this.getClass().getSimpleName(), new ArrayList<>());
            }
        }

    }
}
