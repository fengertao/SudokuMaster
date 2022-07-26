/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

/**
 * 四链数删减法 (Jellyfish)
 * 与Swordfish类似，只是再进一步扩展到四行、四列。
 */
public class MethodJellyFish extends MethodXWing implements IMethod {

    @Override
    protected int getNumberOfRegions() {
        return 4;
    }

}
