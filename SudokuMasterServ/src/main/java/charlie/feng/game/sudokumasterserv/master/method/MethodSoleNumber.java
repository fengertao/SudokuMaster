/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Grid;

/**
 * 唯一余数法
 * 如果某一单元格所在的行，列及区块中共出现了8个不同的数字，那么该单元格可以确定地填入还未出现过的数字
 * 本方法被NakedSingle包含，故实现体为空
 */
public class MethodSoleNumber implements IMethod {

    @Override
    public void apply(Grid grid) {
    }

}
