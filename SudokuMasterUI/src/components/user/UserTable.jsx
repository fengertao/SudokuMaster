/**
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

import React, { useEffect, useState } from 'react';
import { Icon, message, Popconfirm, Spin, Table } from 'antd';
import RoleTag from './RoleTag';
import { disable, enable, getAll } from '@/axios/UserService';

const UserTable = props => {
    let [selectedRowKeys, setSelectedRowKeys] = useState([]);
    let [loading, setLoading] = useState(false);
    let [data, setData] = useState([]);
    let [filteredInfo, setFilteredInfo] = useState({});
    let [sortedInfo, setSortedInfo] = useState({});

    useEffect(() => {
        setLoading(true);
        getAll()
            .then(resp => {
                setLoading(false);
                setData(resp.data);
            })
            .catch(err => {
                setLoading(false);
                if (err.response && err.response.status) {
                    message.error('无查询权限');
                } else {
                    message.error('查询失败');
                }
            });
    }, []);

    const handleChange = (pagination, filters, sorter) => {
        setFilteredInfo(filters);
        setSortedInfo(sorter);
    };

    const onSelectChange = selectedRowKeys => {
        setSelectedRowKeys(selectedRowKeys);
    };
    const rowSelection = {
        selectedRowKeys,
        onChange: onSelectChange,
    };

    const handleEnableFlag = (event, record) => {
        if (record.roles.filter(role => role.name === 'ROLE_ROOT').length > 0) {
            message.error('禁止对站长操作');
        } else {
            setLoading(true);
            const actionMethod = record.enabled ? disable : enable;

            actionMethod(record.username)
                .then(resp => {
                    setLoading(false);
                    setData(
                        data.map(user => {
                            if (user.username === record.username) {
                                return resp.data;
                            } else {
                                return user;
                            }
                        })
                    );
                    message.warn(record.enabled ? '禁用成功' : '激活成功');
                })
                .catch(ex => {
                    setLoading(false);
                    message.error(ex.response.data);
                });
        }
    };

    const columns = [
        {
            title: '用户名',
            dataIndex: 'username',
            sorter: (a, b) => a.username > b.username,
            sortOrder: sortedInfo.columnKey === 'username' && sortedInfo.order,
        },
        {
            title: '昵称',
            dataIndex: 'fullName',
            sorter: (a, b) => a.fullName > b.fullName,
            sortOrder: sortedInfo.columnKey === 'fullName' && sortedInfo.order,
        },
        {
            title: '邮箱',
            dataIndex: 'email',
            sorter: (a, b) => a.email > b.email,
            sortOrder: sortedInfo.columnKey === 'email' && sortedInfo.order,
        },
        {
            title: '角色',
            dataIndex: 'roles',
            filters: [
                { text: 'Root', value: 'ROLE_ROOT' },
                { text: 'Admin', value: 'ROLE_ADMIN' },
                { text: 'Vip', value: 'ROLE_VIP' },
                { text: 'User', value: 'ROLE_USER' },
                { text: 'Applicant', value: 'ROLE_APPLICANT' },
                { text: 'Guest', value: 'ROLE_ANONYMOUS' },
            ],
            filteredValue: filteredInfo.roles || null,
            onFilter: (value, record) => record.roles.filter(role => role.name === value).length,
            render: roles => (
                <span>
                    {roles.map(role => (
                        <RoleTag key={role.name} role={role} />
                    ))}
                </span>
            ),
        },
        {
            title: '激活',
            dataIndex: 'enabled',
            render: (enabled, record) => (
                <Popconfirm
                    title={enabled ? '确认禁用?' : '确认激活?'}
                    onConfirm={e => {
                        handleEnableFlag(e, record);
                    }}
                    okText="是"
                    cancelText="否"
                >
                    <Icon type={enabled ? 'check-circle' : 'close-circle'} theme="twoTone" />
                </Popconfirm>
            ),
            filters: [{ text: '已激活', value: true }, { text: '未激活', value: false }],
            filteredValue: filteredInfo.enabled || null,
            onFilter: (value, record) => record.enabled === value,
        },
    ];

    return (
        <Spin spinning={loading} size="large">
            <Table
                rowSelection={rowSelection}
                columns={columns}
                dataSource={data}
                rowKey="username"
                onChange={handleChange}
            />
        </Spin>
    );
};
export default UserTable;
