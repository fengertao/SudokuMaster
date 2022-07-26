/**
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

import React from 'react';
import { Row, Col, Card } from 'antd';
import UserTablePageable from './UserTablePageable';
import BreadcrumbCustom from '../BreadcrumbCustom';

const UserPageable = props => (
    <div className="gutter-example">
        <BreadcrumbCustom first="用户" />
        <Row gutter={16}>
            <Col className="gutter-row" md={14}>
                <div className="gutter-box">
                    <Card title="用户列表" bordered={false}>
                        <UserTablePageable />
                    </Card>
                </div>
            </Col>
            <Col className="gutter-row" md={10}>
                <div className="gutter-box">
                    <Card title="编辑用户" bordered={false} />
                </div>
            </Col>
        </Row>
    </div>
);

export default UserPageable;
