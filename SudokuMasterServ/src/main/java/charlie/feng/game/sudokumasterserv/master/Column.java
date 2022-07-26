/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master;

public class Column extends AbstractRegion {

    public Column(Grid grid, int colId) {
        this.setGrid(grid);
        this.setId(colId);
        setCells(new Cell[9]);
        setSubRegion(new SubColumn[]{
                new SubColumn(),
                new SubColumn(),
                new SubColumn()
        });
        for (int i = 0; i < 3; i++) {
            getSubRegion()[i].setCells(new Cell[3]);
        }
        for (int i = 0; i < 9; i++) {
            Cell currentCell = grid.getCells()[i][colId];
            getCells()[i] = currentCell;
            getSubRegion()[i / 3].getCells()[i % 3] = currentCell;
        }
    }

    @Override
    protected MsgKey getMsgKeyForValueExistType() {
        return MsgKey.VALUE_IN_SAME_COLUMN;
    }
}
