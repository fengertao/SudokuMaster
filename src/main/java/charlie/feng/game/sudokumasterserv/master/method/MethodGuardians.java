/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Grid;

/**
 * 守护者 (Guardians)
 * 有地方称之为Broken Wings或者Turbot-Fish??
 * 其描述的是某一个候选数X的情况，当有偶数条强链，且两个端点处于同一unit时，这时可以删除两个端点上的候选数X
 * ref: http://www.sudokufans.org.cn/forums/topic/38/?page=2#comment-247
 */

public class MethodGuardians implements IMethod {

    @Override
    public void apply(Grid grid) {
        //Todo to be implement
    }

}
