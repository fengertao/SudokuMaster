/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

/**
 * 三链数删减法 (Swordfish)
 * XWing的增强版
 * 如果某个数字在某三列中只出现在相同的三行中，则这个数字将从这三行上其他的候选数中删除。
 * 如果某个数字在某三行中只出现在相同的三列中，则这个数字也将从这三列上其他的候选数中删除
 */
public class MethodSwordFish extends MethodXWing implements IMethod {
    @Override
    protected int getNumberOfRegions() {
        return 3;
    }
}
