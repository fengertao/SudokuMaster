/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Grid;
import charlie.feng.game.sudokumasterserv.master.Row;
import org.paukov.combinatorics3.Generator;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 暴力解题法。
 * 在其它方法试过都无法解题的时候，用暴力解题法
 */
public class MethodBruteForce implements IMethod {
    @Override
    public void apply(Grid grid) {
        Grid solution = bruteForceOnRow(grid, 0);
        grid.load(solution.getAnswer());
    }

    public Grid bruteForceOnRow(Grid grid, int row) {
        if (row == 9) {
            return grid;
        }
        AtomicReference<Grid> solution = new AtomicReference<>();
        Generator.permutation(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .simple()
                .stream()
                .anyMatch(cols -> {
                    Grid tempGrid = tryApplyRowToGrid(grid, row, cols);
                    if (tempGrid == null) {
                        return false;
                    } else {
                        solution.set(tempGrid);
                        return true;
                    }
                });
        return solution.get() == null ? null : solution.get();
    }

    public Grid tryApplyRowToGrid(Grid grid, int rowId, List<Integer> cols) {
        Row row = grid.getRows()[rowId];
        for (int i = 0; i < 9; i++) {
            if (cols.get(i).equals(row.getCells()[i].getValue())) {
                continue;
            }
            if (row.getCells()[i].getValue() != null) {
                return null;
            }
            if (grid.getColumns()[i].isDigitGained(cols.get(i))) {
                return null;
            }
            if (grid.getBlocks()[rowId / 3 * 3 + i / 3].isDigitGained(cols.get(i))) {
                return null;
            }
        }
        Grid newGrid = new Grid(grid.getAnswer());
        Row newRow = newGrid.getRows()[rowId];
        for (int i = 0; i < 9; i++) {
            newRow.getCells()[i].tryValue(cols.get(i));
        }
        return bruteForceOnRow(newGrid, rowId + 1);
    }
}
