/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private static Logger logger = LoggerFactory.getLogger(Cell.class);
    public int rowId;
    public int columnId;
    private Integer value;
    private boolean[] candidates;
    private int blockId;
    private Grid grid;
    private boolean noticedNeighbor = false;

    public Cell(Grid grid, int r, int c, int b, String value) {
        this.grid = grid;
        this.rowId = r;
        this.columnId = c;
        this.blockId = b;
        candidates = new boolean[9];
        if ((value == null) || (value.equals("0"))) {
            this.value = null;
            for (int k = 0; k < 9; k++) {
                candidates[k] = true;
            }
        } else {
            this.value = Integer.valueOf(value);
            for (int k = 0; k < 9; k++) {
                candidates[k] = k + 1 == this.value;
            }
        }
    }

    public Integer getValue() {
        return this.value;
    }

    public int getNumberOfCandidates() {
        int totalCandidates = 0;
        for (int k = 0; k < 9; k++) {
            if (candidates[k]) {
                totalCandidates++;
            }
        }
        return totalCandidates;
    }

    public List<Integer> getCandidateList() {
        List<Integer> candidateList = new ArrayList<>(9);
        for (int k = 0; k < 9; k++) {
            if (candidates[k]) {
                candidateList.add(k + 1);
            }
        }
        return candidateList;
    }

    public String getCandidateString() {
        StringBuilder builder = new StringBuilder();
        for (int k = 0; k < 9; k++) {
            if (candidates[k]) {
                builder.append(k + 1);
            }
        }
        return builder.toString();
    }

    public String locationString() {
        return "(" + (rowId + 1) + "," + (columnId + 1) + ")";
    }


    public boolean isSupportCandidate(Integer digit) {
        return candidates[digit - 1];
    }

    public void removeDigitFromCandidate(Integer digit, String methodName, List<Cell> refCells) {
        validateRemoveDigitFromCandidate(digit);
        if (!candidates[digit - 1]) {
            return;
        }
        grid.isChangedInCycle = true;
        candidates[digit - 1] = false;
        grid.resolution.logStep(this, refCells, grid.getPosition(), methodName, MsgKey.REMOVE_CANDIDATE, "" + digit, getCandidateString());
        if (getNumberOfCandidates() == 1) {
            gainValue(getCandidateList().get(0), methodName, refCells);
        }
    }

    public void resolvedByNakedSingle(List<Cell> refCells) {
        int digit = 0;
        int candidatesInCell = 0;
        for (int k = 0; k < 9; k++) {
            if (candidates[k]) {
                digit = k + 1;
                candidatesInCell++;
            }
        }

        if (candidatesInCell != 1) {
            grid.dump();
            throw new RuntimeException("Wrong resolvedByNakedSingle, row:" + rowId + " column:" + columnId);
        } else {
            gainValue(digit, "MethodSoleNumber", refCells);
        }
    }

    //Todo method name
    public void gainValue(int digit, String methodName, List<Cell> refCells) {
        validateGainValue(digit);
        if (value != null)
            return;
        grid.isChangedInCycle = true;
        grid.resolution.logStep(this, refCells, grid.getPosition(), methodName, MsgKey.GET_VALUE, "" + digit);
        value = digit;
        for (int k2 = 0; k2 < 9; k2++) {
            if (k2 != digit - 1) {
                candidates[k2] = false;
            }
        }

        logger.trace("Cell resolved. row:" + rowId + " col:" + columnId + " value:" + value + " " + methodName);
        noticeNeighboorsAboutValueGained();
    }

    private void validateRemoveDigitFromCandidate(Integer digit) {
        //Todo validate more ?
        if (grid.expectedAnswer != null) {
            Cell expectedCell = grid.expectedAnswer.cells[rowId][columnId];
            if (expectedCell.getValue().equals(digit)) {
                throw new RuntimeException(
                        "Should not remove from candidate: Row:" + rowId + " Col:" + columnId + " Excepted value:" + expectedCell.getValue()
                                + " input value:" + digit);
            }
        }
    }

    private void validateGainValue(int digit) {
        //Todo validate the value is existing in row/col/block
        if (grid.expectedAnswer != null) {
            Cell expectedCell = grid.expectedAnswer.cells[rowId][columnId];
            if (expectedCell.getValue() != digit) {
                throw new RuntimeException(
                        "Wrong value: Row:" + rowId + " Col:" + columnId + " Excepted value:" + expectedCell.getValue() + " input value:" + digit);
            }
        }
        if ((value != null) && (value != digit)) {
            throw new RuntimeException("Conflicted value: Row:" + rowId + " Col:" + columnId + " existing value:" + value + " input value:" + digit);
        }
    }

    public void noticeNeighboorsAboutValueGained() {
        if (value == null) {
            grid.dump();
            throw new RuntimeException("Wrong noticeNeighboorsAboutValueGained, row:" + rowId + " column:" + columnId);
        }
        if (!noticedNeighbor) {
            noticedNeighbor = true;
            grid.rows[rowId].onCellResolved(this);
            grid.columns[columnId].onCellResolved(this);
            grid.blocks[blockId].onCellResolved(this);
        }
    }

}
