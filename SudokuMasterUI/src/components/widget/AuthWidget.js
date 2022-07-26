/**
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

import { useContext } from 'react';
import { AuthContext } from '@/context/AuthContext';

const AuthWidget = props => {
    const { state: authState } = useContext(AuthContext);

    return props.children(authState || {});
};
export default AuthWidget;
