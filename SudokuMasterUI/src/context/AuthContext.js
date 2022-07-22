/**
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

import React from 'react';
import { setupAxiosInterceptors, teardownAxiosInterceptors } from '@/axios/AuthService';
import * as AuthAction from './AuthAction';

const AuthContext = React.createContext();

const initialState = {};

const mockState = {
    username: 'charlie',
    token:
        'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaGFybGllIiwiZXhwIjoxNTc0MTQwNDc3LCJpYXQiOjE1NzM1MzU2Nzd9.gWUscG-CoD3SxmM_yW-KI21B-TFKoSAncqQkaOanZhYIZhdQMaJxyETvZtGk78Vc54WSbghKLbYN6JXDzequfA',
    permissions: ['ROLE_ROOT', 'ROLE_ADMIN'],
};

let reducer = (state, action) => {
    switch (action.type) {
        case AuthAction.INITIAL:
            return initialState;
        case AuthAction.MOCK:
            const interceptor = setupAxiosInterceptors(mockState.token);
            return { ...mockState, interceptor: interceptor };
        case AuthAction.LOGIN_SUCCESSFUL: {
            const interceptor = setupAxiosInterceptors(action.data.token);
            return {
                username: action.data.username,
                permissions: action.data.roles,
                token: action.data.token,
                interceptor: interceptor,
            };
        }
        case AuthAction.LOGOUT:
            //Todo logout from backend
            if (action.data && action.data.interceptor != null) {
                teardownAxiosInterceptors(action.data.interceptor);
            }
            return initialState;
        default:
            return state;
    }
};

function AuthProvider(props) {
    let [state, dispatch] = React.useReducer(reducer, initialState);
    let value = { state, dispatch };
    return <AuthContext.Provider value={value}>{props.children}</AuthContext.Provider>;
}

let AuthConsumer = AuthContext.Consumer;

export { AuthContext, AuthProvider, AuthConsumer };
