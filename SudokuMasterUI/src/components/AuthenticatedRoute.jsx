/**
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

import React, { useContext } from 'react';
import { Route, Redirect } from 'react-router-dom';
import { isUserLoggedIn, loginAsMockUser } from '@/axios/AuthService';
import { AuthContext } from '@/context/AuthContext';

const AuthenticatedRoute = props => {
    const { state: authState, dispatch } = useContext(AuthContext);

    if (isUserLoggedIn(authState)) {
        return <Route {...props} />;
    } else if (process.env.NODE_ENV !== 'production') {
        loginAsMockUser(dispatch);
        return <Route {...props} />;
    } else {
        return <Redirect to="/login" />;
    }
};

export default AuthenticatedRoute;
