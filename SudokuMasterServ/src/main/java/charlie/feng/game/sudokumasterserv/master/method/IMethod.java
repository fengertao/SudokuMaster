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
     * Get the cost of this method. Higher cost means this method should be tried later.
     *
     * @return the cost value
     * 1: player able to resolve 1 star puzzle
     * 2: player able to resolve 2 star puzzle
     * 8: unimplemented method
     * 9: XChain
     * 10: complicated XChain
     * 11: multi digit strong weak chain
     * 99: brute force
     */
    int getCost();

    /**
     * Execute the method, change grid position if any opportunity found.
     * @param grid the Grid to be play
     */
    void apply(Grid grid);

}
