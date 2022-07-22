/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Cell;
import charlie.feng.game.sudokumasterserv.master.Grid;

/**
 * 直接排除法
 * 当我们知道某单元格有某数，同行、列、块不能有同样数字
 */
//Todo whether this method should be before first cycle only?
public class MethodDirectExclusive implements IMethod {
    @Override
    public void apply(Grid grid) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Cell cell = grid.getCells()[r][c];
                if (cell.getValue() != null) {
                    cell.noticeNeighboorsAboutValueGained();
                }
            }
        }
    }

}
