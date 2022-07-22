/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Grid;

/**
 * XYZ形态匹配法 (XYZ-wing)
 * XYZ形态匹配法很象XY形态匹配法，但不同的是，这次有一个单元格包含3个候选数。典型的XYZ形态如下：
 * 0  *  YZ
 * 0 XYZ *
 * 0  *  0
 * 0  0  0
 * 0  XZ 0
 * 0  0  0
 * 其中，XYZ表示该单元格有三个候选数，它与YZ在同一区块但不同列中，而与XZ在同一列但不同区块中。
 * 如果满足这样的条件，则星号所示的单元格中一定不能包含候选数Z。这是因为：
 * 如果XYZ=X，则YZ必然为Z。那么在同一区块中的星号所示的单元格自然就不能为Z。
 * 如果XYZ=Y，则XZ必然为Z。那么与XZ同一列的星号所示的单元格自然也就不能为Z。
 * 如果XYZ=Z，则与它同一区块的星号所在的单元格肯定不能是Z。
 */

public class MethodXYZWing implements IMethod {

    @Override
    public void apply(Grid grid) {
        //Todo to be implement
    }

}
