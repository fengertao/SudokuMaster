/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Grid {
    public static String EMPTY_GRID = "000000000000000000000000000000000000000000000000000000000000000000000000000000000";
    public String id;
    public Cell[][] cells;
    public Row[] rows;
    public Column[] columns;
    public Block[] blocks;
    public Grid expectedAnswer; //for debug purpose only.
    public Resolution resolution = new Resolution();
    boolean isChangedInCycle;

    public Grid(String id) {
        this.id = id;
        load(id);
    }

    public Grid(String id, String position) {
        this.id = id;
        load(id, position);
    }

    private void load(String id) {
        if ((id == null) || id.length() != 81) {
            throw new IllegalArgumentException("Wrong grid id:" + id);
        }

        cells = new Cell[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                String valueStr = id.substring(r * 9 + c, r * 9 + c + 1);
                cells[r][c] = new Cell(this, r, c, r / 3 * 3 + c / 3, valueStr);
            }
        }

        initialRegion();
    }

    private void load(String id, String position) {
        if ((id == null) || id.length() != 81) {
            throw new IllegalArgumentException("Wrong grid id: " + id);
        }

        String[] cellPositionValues = position.split("[|]", 81);
        if (cellPositionValues.length != 81) {
            throw new IllegalArgumentException("Wrong position: " + position);
        }

        cells = new Cell[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (cellPositionValues[r * 9 + c].length() == 0) {
                    cells[r][c] = new Cell(this, r, c, r / 3 * 3 + c / 3, "0");
                } else if (cellPositionValues[r * 9 + c].length() == 1) {
                    cells[r][c] = new Cell(this, r, c, r / 3 * 3 + c / 3, cellPositionValues[r * 9 + c]);
                } else if (cellPositionValues[r * 9 + c].length() <= 8) {
                    cells[r][c] = new Cell(this, r, c, r / 3 * 3 + c / 3, "0", cellPositionValues[r * 9 + c]);
                } else {
                    throw new IllegalArgumentException(String.format("position %s have more than 8 candidates in cell (%d,%d): %s", position, r + 1, c + 1, cellPositionValues[r * 9 + c]));
                }
            }
        }

        initialRegion();
    }

    private void initialRegion() {
        rows = new Row[9];
        for (int r = 0; r < 9; r++) {
            rows[r] = new Row(this, r);
        }

        columns = new Column[9];
        for (int c = 0; c < 9; c++) {
            columns[c] = new Column(this, c);
        }

        blocks = new Block[9];
        for (int i = 0; i < 9; i++) {
            blocks[i] = new Block(this, i);
        }
    }

    public Resolution getResolution() {
        return resolution;
    }

    public int getNumberOfResolvedCells() {
        int resolvedCells = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (cells[i][j].getValue() != null) {
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
        for (int i = 0; i < 9; i++) {
            rows[i].validate();
            columns[i].validate();
            blocks[i].validate();
        }
    }

    public String getAnswer() {
        StringBuilder builder = new StringBuilder();
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Integer value = cells[r][c].getValue();
                builder.append(value);
            }
        }
        return builder.toString();
    }

    public String getPosition() {
        StringBuilder builder = new StringBuilder();
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Integer value = cells[r][c].getValue();
                if (value != null) {
                    builder.append(value);
                } else {
                    builder.append(cells[r][c].getCandidateString());
                }
                if ((r != 8) || (c != 8)) {
                    builder.append("|");
                }
            }
        }
        return builder.toString();
    }

    public static JSONArray validateGridId(String id) {
        //Todo validate there is no duplicated digital in row, column, block
        JSONArray errorList = new JSONArray();
        if ((id.length() != 81) || !(id.matches("[0-9]{81}"))) {
            errorList.put("数独盘面由81位数字组成。空格请输入0。");
        }
        return errorList;
    }

    public JSONArray validatePosition(String position, boolean skipValidateNonResolvedGrid) {
        JSONArray errorList = new JSONArray();
        if (!isResolved() && skipValidateNonResolvedGrid) {
            return errorList;
        }
        String[] positionCells = position.split("[|]", 81);
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                String positionValue = positionCells[r * 9 + c].length() == 0 ? "123456789" : positionCells[r * 9 + c];
                if (cells[r][c].getValue() != null) {
                    String correctValue = String.valueOf(cells[r][c].getValue());
                    if (!positionValue.contains(correctValue)) {
                        errorList.put(String.format("单元格 (%d,%d) 内输入为 %s, 但其正确解应该为 %s。", r + 1, c + 1, positionValue, correctValue));
                    }
                } else {
                    for (Integer candidate : cells[r][c].getCandidateList() ) {
                        if (!positionValue.contains(String.valueOf(candidate))) {
                            errorList.put(String.format("单元格 (%d,%d) 内输入为 %s, 但其正确范围应该为 %s。", r + 1, c + 1, positionValue, cells[r][c].getCandidateString()));
                            break;
                        }
                    }
                }
            }
        }
        return errorList;
    }

    public JSONObject getJsonResult() throws JSONException {
        JSONObject result = new JSONObject();
        result.put("resolved", isResolved());
        result.put("answer", isResolved() ? getAnswer() : getPosition());
        result.put("resolution", resolution.getJson("ZH"));
        return result;
    }
}
