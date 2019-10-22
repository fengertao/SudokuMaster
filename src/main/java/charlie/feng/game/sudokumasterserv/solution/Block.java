/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.solution;

import java.util.Set;

public class Block extends Region {

    public int blockR;
    public int blockC;
    public SubRegion[] subRows;
    public SubRegion[] subColumns;

    public Block(Grid grid, int no) {
        this.grid = grid;
        this.id = no;
        cells = new Cell[9];
        subRows = new SubRow[]{
                new SubRow(),
                new SubRow(),
                new SubRow()
        };
        subColumns = new SubColumn[]{
                new SubColumn(),
                new SubColumn(),
                new SubColumn()
        };

        for (int i = 0; i < 3; i++) {
            subRows[i].cells = new Cell[3];
            subColumns[i].cells = new Cell[3];
        }

        blockR = no / 3;
        blockC = no % 3;
        for (int i = 0; i < 9; i++) {
            int offsetR = i / 3;
            int offsetC = i % 3;
            Cell currentCell = grid.cells[blockR * 3 + offsetR][blockC * 3 + offsetC];
            cells[i] = currentCell;
            subRows[offsetR].cells[offsetC] = currentCell;
            subColumns[offsetC].cells[offsetR] = currentCell;
        }
    }

    public Set<Integer> getPossibleSubRegion(int k, boolean isRow, Set<Integer> resultSet) {
        resultSet.clear();
        for (int i = 0; i < 3; i++) {
            SubRegion subRegion = isRow ? subRows[i] : subColumns[i];
            if (subRegion.supportNumber(k)) {
                resultSet.add(i);
            }
        }
        return resultSet;
    }

}
