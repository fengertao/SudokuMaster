/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Block;
import charlie.feng.game.sudokumasterserv.master.Grid;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * 组合排除法
 * 如果在横向并行的两个区块中，某个数字可能填入的位置正好都分别占据相同的两行，则这两行可以被用来对横向并行的另一区块做行排除。
 * 如果在纵向并行的两个区块中，某个数字可能填入的位置正好都分别占据相同的两列，则这两列可以被用来对纵向并行的另一区块做列排除。
 */
public class MethodCombinationElimination implements IMethod {

    @Override
    public void apply(Grid grid) {
        for (int i = 0; i < 3; i++) {
            checkSmallShape(grid.getBlocks()[i * 3], grid.getBlocks()[i * 3 + 1], grid.getBlocks()[i * 3 + 2], true);
            checkSmallShape(grid.getBlocks()[i], grid.getBlocks()[i + 3], grid.getBlocks()[i + 6], false);
        }
    }

    private void checkSmallShape(Block block0, Block block1, Block block2, boolean isRow) {
        for (int k = 1; k <= 9; k++) {
            if ((block0.isDigitGained(k)) || (block1.isDigitGained(k)) || (block2.isDigitGained(k))) {
                continue;
            }
            checkHiddenCellInSharp(k, block0, block1, block2, isRow);
            checkHiddenCellInSharp(k, block0, block2, block1, isRow);
            checkHiddenCellInSharp(k, block1, block2, block0, isRow);
        }
    }

    private void checkHiddenCellInSharp(int digit, Block source1, Block source2, Block target, boolean isRow) {
        Set<Integer> sourceSet1 = new HashSet<>();
        Set<Integer> sourceSet2 = new HashSet<>();
        sourceSet1 = source1.getPossibleSubRegion(digit, isRow, sourceSet1);
        sourceSet2 = source2.getPossibleSubRegion(digit, isRow, sourceSet2);

        if ((sourceSet1.size() == 2) && (sourceSet2.size() == 2)) {
            if (sourceSet1.equals(sourceSet2)) {
                Integer[] subRegionArray = sourceSet1.toArray(new Integer[2]);
                @SuppressWarnings("PointlessArithmeticExpression")
                int excludeSubRegion = 0 + 1 + 2 - subRegionArray[0] - subRegionArray[1];
                //Todo refCells
                target.removeDigit(digit, (isRow ? target.getSubRows()[excludeSubRegion] : target.getSubColumns()[excludeSubRegion]).getCells(), new ArrayList<>());
                //must return here because change always done, some key cell maybe changed.
                //                return;
            }
        }
    }

}
