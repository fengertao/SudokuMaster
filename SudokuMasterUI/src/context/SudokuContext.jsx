/**
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

import React from 'react';
import * as SudokuAction from './SudokuAction';

const SudokuContext = React.createContext();

const DEMO_GRID =
    '000000018948007050000008020053702000009000000000901430090600000030500876060000000';

const parseCellsFromGrid = gridId =>
    Array.from(gridId).map(i => {
        return i === '0' ? '' : i;
    });

const initialState = {
    gridId: DEMO_GRID, //initial gridId
    position: null, //position loaded from database
    resolved: false,
    originalCells: Array.from(DEMO_GRID), //the cells of original grid
    cells: parseCellsFromGrid(DEMO_GRID), //the candidate of each cell.
    answer: null,
};

let reducer = (state, action) => {
    switch (action.type) {
        case SudokuAction.INITIAL:
            return initialState;

        case SudokuAction.UPDATE_RESULT: {
            return {
                ...state,
                resolved: action.result.resolved,
                cells: action.result.resolved
                    ? parseCellsFromGrid(action.result.answer)
                    : action.result.answer.split('|'),
                answer: action.result.answer,
            };
        }

        case SudokuAction.LOAD_GRID:
            return {
                gridId: action.gridId,
                position: null,
                resolved: false,
                originalCells: Array.from(action.gridId),
                cells: parseCellsFromGrid(action.gridId),
                answer: null,
            };

        case SudokuAction.LOAD_POSITION:
            return {
                gridId: action.gridId,
                position: action.position,
                resolved: false,
                originalCells: Array.from(action.gridId),
                cells: action.position.split('|'),
                answer: null,
            };

        case SudokuAction.UPDATE_CELL_CANDIDATE:
            state.cells[action.index] = action.candidate;
            return {
                ...state,
                cells: state.cells,
            };

        default:
            return state;
    }
};

function SudokuProvider(props) {
    let [state, dispatch] = React.useReducer(reducer, initialState);
    let value = { state, dispatch };
    return <SudokuContext.Provider value={value}>{props.children}</SudokuContext.Provider>;
}

let SudokuConsumer = SudokuContext.Consumer;

export { SudokuContext, SudokuProvider, SudokuConsumer };
