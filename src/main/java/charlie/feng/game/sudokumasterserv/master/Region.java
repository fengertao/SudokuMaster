/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Region {
    private static Set<Integer> expectedSet = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

    private Cell[] cells;
    private SubRegion[] subRegion;
    private int id;
    private Grid grid;

    protected abstract MsgKey getMsgKeyForValueExistType();

    public boolean isDigitGained(int value) {
        for (Cell cell : getCells()) {
            if ((cell.getValue() != null) && (cell.getValue() == value)) {
                return true;
            }
        }
        return false;
    }

    public void onCellResolved(Cell cell) {
        int value = cell.getValue();
        for (int i = 0; i < 9; i++) {
            Cell neighbor = getCells()[i];
            if (!neighbor.equals(cell)) {
                if (neighbor.getValue() == null) {

                    neighbor.removeDigitFromCandidate(value, getMsgKeyForValueExistType().name(), Lists.newArrayList(cell));
                    if (neighbor.getNumberOfCandidates() == 1) {
                        neighbor.resolvedByNakedSingle(Lists.newArrayList(cell));
                    }
                }
            }
        }
    }

    public void validate() {
        Set<Integer> resultSet = new HashSet<>();
        for (int i = 0; i < 9; i++) {
            if (getCells()[i].getValue() == null) {
                throw new RuntimeException("value is null");
            }
            resultSet.add(getCells()[i].getValue());
        }

        if (!resultSet.equals(expectedSet)) {
            //Todo better exception handling
            throw new RuntimeException(
                    "validate failure, Grid id:" + getGrid().getId() + " " + this.getClass().getSimpleName() + " id:" + getId() + " result:" + resultSet);
        }
    }

    /**
     * remove k from this group candidate number list, it is must not in the excludeCells list;
     */

    public void removeDigit(int k, Cell[] excludeCells, List<Cell> refCells) {
        for (int i = 0; i < 9; i++) {
            Cell currentCell = getCells()[i];
            if (Arrays.asList(excludeCells).contains(currentCell)) {
                continue;
            }
            if ((currentCell.getValue() == null) && (currentCell.isSupportCandidate(k))) {
                currentCell.removeDigitFromCandidate(k, this.getClass().getSimpleName(), refCells);
                if (currentCell.getNumberOfCandidates() == 1) {
                    currentCell.resolvedByNakedSingle(refCells);
                }
            }
        }
    }

    public Set<Integer> getPossibleOffset(int k) {
        Set<Integer> possibleOffsets = new HashSet<>(9);
        for (int i = 0; i < 9; i++) {
            if (getCells()[i].isSupportCandidate(k)) {
                possibleOffsets.add(i);
            }
        }
        return possibleOffsets;
    }

    public Cell[] getCells() {
        return cells;
    }

    public void setCells(Cell[] cells) {
        this.cells = cells;
    }

    public SubRegion[] getSubRegion() {
        return subRegion;
    }

    public void setSubRegion(SubRegion[] subRegion) {
        this.subRegion = subRegion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }
}
