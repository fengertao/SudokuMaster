/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.master.method;

import charlie.feng.game.sudokumasterserv.master.Cell;
import charlie.feng.game.sudokumasterserv.master.Grid;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Note: Strong Weak chain with only 1 digit called XChain.
 * For multi-digit chains, see MethodStrongWeakChain (to be implemented).
 * <p>
 * ref: http://www.sudokufans.org.cn/forums/topic/38/#comment-157
 * https://hodoku.sourceforge.net/en/tech_chains.php
 */

public class MethodXChain implements IMethod {

    @Override
    public void apply(Grid grid) {        // For each digit 1-9
        for (int digit = 1; digit <= 9; digit++) {
            // Find all cells that have this digit as a candidate
            List<Cell> cellsWithDigit = findCellsWithDigit(grid, digit);

            // Find strong links (cells that are the only two possible positions for the digit in a unit)
            List<StrongLink> strongLinks = findStrongLinks(grid, cellsWithDigit, digit);

            // Find weak links (cells that share a unit and can't both contain the digit)
            List<WeakLink> weakLinks = findWeakLinks(grid, cellsWithDigit, digit);

            // Try to form chains and eliminate candidates
            findAndApplyChains(grid, strongLinks, weakLinks, digit);
        }
    }

    private List<Cell> findCellsWithDigit(Grid grid, int digit) {
        List<Cell> cells = new ArrayList<>();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Cell cell = grid.getCells()[row][col];
                if (cell.getValue() == null && cell.isSupportCandidate(digit)) {
                    cells.add(cell);
                }
            }
        }
        return cells;
    }

    private List<StrongLink> findStrongLinks(Grid grid, List<Cell> cellsWithDigit, int digit) {
        List<StrongLink> strongLinks = new ArrayList<>();

        // Check rows
        for (int row = 0; row < 9; row++) {
            List<Cell> rowCells = new ArrayList<>();
            for (Cell cell : cellsWithDigit) {
                if (cell.getRowId() == row) {
                    rowCells.add(cell);
                }
            }
            if (rowCells.size() == 2) {
                strongLinks.add(new StrongLink(rowCells.get(0), rowCells.get(1), digit));
            }
        }

        // Check columns
        for (int col = 0; col < 9; col++) {
            List<Cell> colCells = new ArrayList<>();
            for (Cell cell : cellsWithDigit) {
                if (cell.getColumnId() == col) {
                    colCells.add(cell);
                }
            }
            if (colCells.size() == 2) {
                strongLinks.add(new StrongLink(colCells.get(0), colCells.get(1), digit));
            }
        }

        // Check blocks
        for (int block = 0; block < 9; block++) {
            List<Cell> blockCells = new ArrayList<>();
            for (Cell cell : cellsWithDigit) {
                if (cell.getRowId() / 3 * 3 + cell.getColumnId() / 3 == block) {
                    blockCells.add(cell);
                }
            }
            if (blockCells.size() == 2) {
                strongLinks.add(new StrongLink(blockCells.get(0), blockCells.get(1), digit));
            }
        }

        return strongLinks;
    }

    private List<WeakLink> findWeakLinks(Grid grid, List<Cell> cellsWithDigit, int digit) {
        List<WeakLink> weakLinks = new ArrayList<>();

        // Check all pairs of cells
        for (int i = 0; i < cellsWithDigit.size(); i++) {
            for (int j = i + 1; j < cellsWithDigit.size(); j++) {
                Cell cell1 = cellsWithDigit.get(i);
                Cell cell2 = cellsWithDigit.get(j);

                // Check if cells share a unit (row, column, or block)
                if (cell1.getRowId() == cell2.getRowId()
                    || cell1.getColumnId() == cell2.getColumnId()
                    || ((cell1.getRowId() / 3 == cell2.getRowId() / 3
                    && cell1.getColumnId() / 3 == cell2.getColumnId() / 3))) {
                    weakLinks.add(new WeakLink(cell1, cell2, digit));
                }
            }
        }

        return weakLinks;
    }

    private void findAndApplyChains(Grid grid, List<StrongLink> strongLinks, List<WeakLink> weakLinks, int digit) {
        // Try to form chains starting from each strong link
        for (StrongLink startLink : strongLinks) {
            Set<Cell> visitedCells = new HashSet<>();
            List<Cell> chain = new ArrayList<>();
            chain.add(startLink.cell1);
            chain.add(startLink.cell2);
            visitedCells.add(startLink.cell1);
            visitedCells.add(startLink.cell2);

            // Start with a strong link, so next should be weak
            extendChain(grid, chain, visitedCells, strongLinks, weakLinks, digit, false);
        }
    }

    private void extendChain(Grid grid, List<Cell> chain, Set<Cell> visitedCells,
                             List<StrongLink> strongLinks, List<WeakLink> weakLinks,
                             int digit, boolean needStrongLink) {
        Cell lastCell = chain.get(chain.size() - 1);

        if (needStrongLink) {
            // Try to find a strong link from the last cell
            for (StrongLink strongLink : strongLinks) {
                if (strongLink.cell1 == lastCell && !visitedCells.contains(strongLink.cell2)) {
                    chain.add(strongLink.cell2);
                    visitedCells.add(strongLink.cell2);
                    checkAndApplyChain(grid, chain, digit);
                    // After strong link, we need a weak link
                    extendChain(grid, chain, visitedCells, strongLinks, weakLinks, digit, false);
                    chain.remove(chain.size() - 1);
                    visitedCells.remove(strongLink.cell2);
                } else if (strongLink.cell2 == lastCell && !visitedCells.contains(strongLink.cell1)) {
                    chain.add(strongLink.cell1);
                    visitedCells.add(strongLink.cell1);
                    checkAndApplyChain(grid, chain, digit);
                    // After strong link, we need a weak link
                    extendChain(grid, chain, visitedCells, strongLinks, weakLinks, digit, false);
                    chain.remove(chain.size() - 1);
                    visitedCells.remove(strongLink.cell1);
                }
            }
        } else {
            // Try to find a weak link from the last cell
            for (WeakLink weakLink : weakLinks) {
                if (weakLink.cell1 == lastCell && !visitedCells.contains(weakLink.cell2)) {
                    chain.add(weakLink.cell2);
                    visitedCells.add(weakLink.cell2);
                    checkAndApplyChain(grid, chain, digit);
                    // After weak link, we need a strong link
                    extendChain(grid, chain, visitedCells, strongLinks, weakLinks, digit, true);
                    chain.remove(chain.size() - 1);
                    visitedCells.remove(weakLink.cell2);
                } else if (weakLink.cell2 == lastCell && !visitedCells.contains(weakLink.cell1)) {
                    chain.add(weakLink.cell1);
                    visitedCells.add(weakLink.cell1);
                    checkAndApplyChain(grid, chain, digit);
                    // After weak link, we need a strong link
                    extendChain(grid, chain, visitedCells, strongLinks, weakLinks, digit, true);
                    chain.remove(chain.size() - 1);
                    visitedCells.remove(weakLink.cell1);
                }
            }
        }
    }

    private void checkAndApplyChain(Grid grid, List<Cell> chain, int digit) {
        // If we have a chain of length >= 4 and even length (to end with a strong link)
        if (chain.size() >= 4 && chain.size() % 2 == 0) {
            Cell startCell = chain.get(0);
            Cell endCell = chain.get(chain.size() - 1);

            // Check all cells in the grid that could potentially be eliminated
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    Cell targetCell = grid.getCells()[row][col];

                    // Skip cells that are part of the chain or already filled
                    if (targetCell == startCell || targetCell == endCell
                        || chain.contains(targetCell) || targetCell.getValue() != null) {
                        continue;
                    }

                    // Skip cells that don't have the digit as a candidate
                    if (!targetCell.isSupportCandidate(digit)) {
                        continue;
                    }

                    // Check if target cell can see both start and end cells
                    boolean canSeeStart = canSeeCell(targetCell, startCell);
                    boolean canSeeEnd = canSeeCell(targetCell, endCell);

                    if (canSeeStart && canSeeEnd) {
                        targetCell.removeDigitFromCandidate(digit, this.getClass().getSimpleName(), chain);
                    }
                }
            }
        }
    }

    private boolean canSeeCell(Cell cell1, Cell cell2) {
        // Check if cells are in the same row
        if (cell1.getRowId() == cell2.getRowId()) {
            return true;
        }

        // Check if cells are in the same column
        if (cell1.getColumnId() == cell2.getColumnId()) {
            return true;
        }

        // Check if cells are in the same block
        if (cell1.getRowId() / 3 == cell2.getRowId() / 3
            && cell1.getColumnId() / 3 == cell2.getColumnId() / 3) {
            return true;
        }

        return false;
    }

    private static class StrongLink {
        private final Cell cell1;
        private final Cell cell2;
        private final int digit;

        StrongLink(Cell cell1, Cell cell2, int digit) {
            this.cell1 = cell1;
            this.cell2 = cell2;
            this.digit = digit;
        }
    }

    private static class WeakLink {
        private final Cell cell1;
        private final Cell cell2;
        private final int digit;

        WeakLink(Cell cell1, Cell cell2, int digit) {
            this.cell1 = cell1;
            this.cell2 = cell2;
            this.digit = digit;
        }
    }
}
