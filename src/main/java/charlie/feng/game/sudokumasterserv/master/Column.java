/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master;

public class Column extends Region {

    public Column(Grid grid, int colId) {
        this.grid = grid;
        this.id = colId;
        cells = new Cell[9];
        subRegion = new SubColumn[]{
                new SubColumn(),
                new SubColumn(),
                new SubColumn()
        };
        for (int i = 0; i < 3; i++) {
            subRegion[i].cells = new Cell[3];
        }
        for (int i = 0; i < 9; i++) {
            Cell currentCell = grid.cells[i][colId];
            cells[i] = currentCell;
            subRegion[i / 3].cells[i % 3] = currentCell;
        }
    }

}
