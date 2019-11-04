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

    public void validate() {
        for (int i = 0; i < 9; i++) {
            rows[i].validate();
            columns[i].validate();
            blocks[i].validate();
        }
    }

    public void dump() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Integer value = cells[r][c].getValue();
                System.out.print((value == null ? " " : value.toString()));
            }
            System.out.println();
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

    public String getIncompleteAnswer() {
        StringBuilder builder = new StringBuilder();
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Integer value = cells[r][c].getValue();
                if (value != null) {
                    builder.append(value);
                } else {
                    builder.append((char) (cells[r][c].getNumberOfCandidates() + 0x60));
                }

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

    public JSONObject getJsonResult() throws JSONException {
        JSONObject result = new JSONObject();
        result.put("resolved", isResolved());
        result.put("answer", isResolved() ? getAnswer() : getIncompleteAnswer());
        JSONArray array = new JSONArray();
        result.put("resolution", resolution.getJson("ZH"));
        return result;
    }
}
