/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Cell;
import charlie.feng.game.sudokumasterserv.master.Grid;

import java.util.ArrayList;

/**
 * 显式唯一法
 * 扫描候选数栅格表，如果哪个单元格中只剩下一个候选数，就可应用显式唯一法
 * 唯一余数法为显式唯一法的一种简单形式
 * 如果某一单元格所在的行，列及区块中共出现了8个不同的数字，那么该单元格可以确定地填入还未出现过的数字
 */
public class MethodNakedSingle implements IMethod {

    @Override
    public void apply(Grid grid) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Cell cell = grid.getCells()[r][c];
                if (cell.getNumberOfCandidates() == 1) {
                    cell.resolvedByNakedSingle(new ArrayList<>());
                }
            }
        }
    }
}
