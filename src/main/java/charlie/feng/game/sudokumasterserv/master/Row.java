/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master;

public class Row extends AbstractRegion {

    public Row(Grid grid, int rowid) {
        this.setGrid(grid);
        this.setId(rowid);
        setCells(new Cell[9]);
        setSubRegion(new SubRow[]{
                new SubRow(),
                new SubRow(),
                new SubRow()
        });
        for (int i = 0; i < 3; i++) {
            getSubRegion()[i].setCells(new Cell[3]);
        }
        for (int i = 0; i < 9; i++) {
            Cell currentCell = grid.getCells()[rowid][i];
            getCells()[i] = currentCell;
            getSubRegion()[i / 3].getCells()[i % 3] = currentCell;
        }
    }

    @Override
    protected MsgKey getMsgKeyForValueExistType() {
        return MsgKey.VALUE_IN_SAME_ROW;
    }

}
