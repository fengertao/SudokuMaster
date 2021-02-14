/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private static Logger logger = LoggerFactory.getLogger(Cell.class);
    private int rowId;
    private int columnId;
    private Integer value;
    private final boolean[] candidates;
    private final int blockId;
    private final Grid grid;
    private boolean noticedNeighbor = false;

    public Cell(Grid grid, int r, int c, int b, String value) {
        this.grid = grid;
        this.setRowId(r);
        this.setColumnId(c);
        this.blockId = b;
        candidates = new boolean[9];
        if (value == null || "0".equals(value)) {
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

    public Cell(Grid grid, int r, int c, int b, String value, String positionValue) {
        this.grid = grid;
        this.setRowId(r);
        this.setColumnId(c);
        this.blockId = b;
        candidates = new boolean[9];
        if ((value == null) || ("0".equals(value))) {
            this.value = null;
            String positionFullValue = !StringUtils.hasText(positionValue) ? "123456789" : positionValue;
            for (int k = 0; k < 9; k++) {
                candidates[k] = positionFullValue.contains(String.valueOf(k + 1));
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
        String candidatesStr = builder.toString();
        return "123456789".equals(candidatesStr) ? "" : candidatesStr;
    }

    public String locationString() {
        return "(" + (getRowId() + 1) + "," + (getColumnId() + 1) + ")";
    }


    public boolean isSupportCandidate(Integer digit) {
        return candidates[digit - 1];
    }

    /**
     *
     * @param digit
     * @param methodName
     * @param refCells
     * @return true for really remove digit, false for digit doesn't existing before remove
     */
    public boolean removeDigitFromCandidate(Integer digit, String methodName, List<Cell> refCells) {
        validateRemoveDigitFromCandidate(digit);
        if (!candidates[digit - 1]) {
            return false;
        }
        String preChangeCandidates = getCandidateString();
        grid.setChangedInCycle(true);
        candidates[digit - 1] = false;
        grid.getResolution().logStep(this, preChangeCandidates, refCells, grid.getPosition(), methodName, MsgKey.REMOVE_CANDIDATE, "" + digit, getCandidateString());
        if (getNumberOfCandidates() == 1) {
            gainValue(getCandidateList().get(0), methodName, refCells, preChangeCandidates);
        }
        return true;
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
            logger.error("Grid id:" + grid.getId());
            logger.error("Grid Position:" + grid.getPosition());
            throw new RuntimeException(String.format("Wrong resolvedByNakedSingle, %s:", locationString()));
        } else {
            gainValue(digit, "MethodSoleNumber", refCells);
        }
    }

    //If gain value via removeDigitFromCandidate(), removeDigitFromCandidate() should provide preChangeCandidate, because cell have been changed.
    private void gainValue(int digit, String methodName, List<Cell> refCells, String preChangeCandidates) {
        validateGainValue(digit);
        if (value != null) {
            return;
        }
        grid.setChangedInCycle(true);
        value = digit;
        grid.getResolution().logStep(this, preChangeCandidates, refCells, grid.getPosition(), methodName, MsgKey.GET_VALUE, "" + digit);
        for (int k2 = 0; k2 < 9; k2++) {
            if (k2 != digit - 1) {
                candidates[k2] = false;
            }
        }

        logger.trace("Cell resolved. row:" + getRowId() + " col:" + getColumnId() + " value:" + value + " " + methodName);
        noticeNeighboorsAboutValueGained();
    }

    //Todo method name
    public void gainValue(int digit, String methodName, List<Cell> refCells) {
        gainValue(digit, methodName, refCells, getCandidateString());
    }

    /**
     * Try set the value to cell. only used in bruteforce or try related method
     * @param digit
     */
    public void tryValue(int digit) {
        value = digit;
    }

    private void validateRemoveDigitFromCandidate(Integer digit) {
        //Todo validate more ?
        if (grid.getExpectedAnswer() != null) {
            Cell expectedCell = grid.getExpectedAnswer().getCells()[getRowId()][getColumnId()];
            if (expectedCell.getValue().equals(digit)) {
                throw new RuntimeException(
                        "Should not remove from candidate: Row:" + getRowId() + " Col:" + getColumnId() + " Excepted value:" + expectedCell.getValue()
                                + " input value:" + digit);
            }
        }
    }

    private void validateGainValue(int digit) {
        //Todo validate the value is existing in row/col/block
        if (grid.getExpectedAnswer() != null) {
            Cell expectedCell = grid.getExpectedAnswer().getCells()[getRowId()][getColumnId()];
            if (expectedCell.getValue() != digit) {
                throw new RuntimeException(
                        "Wrong value: Row:" + getRowId() + " Col:" + getColumnId() + " Excepted value:" + expectedCell.getValue() + " input value:" + digit);
            }
        }
        if ((value != null) && (value != digit)) {
            throw new RuntimeException("Conflicted value: Row:" + getRowId() + " Col:" + getColumnId() + " existing value:" + value + " input value:" + digit);
        }
    }

    public void noticeNeighboorsAboutValueGained() {
        if (!noticedNeighbor) {
            noticedNeighbor = true;
            grid.getRows()[getRowId()].onCellResolved(this);
            grid.getColumns()[getColumnId()].onCellResolved(this);
            grid.getBlocks()[blockId].onCellResolved(this);
        }
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public int getColumnId() {
        return columnId;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }
}
