/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master;

public class SubRegion {
    private Cell[] cells = new Cell[3];

    public boolean supportNumber(int k) {
        for (int i = 0; i < 3; i++) {
            if (getCells()[i].isSupportCandidate(k)) {
                return true;
            }
        }
        return false;
    }

    public Cell[] getCells() {
        return cells;
    }

    public void setCells(Cell[] cells) {
        this.cells = cells;
    }
}
