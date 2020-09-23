/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Block;
import charlie.feng.game.sudokumasterserv.master.Grid;
import charlie.feng.game.sudokumasterserv.master.AbstractRegion;

import java.util.Arrays;

/**
 * 区块排除法
 * 又名 双隐格排除法 三隐格排除法 区块删减法 (Intersection Removal)
 * 当某数字在某个区块中可填入的位置正好都在同一行上，因为该区块中必须要有该数字，所以这一行中不在该区块内的单元格上将不能再出现该数字。
 * 当某数字在某个区块中可填入的位置正好都在同一列上，因为该区块中必须要有该数字，所以这一列中不在该区块内的单元格上将不能再出现该数字。
 * 当某数字在某行中可填入的位置正好都在同一区块上，因为该行中必须要有该数字，所以该区块中不在该行内的单元格上将不能再出现该数字。
 * 当某数字在某列中可填入的位置正好都在同一区块上，因为该列中必须要有该数字，所以该区块中不在该列内的单元格上将不能再出现该数字。
 */
public class MethodBlockElimination implements IMethod {

    @Override
    public void apply(Grid grid) {
        for (int i = 0; i < 9; i++) {
            checkHiddenCellInRowColumn(grid, grid.getRows()[i], true);
            checkHiddenCellInRowColumn(grid, grid.getColumns()[i], false);
            checkHiddenCellInBlock(grid, grid.getBlocks()[i]);
        }
    }

    private void checkHiddenCellInRowColumn(Grid grid, AbstractRegion region, boolean isRow) {
        for (int k = 1; k <= 9; k++) {
            if (region.isDigitGained(k)) {
                continue;
            }

            int subRegionCount = 0;
            int hiddenSubRegionOffset = 10;

            for (int iSubRegion = 0; iSubRegion < 3; iSubRegion++) {
                if (region.getSubRegion()[iSubRegion].supportNumber(k)) {
                    subRegionCount++;
                    hiddenSubRegionOffset = iSubRegion;
                }
            }
            if (subRegionCount == 1) {
                int blockId;
                if (isRow) {
                    blockId = region.getId() / 3 * 3 + hiddenSubRegionOffset;
                } else {
                    blockId = region.getId() / 3 + hiddenSubRegionOffset * 3;
                }
                grid.getBlocks()[blockId].removeDigit(k, region.getSubRegion()[hiddenSubRegionOffset].getCells(), Arrays.asList(region.getSubRegion()[hiddenSubRegionOffset].getCells()));
                //must return here because change always done, some key cell maybe changed.
                //                return;
            }
        }
    }

    private void checkHiddenCellInBlock(Grid grid, Block block) {
        for (int k = 1; k <= 9; k++) {
            if (block.isDigitGained(k)) {
                continue;
            }
            int subRegionCount = 0;
            int hiddenSubRegionOffset = 10;

            for (int iSubRegion = 0; iSubRegion < 3; iSubRegion++) {
                if (block.getSubRows()[iSubRegion].supportNumber(k)) {
                    subRegionCount++;
                    hiddenSubRegionOffset = iSubRegion;
                }
            }

            if (subRegionCount == 1) {
                grid.getRows()[block.getBlockR() * 3 + hiddenSubRegionOffset].removeDigit(k, block.getSubRows()[hiddenSubRegionOffset].getCells(), Arrays.asList(block.getSubRows()[hiddenSubRegionOffset].getCells()));
            }

            subRegionCount = 0;
            hiddenSubRegionOffset = 10;

            for (int iSubRegion = 0; iSubRegion < 3; iSubRegion++) {
                if (block.getSubColumns()[iSubRegion].supportNumber(k)) {
                    subRegionCount++;
                    hiddenSubRegionOffset = iSubRegion;
                }
            }

            if (subRegionCount == 1) {
                grid.getColumns()[block.getBlockC() * 3 + hiddenSubRegionOffset].removeDigit(k, block.getSubColumns()[hiddenSubRegionOffset].getCells(), Arrays.asList(block.getSubColumns()[hiddenSubRegionOffset].getCells()));
            }

        }
    }
}
