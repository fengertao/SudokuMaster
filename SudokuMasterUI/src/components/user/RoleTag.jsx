/**
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

import React from 'react';
import { Tag } from 'antd';

const RoleTag = props => {
    const role = props.role;
    let color;
    let title = role.description;
    switch (role.name) {
        case 'ROLE_ROOT':
            color = 'purple';
            break;
        case 'ROLE_ADMIN':
            color = 'geekblue';
            break;
        case 'ROLE_VIP':
            color = 'blue';
            break;
        case 'ROLE_USER':
            color = 'green';
            break;
        case 'ROLE_APPLICANT':
            color = 'volcano';
            break;
        case 'ROLE_ANONYMOUS':
            color = 'red';
            break;
        default:
            color = 'red';
            title = 'unknow';
            break;
    }
    return (
        <Tag color={color} key={role.name}>
            {title}
        </Tag>
    );
};

export default RoleTag;
