/**
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

const INITIAL = 'intial';
const UPDATE_RESULT = 'updateResult';
const ADD_STEPS = 'addtep';
const LOAD_GRID = 'loadGrid';
const LOAD_POSITION = 'loadPosition';
const UPDATE_CELL_CANDIDATE = 'updateCellCandidate';

const initial = () => ({
    type: INITIAL,
});

const updateResult = result => ({
    type: UPDATE_RESULT,
    result,
});

const addSteps = (position, steps) => ({
    type: LOAD_GRID,
    position,
    steps,
});

const loadGrid = gridId => ({
    type: LOAD_GRID,
    gridId,
});

const loadPosition = (gridId, position) => ({
    type: LOAD_POSITION,
    gridId,
    position,
});

const updateCellCandidate = (index, candidate) => ({
    type: UPDATE_CELL_CANDIDATE,
    index,
    candidate,
});

export {
    INITIAL,
    UPDATE_RESULT,
    ADD_STEPS,
    LOAD_GRID,
    LOAD_POSITION,
    UPDATE_CELL_CANDIDATE,
    initial,
    updateResult,
    addSteps,
    loadGrid,
    loadPosition,
    updateCellCandidate,
};
