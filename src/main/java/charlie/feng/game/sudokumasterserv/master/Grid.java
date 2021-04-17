/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Grid {
    public static final String EMPTY_GRID = "000000000000000000000000000000000000000000000000000000000000000000000000000000000";
    private String id;
    private Cell[][] cells;
    private Row[] rows;
    private Column[] columns;
    private Block[] blocks;
    private boolean wrongGrid;

    public boolean isWrongGrid() {
        return wrongGrid;
    }

    //for debug purpose only
    private Grid expectedAnswer;
    private Resolution resolution = new Resolution();
    private boolean isChangedInCycle;

    public Grid(String id) {
        this.setId(id);
        load(id);
    }

    public Grid(String id, String position) {
        this.setId(id);
        load(id, position);
    }

    public void load(String gridId) {
        if ((gridId == null) || gridId.length() != 81) {
            throw new IllegalArgumentException("Wrong grid id:" + id);
        }

        setCells(new Cell[9][9]);
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                String valueStr = gridId.substring(r * 9 + c, r * 9 + c + 1);
                getCells()[r][c] = new Cell(this, r, c, r / 3 * 3 + c / 3, valueStr);
            }
        }

        initialRegion();
    }

    private void load(String gridId, String position) {
        if ((gridId == null) || gridId.length() != 81) {
            throw new IllegalArgumentException("Wrong grid id: " + gridId);
        }

        String[] cellPositionValues = position.split("[|]", 81);
        if (cellPositionValues.length != 81) {
            throw new IllegalArgumentException("Wrong position: " + position);
        }

        setCells(new Cell[9][9]);
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (cellPositionValues[r * 9 + c].length() == 0) {
                    getCells()[r][c] = new Cell(this, r, c, r / 3 * 3 + c / 3, "0");
                } else if (cellPositionValues[r * 9 + c].length() == 1) {
                    getCells()[r][c] = new Cell(this, r, c, r / 3 * 3 + c / 3, cellPositionValues[r * 9 + c]);
                } else if (cellPositionValues[r * 9 + c].length() <= 8) {
                    getCells()[r][c] = new Cell(this, r, c, r / 3 * 3 + c / 3, "0", cellPositionValues[r * 9 + c]);
                } else {
                    throw new IllegalArgumentException(String.format("position %s have more than 8 candidates in cell (%d,%d): %s", position, r + 1, c + 1, cellPositionValues[r * 9 + c]));
                }
            }
        }

        initialRegion();
    }

    private void initialRegion() {
        setRows(new Row[9]);
        for (int r = 0; r < 9; r++) {
            getRows()[r] = new Row(this, r);
        }

        setColumns(new Column[9]);
        for (int c = 0; c < 9; c++) {
            getColumns()[c] = new Column(this, c);
        }

        setBlocks(new Block[9]);
        for (int i = 0; i < 9; i++) {
            getBlocks()[i] = new Block(this, i);
        }
    }

    public Resolution getResolution() {
        return resolution;
    }

    public int getNumberOfResolvedCells() {
        int resolvedCells = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (getCells()[i][j].getValue() != null) {
                    resolvedCells++;
                }
            }
        }
        return resolvedCells;
    }

    public boolean isResolved() {
        return (getNumberOfResolvedCells() == 81);
    }

    public void validate() {
        try {
            for (int i = 0; i < 9; i++) {
                getRows()[i].validate();
                getColumns()[i].validate();
                getBlocks()[i].validate();
            }
        } catch (Exception e) {
            //Todo Log exception here
            wrongGrid = true;
        }
    }

    public String getAnswer() {
        StringBuilder builder = new StringBuilder();
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Integer value = getCells()[r][c].getValue();
                builder.append(value == null ? "0" : value);
            }
        }
        return builder.toString();
    }

    public String getPosition() {
        StringBuilder builder = new StringBuilder();
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Integer value = getCells()[r][c].getValue();
                if (value != null) {
                    builder.append(value);
                } else {
                    builder.append(getCells()[r][c].getCandidateString());
                }
                if ((r != 8) || (c != 8)) {
                    builder.append("|");
                }
            }
        }
        return builder.toString();
    }

    public static JsonArray validateGridId(String gridId) {
        //Todo validate there is no duplicated digital in row, column, block
        JsonArray errorList = new JsonArray();
        if ((gridId.length() != 81) || !(gridId.matches("[0-9]{81}"))) {
            errorList.add("数独盘面由81位数字组成。空格请输入0。");
        }
        return errorList;
    }

    public JsonArray validatePosition(String position, boolean skipValidateNonResolvedGrid) {
        JsonArray errorList = new JsonArray();
        if (!isResolved() && skipValidateNonResolvedGrid) {
            return errorList;
        }
        String[] positionCells = position.split("[|]", 81);
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                String positionValue = positionCells[r * 9 + c].length() == 0 ? "123456789" : positionCells[r * 9 + c];
                if (getCells()[r][c].getValue() != null) {
                    String correctValue = String.valueOf(getCells()[r][c].getValue());
                    if (!positionValue.contains(correctValue)) {
                        errorList.add(String.format("单元格 (%d,%d) 内输入为 %s, 但其正确解应该为 %s。", r + 1, c + 1, positionValue, correctValue));
                    }
                } else {
                    for (Integer candidate : getCells()[r][c].getCandidateList()) {
                        if (!positionValue.contains(String.valueOf(candidate))) {
                            errorList.add(String.format("单元格 (%d,%d) 内输入为 %s, 但其正确范围应该为 %s。", r + 1, c + 1, positionValue, getCells()[r][c].getCandidateString()));
                            break;
                        }
                    }
                }
            }
        }
        return errorList;
    }

    public JsonObject getJsonResult() {
        JsonObject result = new JsonObject();
        result.addProperty("resolved", isResolved() && !isWrongGrid());
        result.addProperty("msg", isWrongGrid() ? "数独盘面错误，请检查输入的盘面。" : null);
        result.addProperty("answer", isResolved() ? getAnswer() : getPosition());
        result.add("resolution", getResolution().getJson("ZH"));
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }

    public Row[] getRows() {
        return rows;
    }

    public void setRows(Row[] rows) {
        this.rows = rows;
    }

    public Column[] getColumns() {
        return columns;
    }

    public void setColumns(Column[] columns) {
        this.columns = columns;
    }

    public Block[] getBlocks() {
        return blocks;
    }

    public void setBlocks(Block[] blocks) {
        this.blocks = blocks;
    }

    public Grid getExpectedAnswer() {
        return expectedAnswer;
    }

    public void setExpectedAnswer(Grid expectedAnswer) {
        this.expectedAnswer = expectedAnswer;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    public boolean isChangedInCycle() {
        return isChangedInCycle;
    }

    public void setChangedInCycle(boolean changedInCycle) {
        isChangedInCycle = changedInCycle;
    }

    public enum PlayResult {
        RESOLVED,
        UNRESOLVED,
        WRONG_GRID
    }
}


