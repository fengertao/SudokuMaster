/**
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

const INITIAL = 'intial';
const UPDATE_RESULT = 'updateResult';

const initial = () => ({
    type: INITIAL,
});

const updateResult = result => ({
    type: UPDATE_RESULT,
    result,
});

export { INITIAL, UPDATE_RESULT, initial, updateResult };
