/**
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

import React from 'react';
import * as SudokuSolutionAction from './SudokuSolutionAction';

const SudokuSolutionContext = React.createContext();

const initialState = {
    gridId: '', //initial gridId
    resolved: false,
    answer: null,
    resolution: [],
};

let reducer = (state, action) => {
    switch (action.type) {
        case SudokuSolutionAction.INITIAL:
            return initialState;

        case SudokuSolutionAction.UPDATE_RESULT: {
            return {
                ...state,
                resolved: action.result.resolved,
                answer: action.result.answer,
                resolution: action.result.resolution,
            };
        }
        default:
            return state;
    }
};

function SudokuSolutionProvider(props) {
    let [state, dispatch] = React.useReducer(reducer, initialState);
    let value = { state, dispatch };
    return (
        <SudokuSolutionContext.Provider value={value}>
            {props.children}
        </SudokuSolutionContext.Provider>
    );
}

let SudokuSolutionConsumer = SudokuSolutionContext.Consumer;

export { SudokuSolutionContext, SudokuSolutionProvider, SudokuSolutionConsumer };
