/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import charlie.feng.game.sudokumasterserv.master.Cell;
import charlie.feng.game.sudokumasterserv.master.Grid;

/**
 * XYZ形态匹配法 (XYZ-wing)
 * XYZ形态匹配法很象XY形态匹配法，但不同的是，这次有一个单元格包含3个候选数。典型的XYZ形态如下：
 * 0  *  YZ
 * 0 XYZ 0
 * 0  *  0
 * 0  0  0
 * 0  XZ 0
 * 0  0  0
 * 其中，XYZ表示该单元格有三个候选数，它与YZ在同一区块但不同列中，而与XZ在同一列但不同区块中。
 * 如果满足这样的条件，则星号所示的单元格中一定不能包含候选数Z。这是因为：
 * 如果XYZ=X，则XZ必然为Z。那么与XZ同一列的星号所示的单元格自然也就不能为Z。
 * 如果XYZ=Y，则YZ必然为Z。那么在同一区块中的星号所示的单元格自然就不能为Z。
 * 如果XYZ=Z，则与它同一区块的星号所在的单元格肯定不能是Z。
 */
public class MethodXYZWing implements IMethod {

    @Override
    public int getCost() {
        return 2;
    }

    @Override
    public void apply(Grid grid) {
        // Find all cells with exactly 3 candidates (potential XYZ cells)
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Cell cellXYZ = grid.getCells()[row][col];
                List<Integer> candidatesXYZ = cellXYZ.getCandidateList();
                if (candidatesXYZ.size() != 3) {
                    continue;
                }

                // Find YZ cell in the same block
                for (int blockRow = (row / 3) * 3; blockRow < (row / 3) * 3 + 3; blockRow++) {
                    for (int blockCol = (col / 3) * 3; blockCol < (col / 3) * 3 + 3; blockCol++) {
                        if (blockRow == row && blockCol == col) {
                            continue;
                        }

                        Cell cellYZ = grid.getCells()[blockRow][blockCol];
                        List<Integer> candidatesYZ = cellYZ.getCandidateList();
                        if (candidatesYZ.size() != 2) {
                            continue;
                        }

                        // Check if YZ cell shares two candidates with XYZ cell
                        Set<Integer> sharedCandidatesYZ = new HashSet<>(candidatesXYZ);
                        sharedCandidatesYZ.retainAll(candidatesYZ);
                        if (sharedCandidatesYZ.size() != 2) {
                            continue;
                        }

                        // Find XZ cell in the same column
                        for (int r = 0; r < 9; r++) {
                            if (r == row || (r / 3) == (row / 3)) {
                                continue; // Skip same row and same block
                            }

                            Cell cellXZ = grid.getCells()[r][col];
                            List<Integer> candidatesXZ = cellXZ.getCandidateList();
                            if (candidatesXZ.size() != 2) {
                                continue;
                            }

                            // Check if XZ cell shares two candidates with XYZ cell
                            Set<Integer> sharedCandidatesXZ = new HashSet<>(candidatesXYZ);
                            sharedCandidatesXZ.retainAll(candidatesXZ);
                            if (sharedCandidatesXZ.size() != 2) {
                                continue;
                            }

                            // Find the Z candidate (must be common between YZ and XZ cells)
                            Set<Integer> commonCandidates = new HashSet<>(candidatesYZ);
                            commonCandidates.retainAll(candidatesXZ);
                            if (commonCandidates.size() != 1) {
                                continue;
                            }

                            int z = commonCandidates.iterator().next();
                            if (!candidatesXYZ.contains(z)) {
                                continue;
                            }

                            // XYZ-Wing pattern found, remove Z from affected cells
                            // Remove Z from cells that are in both the same block as XYZ and the same column as XZ
                            for (int blockR = (row / 3) * 3; blockR < (row / 3) * 3 + 3; blockR++) {
                                for (int blockC = (col / 3) * 3; blockC < (col / 3) * 3 + 3; blockC++) {
                                    // Skip the XYZ and YZ cells themselves
                                    if ((blockR == row && blockC == col) || (blockR == blockRow && blockC == blockCol)) {
                                        continue;
                                    }
                                    // Only remove Z from cells in the same column as XZ
                                    if (blockC == col) {
                                        grid.getCells()[blockR][blockC].removeDigitFromCandidate(z,
                                                this.getClass().getSimpleName(),
                                                Arrays.asList(cellXYZ, cellYZ, cellXZ));
                                    }
                                }
                            }
                        }

                        // Find XZ cell in the same row
                        for (int c = 0; c < 9; c++) {
                            if (c == col || (c / 3) == (col / 3)) {
                                continue; // Skip same column and same block
                            }

                            Cell cellXZ = grid.getCells()[row][c];
                            List<Integer> candidatesXZ = cellXZ.getCandidateList();
                            if (candidatesXZ.size() != 2) {
                                continue;
                            }

                            // Check if XZ cell shares two candidates with XYZ cell
                            Set<Integer> sharedCandidatesXZ = new HashSet<>(candidatesXYZ);
                            sharedCandidatesXZ.retainAll(candidatesXZ);
                            if (sharedCandidatesXZ.size() != 2) {
                                continue;
                            }

                            // Find the Z candidate (must be common between YZ and XZ cells)
                            Set<Integer> commonCandidates = new HashSet<>(candidatesYZ);
                            commonCandidates.retainAll(candidatesXZ);
                            if (commonCandidates.size() != 1) {
                                continue;
                            }

                            int z = commonCandidates.iterator().next();
                            if (!candidatesXYZ.contains(z)) {
                                continue;
                            }

                            // XYZ-Wing pattern found, remove Z from affected cells
                            // Remove Z from cells that are in both the same block as XYZ and the same row as XZ
                            for (int blockR = (row / 3) * 3; blockR < (row / 3) * 3 + 3; blockR++) {
                                for (int blockC = (col / 3) * 3; blockC < (col / 3) * 3 + 3; blockC++) {
                                    // Skip the XYZ and YZ cells themselves
                                    if ((blockR == row && blockC == col) || (blockR == blockRow && blockC == blockCol)) {
                                        continue;
                                    }
                                    // Only remove Z from cells in the same row as XZ
                                    if (blockR == row) {
                                        grid.getCells()[blockR][blockC].removeDigitFromCandidate(z,
                                                this.getClass().getSimpleName(),
                                                Arrays.asList(cellXYZ, cellYZ, cellXZ));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
