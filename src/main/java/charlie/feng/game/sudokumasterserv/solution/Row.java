/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.solution;

public class Row extends Region {

    public Row(Grid grid, int rowid) {
        this.grid = grid;
        this.id = rowid;
        cells = new Cell[9];
        subRegion = new SubRow[]{
                new SubRow(),
                new SubRow(),
                new SubRow()
        };
        for (int i = 0; i < 3; i++) {
            subRegion[i].cells = new Cell[3];
        }
        for (int i = 0; i < 9; i++) {
            Cell currentCell = grid.cells[rowid][i];
            cells[i] = currentCell;
            subRegion[i / 3].cells[i % 3] = currentCell;
        }
    }

}
