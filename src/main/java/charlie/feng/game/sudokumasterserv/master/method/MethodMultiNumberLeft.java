/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Grid;

/**
 * 二余法 (同样可以有三余法、四余法等等)
 * 根据行某单元格还缺两个数字，在该单元格的列或者块中寻找，如果可以找到任何一个，就可以确认该单元格的解
 * 根据列、块同理
 * 本方法已经被其它方法包含，故无需实现
 */

public class MethodMultiNumberLeft implements IMethod {

    @Override
    public void apply(Grid grid) {
    }

}
