/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

import React from 'react';
import * as ResponsiveAction from './ResponsiveAction';

const ResponsiveContext = React.createContext();

const initialState = {
    isMobile: false,
};

let reducer = (state, action) => {
    switch (action.type) {
        case ResponsiveAction.SET_MOBILE_FLAG:
            return {isMobile: action.data.isMobile};
        default:
            return state;
    }
};

function ResponsiveProvider(props) {
    let [state, dispatch] = React.useReducer(reducer, initialState);
    let value = { state, dispatch };
    return <ResponsiveContext.Provider value={value}>{props.children}</ResponsiveContext.Provider>;
}

let ResponsiveConsumer = ResponsiveContext.Consumer;

export { ResponsiveContext, ResponsiveProvider, ResponsiveConsumer };
