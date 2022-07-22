/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Grid;

/**
 * WXYZ形态匹配法 (WXYZ-wing)
 * WXYZ形态匹配法是更加进阶的形态匹配法，但它将涉及到一个单元格包含4个候选数的情况。典型的WXYZ形态如下：
 * 0  *  WZ
 * 0 WXYZ 0
 * 0  *  0
 * 0  0  0
 * 0  XZ 0
 * 0  YZ  0
 * 其中WXYZ表示拥有4个候选数的单元格，它与WZ在同一区块但不同列中，而与XZ和YZ在不同区块但在同一列中。
 * 满足了这样的形态后，星号所示的单元格中将不能含有候选数Z。这是因为：
 * 如果WXYZ=W，则WZ必为Z，而同一区块中的星号所示的单元格中必然不能填入Z。
 * 如果WXYZ=X，则XZ必为Z，而同一列中的星号所示的单元格中不可能再填Z。
 * 如果WXYZ=Y，则YZ必为Z，而同一列中的星号所示的单元格中不可能再填Z。
 * 如果WXYZ=Z，则同一区块中的星号所示的单元格中不能再为Z。
 */

public class MethodWXYZWing implements IMethod {

    @Override
    public void apply(Grid grid) {
        //Todo to be implement
    }

}
