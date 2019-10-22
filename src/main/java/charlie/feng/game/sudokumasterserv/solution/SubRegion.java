/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.solution;

public class SubRegion {
    public Cell[] cells = new Cell[3];

    public boolean supportNumber(int k) {
        for (int i = 0; i < 3; i++) {
            if (cells[i].isSupportCandidate(k))
                return true;
        }
        return false;
    }
}
