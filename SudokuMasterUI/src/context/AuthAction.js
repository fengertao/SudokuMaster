/**
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

const INITIAL = 'intial';
const MOCK = 'mock';
const LOGIN_SUCCESSFUL = 'loginSuccessful';
const LOGOUT = 'logout';

const initial = () => ({
    type: INITIAL,
});

const mock = () => ({
    type: MOCK,
});

const login = loginJson => ({
    type: LOGIN_SUCCESSFUL,
    data: loginJson,
});

const logout = auth => ({
    type: LOGOUT,
    auth: auth,
});

export { INITIAL, MOCK, LOGIN_SUCCESSFUL, LOGOUT, initial, mock, login, logout };
