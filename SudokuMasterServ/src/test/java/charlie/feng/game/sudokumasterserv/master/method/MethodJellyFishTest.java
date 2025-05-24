/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import charlie.feng.game.sudokumasterserv.master.Grid;

/**
 * Test cases for JellyFish method.
 */
public class MethodJellyFishTest {

    @Test
    public void checkJellyFishRow() {
        // 测试行方向的JellyFish模式
        // 数字4在0,1,2,3行中只能出现在4,5,6,7列
        Grid grid = new Grid(Grid.EMPTY_GRID);

        // 移除其他列的候选数4
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                grid.getCells()[row][col].removeDigitFromCandidate(4, "mock", null);
            }
            for (int col = 8; col < 9; col++) {
                grid.getCells()[row][col].removeDigitFromCandidate(4, "mock", null);
            }
        }

        new MethodJellyFish().apply(grid);

        // 验证4,5,6,7列的其他行（4-8行）中的候选数4是否被移除
        for (int row = 4; row < 9; row++) {
            for (int col = 4; col < 8; col++) {
                Assertions.assertFalse(
                    grid.getCells()[row][col].isSupportCandidate(4),
                    "Cell at row " + row + ", col " + col + " should not have candidate 4"
                );
            }
        }
    }

    @Test
    public void checkJellyFishColumn() {
        // 测试列方向的JellyFish模式
        // 数字5在0,1,2,3列中只能出现在4,5,6,7行
        Grid grid = new Grid(Grid.EMPTY_GRID);

        // 移除其他行的候选数5
        for (int col = 0; col < 4; col++) {
            for (int row = 0; row < 4; row++) {
                grid.getCells()[row][col].removeDigitFromCandidate(5, "mock", null);
            }
            for (int row = 8; row < 9; row++) {
                grid.getCells()[row][col].removeDigitFromCandidate(5, "mock", null);
            }
        }

        new MethodJellyFish().apply(grid);

        // 验证4,5,6,7行的其他列（4-8列）中的候选数5是否被移除
        for (int col = 4; col < 9; col++) {
            for (int row = 4; row < 8; row++) {
                Assertions.assertFalse(
                    grid.getCells()[row][col].isSupportCandidate(5),
                    "Cell at row " + row + ", col " + col + " should not have candidate 5"
                );
            }
        }
    }

    @Test
    public void checkJellyFishWithMultipleDigits() {
        // 测试多个数字的JellyFish模式
        // 数字6和7在0,1,2,3行中只能出现在4,5,6,7列
        Grid grid = new Grid(Grid.EMPTY_GRID);

        // 移除其他列的候选数6和7
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                grid.getCells()[row][col].removeDigitFromCandidate(6, "mock", null);
                grid.getCells()[row][col].removeDigitFromCandidate(7, "mock", null);
            }
            for (int col = 8; col < 9; col++) {
                grid.getCells()[row][col].removeDigitFromCandidate(6, "mock", null);
                grid.getCells()[row][col].removeDigitFromCandidate(7, "mock", null);
            }
        }

        new MethodJellyFish().apply(grid);

        // 验证4,5,6,7列的其他行（4-8行）中的候选数6和7是否被移除
        for (int row = 4; row < 9; row++) {
            for (int col = 4; col < 8; col++) {
                Assertions.assertFalse(
                    grid.getCells()[row][col].isSupportCandidate(6),
                    "Cell at row " + row + ", col " + col + " should not have candidate 6"
                );
                Assertions.assertFalse(
                    grid.getCells()[row][col].isSupportCandidate(7),
                    "Cell at row " + row + ", col " + col + " should not have candidate 7"
                );
            }
        }
    }
}
