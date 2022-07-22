/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Cell;
import charlie.feng.game.sudokumasterserv.master.Grid;
import charlie.feng.game.sudokumasterserv.master.AbstractRegion;
import org.paukov.combinatorics3.Generator;

import java.util.ArrayList;
import java.util.HashSet;
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

    protected int getNumberOfRegions() {
        return 2;
    }

    @Override
    public void apply(Grid grid) {
        for (int k = 1; k <= 9; k++) {
            checkInGrid(grid, k, true);
            checkInGrid(grid, k, false);
        }
    }

    private void checkInGrid(Grid grid, int value, boolean isRow) {

        Generator.combination(0, 1, 2, 3, 4, 5, 6, 7, 8)
                .simple(getNumberOfRegions())
                .stream()
                .forEach(regionIds -> checkInRegion(regionIds, grid, value, isRow));
    }

    private void checkInRegion(List<Integer> regionIds, Grid grid, int value, boolean isRow) {
        Set<Integer> possibleOffsetSet = new HashSet<>();
        int numberOfRegions = regionIds.size();
        for (int i : regionIds) {
            AbstractRegion region = (isRow ? grid.getRows()[i] : grid.getColumns()[i]);
            if (region.isDigitGained(value)) {
                return;
            }
            Set<Integer> iPossibleOffsetSet = region.getPossibleOffset(value);
            possibleOffsetSet.addAll(iPossibleOffsetSet);
            if (possibleOffsetSet.size() > numberOfRegions) {
                return;
            }
        }
        //X-Wing detected, start clear
        Integer[] offsetArray = possibleOffsetSet.toArray(new Integer[0]);
        List<Cell> refCells = new ArrayList<>();
        for (Integer offset : offsetArray) {
            if (isRow) {
                for (int i : regionIds) {
                    refCells.add(grid.getCells()[i][offset]);
                }
            } else {
                for (int i : regionIds) {
                    refCells.add(grid.getCells()[offset][i]);
                }
            }
        }
        for (int iOther = 0; iOther < 9; iOther++) {
            if (regionIds.contains(iOther)) {
                continue;
            }
            AbstractRegion regionOther = (isRow ? grid.getRows()[iOther] : grid.getColumns()[iOther]);
            for (int offset : offsetArray) {
                regionOther.getCells()[offset].removeDigitFromCandidate(value, this.getClass().getSimpleName(), refCells);
            }
        }
    }
}
