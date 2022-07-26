/**
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

import axios from 'axios';
import { MOCK, LOGOUT } from '@/context/AuthAction';
import { LOGIN_URL } from '@/axios/config';

export const isUserLoggedIn = authState => {
    return authState && authState.username ? true : false;
};

export const loginAsMockUser = dispatch => {
    dispatch({ type: MOCK });
};

export const login = loginJson => {
    return axios.post(LOGIN_URL, loginJson, {
        headers: {
            'Content-Type': 'application/json',
        },
    });
};

export const logout = (authState, dispatch) => {
    const dispatchResult = dispatch({ type: LOGOUT, data: authState });
    return dispatchResult;
};

export const setupAxiosInterceptors = token => {
    const interceptor = axios.interceptors.request.use(config => {
        config.headers.authorization = createJWTToken(token);
        return config;
    });
    return interceptor;
};

const createJWTToken = token => {
    return 'Bearer ' + token;
};

export const teardownAxiosInterceptors = interceptor => {
    axios.interceptors.request.eject(interceptor);
};
