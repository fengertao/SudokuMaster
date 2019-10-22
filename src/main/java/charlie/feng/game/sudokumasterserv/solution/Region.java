/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.solution;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Region {
    private static Set<Integer> expectedSet = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

    public Cell[] cells;
    public SubRegion[] subRegion;
    public int id;
    Grid grid;

    public boolean isDigitGained(int value) {
        for (Cell cell : cells) {
            if ((cell.getValue() != null) && (cell.getValue() == value)) {
                return true;
            }
        }
        return false;
    }

    public void onCellResolved(Cell cell) {
        int value = cell.getValue();
        for (int i = 0; i < 9; i++) {
            Cell neighbor = cells[i];
            if (!neighbor.equals(cell)) {
                if (neighbor.getValue() == null) {
                    neighbor.removeDigitFromCandidate(value, this.getClass().getSimpleName());
                    if (neighbor.getNumberOfCandidates() == 1) {
                        neighbor.resolvedByNakedSingle();
                    }
                }
            }
        }
    }

    public void validate() {
        Set<Integer> resultSet = new HashSet<>();
        for (int i = 0; i < 9; i++) {
            if (cells[i].getValue() == null) {
                throw new RuntimeException("value is null");
            }
            resultSet.add(cells[i].getValue());
        }

        if (!resultSet.equals(expectedSet)) {
            //Todo better exception handling
            throw new RuntimeException(
                    "validate failure, Grid id:" + grid.id + " " + this.getClass().getSimpleName() + " id:" + id + " result:" + resultSet);
        }
    }

    // remove k from this group candidate number list, it is must in the excludeCells list;
    public void removeDigit(int k, Cell[] excludeCells) {
        for (int i = 0; i < 9; i++) {
            Cell currentCell = cells[i];
            if (Arrays.asList(excludeCells).contains(currentCell)) {
                continue;
            }
            if ((currentCell.getValue() == null) && (currentCell.isSupportCandidate(k))) {
                currentCell.removeDigitFromCandidate(k, this.getClass().getSimpleName());
                if (currentCell.getNumberOfCandidates() == 1) {
                    currentCell.resolvedByNakedSingle();
                }
            }
        }
    }

    public Set<Integer> getPossibleOffset(int k) {
        Set<Integer> possibleOffsets = new HashSet<>(9);
        for (int i = 0; i < 9; i++) {
            if (cells[i].isSupportCandidate(k)) {
                possibleOffsets.add(i);
            }
        }
        return possibleOffsets;
    }
}
