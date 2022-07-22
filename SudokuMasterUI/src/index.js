/**
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

import React from 'react';
import ReactDOM from 'react-dom';
import * as serviceWorker from './serviceWorker';
import Page from './Page';
import { AuthProvider } from '@/context/AuthContext';
import { ResponsiveProvider } from '@/context/ResponsiveContext';
import './style/lib/animate.css';
import './style/antd/index.less';
import './style/index.less';

ReactDOM.render(
    <AuthProvider>
        <ResponsiveProvider>
            <Page />
        </ResponsiveProvider>
    </AuthProvider>,
    document.getElementById('root')
);
// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: http://bit.ly/CRA-PWA
// serviceWorker.unregister();
serviceWorker.register();
