/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Grid;

/**
 * Method to resolve Sudoku
 */
public interface IMethod {

    /**
     * Execute the method, change grid position if any opportunity found.
     * @param grid the Grid to be play
     */
    void apply(Grid grid);

}
