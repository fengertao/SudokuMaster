/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Block;
import charlie.feng.game.sudokumasterserv.master.Grid;
import charlie.feng.game.sudokumasterserv.master.Region;
import com.google.common.collect.ImmutableList;

/**
 * 区块排除法
 * 又名 双隐格排除法 三隐格排除法 区块删减法 (Intersection Removal)
 * 当某数字在某个区块中可填入的位置正好都在同一行上，因为该区块中必须要有该数字，所以这一行中不在该区块内的单元格上将不能再出现该数字。
 * 当某数字在某个区块中可填入的位置正好都在同一列上，因为该区块中必须要有该数字，所以这一列中不在该区块内的单元格上将不能再出现该数字。
 * 当某数字在某行中可填入的位置正好都在同一区块上，因为该行中必须要有该数字，所以该区块中不在该行内的单元格上将不能再出现该数字。
 * 当某数字在某列中可填入的位置正好都在同一区块上，因为该列中必须要有该数字，所以该区块中不在该列内的单元格上将不能再出现该数字。
 */
public class MethodBlockElimination implements IMethod {
    public void apply(Grid grid) {
        for (int i = 0; i < 9; i++) {
            checkHiddenCellInRowColumn(grid, grid.rows[i], true);
            checkHiddenCellInRowColumn(grid, grid.columns[i], false);
            checkHiddenCellInBlock(grid, grid.blocks[i]);
        }
    }

    private void checkHiddenCellInRowColumn(Grid grid, Region region, boolean isRow) {
        for (int k = 1; k <= 9; k++) {
            if (region.isDigitGained(k)) {
                continue;
            }

            int subRegionCount = 0;
            int hiddenSubRegionOffset = 10;

            for (int iSubRegion = 0; iSubRegion < 3; iSubRegion++) {
                if (region.subRegion[iSubRegion].supportNumber(k)) {
                    subRegionCount++;
                    hiddenSubRegionOffset = iSubRegion;
                }
            }
            if (subRegionCount == 1) {
                int blockId;
                if (isRow) {
                    blockId = region.id / 3 * 3 + hiddenSubRegionOffset;
                } else {
                    blockId = region.id / 3 + hiddenSubRegionOffset * 3;
                }
                grid.blocks[blockId].removeDigit(k, region.subRegion[hiddenSubRegionOffset].cells, ImmutableList.copyOf(region.subRegion[hiddenSubRegionOffset].cells));
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
            {
                int subRegionCount = 0;
                int hiddenSubRegionOffset = 10;

                for (int iSubRegion = 0; iSubRegion < 3; iSubRegion++) {
                    if (block.subRows[iSubRegion].supportNumber(k)) {
                        subRegionCount++;
                        hiddenSubRegionOffset = iSubRegion;
                    }
                }

                if (subRegionCount == 1) {
                    grid.rows[block.blockR * 3 + hiddenSubRegionOffset].removeDigit(k, block.subRows[hiddenSubRegionOffset].cells, ImmutableList.copyOf(block.subRows[hiddenSubRegionOffset].cells));
                }
            }
            {
                int subRegionCount = 0;
                int hiddenSubRegionOffset = 10;

                for (int iSubRegion = 0; iSubRegion < 3; iSubRegion++) {
                    if (block.subColumns[iSubRegion].supportNumber(k)) {
                        subRegionCount++;
                        hiddenSubRegionOffset = iSubRegion;
                    }
                }

                if (subRegionCount == 1) {
                    grid.columns[block.blockC * 3 + hiddenSubRegionOffset].removeDigit(k, block.subColumns[hiddenSubRegionOffset].cells, ImmutableList.copyOf(block.subColumns[hiddenSubRegionOffset].cells));
                    //must return here because change always done, some key cell maybe changed.
                    //                    return;
                }
            }

        }
    }
}
