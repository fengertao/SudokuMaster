/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Cell;
import charlie.feng.game.sudokumasterserv.master.Grid;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * XY形态匹配法 (XY-wing)
 * 形式1：
 * 0  0  0    0  0  0
 * 0  XY 0    0  XZ 0
 * 0  0  0    0  0  0
 * 0  0  0    0  0  0
 * 0  YZ 0    0  *  0
 * 0  0  0    0  0  0
 * XY，XZ和YZ分别表示只有两个候选数的单元格，但它们的候选数部分重叠。可以看到，不管XY最后取什么值，星号所示的位置不可能是Z值。这是因为：
 * 如果XY取X值，则与其同行的XZ只能取Z值，这样星号所示单元格就不能为Z值。
 * 如果XY取Y值，则与其同列的YZ只能取Z值，而星号所示的单元格同样不能是Z值。
 * 形式2：
 * *  XY *    0  XZ 0
 * 0  0  0    0  0  0
 * YZ 0  0    *  *  *
 * 这时，XY和YZ同在一个区块但不同行中，而XZ和XY在同一行，但在不同区块中。同样，所有打星号的单元格中不能是Z值。这是因为：
 * 如果XY＝X，则XZ＝Z。那么XZ所在的行和区块中就不能再出现Z；
 * 如果XY＝Y，则YZ＝Z。那么YZ所在的行和区块中就不能再出现Z。
 * 第二种XY形态的变形，即XY和YZ在同一区块但不同列中，而XY和XZ在同一列的不同区块中：
 * 0  0  *
 * YZ 0  *
 * 0  0  XY
 * *  0  XZ
 * *  0  0
 * *  0  0
 * 分析方法与之前一样，结果是打星号的单元格中不能出现候选数Z。
 */
public class MethodXYWing implements IMethod {
    @Override
    public void apply(Grid grid) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                // Grid[row][col] is A cell. A cell have relationship with B Cell and C Cell.
                checkXYWingRowColumn(grid, row, col);
                checkXYWingRowBlock(grid, row, col, true);
                checkXYWingRowBlock(grid, row, col, false);
            }
        }
    }

    private void checkXYWingRowColumn(Grid grid, int rowA, int colA) {
        Integer x;
        Integer y;
        Integer z;
        Cell cellA = grid.getCells()[rowA][colA];
        List<Integer> candidatesA = cellA.getCandidateList();
        if (candidatesA.size() != 2) {
            return;
        }
        for (int colB = 0; colB < 9; colB++) {
            if (colB == colA) {
                continue;
            }
            Cell cellB = grid.getCells()[rowA][colB];
            List<Integer> candidatesB = cellB.getCandidateList();
            if (candidatesB.size() != 2) {
                continue;
            }
            Set<Integer> candidateAB = new HashSet<>(4);
            candidateAB.addAll(candidatesA);
            candidateAB.addAll(candidatesB);
            if (candidateAB.size() != 3) {
                continue;
            }
            if (candidatesA.get(0).equals(candidatesB.get(0))) {
                x = candidatesA.get(0);
                y = candidatesA.get(1);
                z = candidatesB.get(1);
            } else if (candidatesA.get(0).equals(candidatesB.get(1))) {
                x = candidatesA.get(0);
                y = candidatesA.get(1);
                z = candidatesB.get(0);
            } else if (candidatesA.get(1).equals(candidatesB.get(0))) {
                x = candidatesA.get(1);
                y = candidatesA.get(0);
                z = candidatesB.get(1);
            } else if (candidatesA.get(1).equals(candidatesB.get(1))) {
                x = candidatesA.get(1);
                y = candidatesA.get(0);
                z = candidatesB.get(0);
            } else {
                throw new RuntimeException("Wrong logic");
            }

            for (int rowC = 0; rowC < 9; rowC++) {
                if (rowC == colA) {
                    continue;
                }
                Cell cellC = grid.getCells()[rowC][colA];
                List<Integer> candidatesC = cellC.getCandidateList();
                if (candidatesC.size() != 2) {
                    continue;
                }
                Set<Integer> candidateAC = new HashSet<>(4);
                candidateAC.addAll(candidatesA);
                candidateAC.addAll(candidatesC);
                if (candidateAC.size() != 3) {
                    continue;
                }
                if (x.equals(candidatesC.get(0))) {
                    continue; //cellCshould contains y and z only
                } else if (x.equals(candidatesC.get(1))) {
                    continue; //cellCshould contains y and z only
                } else if (y.equals(candidatesC.get(0))) {
                    if (!z.equals(candidatesC.get(1))) {
                        continue;
                    }
                } else if (y.equals(candidatesC.get(1))) {
                    if (!z.equals(candidatesC.get(0))) {
                        continue;
                    }
                } else {
                    throw new RuntimeException("Wrong logic");
                }

                //XY-Wing detected, now remove z from * cell
                grid.getCells()[rowC][colB].removeDigitFromCandidate(z, this.getClass().getSimpleName(), Arrays.asList(cellA, cellB, cellC));
            }

        }

    }

    /**
     * if isABSameRow, means Cell A and B in same row,
     * else Cell A and B in same column
     */
    private void checkXYWingRowBlock(Grid grid, int rowA, int colA, boolean isABSameRow) {
        Integer x;
        Integer y;
        Integer z;
        Cell cellA = grid.getCells()[rowA][colA];
        List<Integer> candidatesA = cellA.getCandidateList();
        if (candidatesA.size() != 2) {
            return;
        }
        for (int rowColB = 0; rowColB < 9; rowColB++) {
            if ((isABSameRow) && (rowColB == colA) || (!isABSameRow) && (rowColB == rowA)) {
                continue;
            }
            Cell cellB = isABSameRow ? grid.getCells()[rowA][rowColB] : grid.getCells()[rowColB][colA];
            List<Integer> candidatesB = cellB.getCandidateList();
            if (candidatesB.size() != 2) {
                continue;
            }
            Set<Integer> candidateAB = new HashSet<>(4);
            candidateAB.addAll(candidatesA);
            candidateAB.addAll(candidatesB);
            if (candidateAB.size() != 3) {
                continue;
            }
            if (candidatesA.get(0).equals(candidatesB.get(0))) {
                x = candidatesA.get(0);
                y = candidatesA.get(1);
                z = candidatesB.get(1);
            } else if (candidatesA.get(0).equals(candidatesB.get(1))) {
                x = candidatesA.get(0);
                y = candidatesA.get(1);
                z = candidatesB.get(0);
            } else if (candidatesA.get(1).equals(candidatesB.get(0))) {
                x = candidatesA.get(1);
                y = candidatesA.get(0);
                z = candidatesB.get(1);
            } else if (candidatesA.get(1).equals(candidatesB.get(1))) {
                x = candidatesA.get(1);
                y = candidatesA.get(0);
                z = candidatesB.get(0);
            } else {
                throw new RuntimeException("Wrong logic");
            }

            //find cellC which is same block with cellA and not same rowcol with CellB
            for (int rowC = (cellA.getRowId() / 3) * 3; rowC <= (cellA.getRowId() / 3) * 3 + 2; rowC++) {
                if (isABSameRow && rowC == rowA) {
                    continue; //Since A and B in same row, C should not in same row. elsewise apply hidden triplet method.
                }
                for (int colC = (cellA.getColumnId() / 3) * 3; colC <= (cellA.getColumnId() / 3) * 3 + 2; colC++) {
                    if (!isABSameRow && colC == colA) {
                        continue; //Since A and B in same col, C should not in same col. elsewise apply hidden triplet method.
                    }
                    Cell cellC = grid.getCells()[rowC][colC];
                    List<Integer> candidatesC = cellC.getCandidateList();
                    if (candidatesC.size() != 2) {
                        continue;
                    }
                    Set<Integer> candidateAC = new HashSet<>(4);
                    candidateAC.addAll(candidatesA);
                    candidateAC.addAll(candidatesC);
                    if (candidateAC.size() != 3) {
                        continue;
                    }
                    if (x.equals(candidatesC.get(0))) {
                        continue; //cellCshould contains y and z only
                    } else if (x.equals(candidatesC.get(1))) {
                        continue; //cellCshould contains y and z only
                    } else if (y.equals(candidatesC.get(0))) {
                        if (!z.equals(candidatesC.get(1))) {
                            continue;
                        }
                    } else if (y.equals(candidatesC.get(1))) {
                        if (!z.equals(candidatesC.get(0))) {
                            continue;
                        }
                    } else {
                        throw new RuntimeException("Wrong logic");
                    }

                    //XY-Wing detected, now remove z from * cell
                    if (isABSameRow) {
                        for (int colStar = (cellA.getColumnId() / 3) * 3; colStar <= (cellA.getColumnId() / 3) * 3 + 2; colStar++) {
                            if ((colStar != colA) && (colStar != rowColB)) {
                                grid.getCells()[rowA][colStar].removeDigitFromCandidate(z, this.getClass().getSimpleName(), Arrays.asList(cellA, cellB, cellC));
                            }
                        }
                        for (int colStar = (cellB.getColumnId() / 3) * 3; colStar <= (cellB.getColumnId() / 3) * 3 + 2; colStar++) {
                            if (colStar != colC) {
                                grid.getCells()[rowC][colStar].removeDigitFromCandidate(z, this.getClass().getSimpleName(), Arrays.asList(cellA, cellB, cellC));
                            }
                        }
                    } else {
                        for (int rowStar = (cellA.getRowId() / 3) * 3; rowStar <= (cellA.getRowId() / 3) * 3 + 2; rowStar++) {
                            if ((rowStar != rowA) && (rowStar != rowColB)) {
                                grid.getCells()[rowStar][colA].removeDigitFromCandidate(z, this.getClass().getSimpleName(), Arrays.asList(cellA, cellB, cellC));
                            }
                        }
                        for (int rowStar = (cellB.getRowId() / 3) * 3; rowStar <= (cellB.getRowId() / 3) * 3 + 2; rowStar++) {
                            if (rowStar != rowC) {
                                grid.getCells()[rowStar][colC].removeDigitFromCandidate(z, this.getClass().getSimpleName(), Arrays.asList(cellA, cellB, cellC));
                            }
                        }
                    }
                }
            }
        }
    }
}
