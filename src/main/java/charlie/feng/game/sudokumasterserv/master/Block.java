/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master;

import java.util.Set;

public class Block extends AbstractRegion {

    private int blockR;
    private int blockC;
    private SubRegion[] subRows;
    private SubRegion[] subColumns;

    public Block(Grid grid, int no) {
        this.setGrid(grid);
        this.setId(no);
        setCells(new Cell[9]);
        setSubRows(new SubRow[]{
                new SubRow(),
                new SubRow(),
                new SubRow()
        });
        setSubColumns(new SubColumn[]{
                new SubColumn(),
                new SubColumn(),
                new SubColumn()
        });

        for (int i = 0; i < 3; i++) {
            getSubRows()[i].setCells(new Cell[3]);
            getSubColumns()[i].setCells(new Cell[3]);
        }

        setBlockR(no / 3);
        setBlockC(no % 3);
        for (int i = 0; i < 9; i++) {
            int offsetR = i / 3;
            int offsetC = i % 3;
            Cell currentCell = grid.getCells()[getBlockR() * 3 + offsetR][getBlockC() * 3 + offsetC];
            getCells()[i] = currentCell;
            getSubRows()[offsetR].getCells()[offsetC] = currentCell;
            getSubColumns()[offsetC].getCells()[offsetR] = currentCell;
        }
    }

    @Override
    protected MsgKey getMsgKeyForValueExistType() {
        return MsgKey.VALUE_IN_SAME_BLOCK;
    }

    public Set<Integer> getPossibleSubRegion(int k, boolean isRow, Set<Integer> resultSet) {
        resultSet.clear();
        for (int i = 0; i < 3; i++) {
            SubRegion subRegion = isRow ? getSubRows()[i] : getSubColumns()[i];
            if (subRegion.supportNumber(k)) {
                resultSet.add(i);
            }
        }
        return resultSet;
    }

    public int getBlockR() {
        return blockR;
    }

    public void setBlockR(int blockR) {
        this.blockR = blockR;
    }

    public int getBlockC() {
        return blockC;
    }

    public void setBlockC(int blockC) {
        this.blockC = blockC;
    }

    public SubRegion[] getSubRows() {
        return subRows;
    }

    public void setSubRows(SubRegion[] subRows) {
        this.subRows = subRows;
    }

    public SubRegion[] getSubColumns() {
        return subColumns;
    }

    public void setSubColumns(SubRegion[] subColumns) {
        this.subColumns = subColumns;
    }
}
