/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Grid;

/**
 * 全双值坟墓法 (Bivalue Universal Grave,简称BUG)
 * BUG的全称为：Bivalue Universal Grave，意为所有格均为双值格（bivalue）。
 * A Bivalue Universal Grave (BUG) is any grid in which all the unsolved cells have two candidates,
 * and if a candidate exists in a row, column, or box, it shows up exactly twice.
 * BUG指的是一个盘势中所有未解格的候选数均含2个，且每个候选数对于每行、列、宫来说出现且只出现两次。
 * 这样的情况将导致题目多解或无解，
 * 所盘势中的某候选删除后会达到BUG结构，则其一定不能被删除。
 * 当盘势达到BUG基础上，某一格的候选数增加一个，即三个候选数时，我们将其称为BUG+1（BUG type1），
 * 可以把非双值格中在其行列宫仅出现两次的数字删除。如果不这么做，将导致题目无解。
 * 当使用了BUG之后，题目即为基础题，可以说BUG是一种魔术解法。
 * 当非双值格不止一个时，还有BUG+2...
 * 参考文献：http://www.sudokufans.org.cn/forums/topic/35/#comment-141
 * 以下五个method是不是BUG的情况?
 * Unique Rectangle type 1
 * Unique Rectangle type 2
 * Unique Rectangle type 3
 * Unique Rectangle type 4
 * Unique Loop
 */

public class MethodBUG implements IMethod {

    @Override
    public void apply(Grid grid) {
        //Todo to be implement
    }

}
