/**
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

import React, { useState, useEffect } from 'react';
import { Table, Input, Button, Icon, message, Modal } from 'antd';
import GridService from '@/axios/GridService';

const GridTable = props => {
    const { visible, onCancel, onLoadGrid } = props;
    let [loading, setLoading] = useState(false);
    let [data, setData] = useState([]);
    let [filteredInfo, setFilteredInfo] = useState({});
    let [, setSearchText] = useState('');
    let [, setSortedInfo] = useState({});
    let searchInput;

    useEffect(() => {
        if (!visible) return;
        setLoading(true);
        GridService.findAllGrid()
            .then(resp => {
                setLoading(false);
                setData(resp.data.grids);
            })
            .catch(err => {
                setLoading(false);
                if (err.response && err.response.status) {
                    message.error('无查询权限');
                } else {
                    message.error('查询失败');
                }
            });
    }, [visible]);

    const getColumnSearchProps = dataIndex => ({
        filterDropdown: ({ setSelectedKeys, selectedKeys, confirm, clearFilters }) => (
            <div style={{ padding: 8 }}>
                <Input
                    ref={node => {
                        searchInput = node;
                    }}
                    placeholder={`Search ${dataIndex}`}
                    value={selectedKeys[0]}
                    onChange={e => setSelectedKeys(e.target.value ? [e.target.value] : [])}
                    onPressEnter={() => handleSearch(selectedKeys, confirm)}
                    style={{ width: 188, marginBottom: 8, display: 'block' }}
                />
                <Button
                    type="primary"
                    onClick={() => handleSearch(selectedKeys, confirm)}
                    icon="search"
                    size="small"
                    style={{ width: 90, marginRight: 8 }}
                >
                    Search
                </Button>
                <Button
                    onClick={() => handleReset(clearFilters)}
                    size="small"
                    style={{ width: 90 }}
                >
                    Reset
                </Button>
            </div>
        ),
        filterIcon: filtered => (
            <Icon type="search" style={{ color: filtered ? '#1890ff' : undefined }} />
        ),
        onFilter: (value, record) =>
            record[dataIndex]
                .toString()
                .toLowerCase()
                .includes(value.toLowerCase()),
        onFilterDropdownVisibleChange: visible => {
            if (visible) {
                setTimeout(() => searchInput.select());
            }
        },
    });

    const handleSearch = (selectedKeys, confirm) => {
        confirm();
        setSearchText(selectedKeys[0]);
    };

    const handleReset = clearFilters => {
        clearFilters();
        setSearchText('');
    };

    const handleChange = (pagination, filters, sorter) => {
        setFilteredInfo(filters);
        setSortedInfo(sorter);
    };

    const handleSelectGrid = gridId => {
        onLoadGrid(gridId);
    };

    const columns = [
        {
            title: '盘面',
            dataIndex: 'id',
        },
        {
            title: '难度',
            dataIndex: 'difficulty',
            filters: [
                { text: '-1', value: -1 },
                { text: '1', value: 1 },
                { text: '2', value: 2 },
                { text: '3', value: 3 },
                { text: '4', value: 4 },
                { text: '5', value: 5 },
            ],
            filteredValue: filteredInfo.difficulty || null,
            onFilter: (value, record) => record.difficulty === value,
        },
        {
            title: '创建人',
            dataIndex: 'createdBy',
            ...getColumnSearchProps('createdBy'),
        },
        {
            title: '创建时间',
            dataIndex: 'createdAt',
            ...getColumnSearchProps('createdAt'),
        },
        {
            title: '可AI解盘',
            dataIndex: 'resolvedByAi',
            render: (resolvedByAi, record) => (
                <Icon type={resolvedByAi ? 'check-circle' : 'close-circle'} theme="twoTone" />
            ),
        },
        {
            title: '注解',
            dataIndex: 'comment',
            ...getColumnSearchProps('comment'),
        },
        {
            title: 'Action',
            key: 'action',
            render: (text, record) => (
                <Button key={record.id} type="link" onClick={() => handleSelectGrid(record.id)}>
                    提取
                </Button>
            ),
        },
    ];

    return (
        <Modal
            visible={visible}
            title="提取盘面"
            onCancel={onCancel}
            footer={[]}
            width={1400}
            height={800}
        >
            <Table
                loading={loading}
                size="small"
                bordered={false}
                columns={columns}
                dataSource={data}
                rowKey="id"
                onChange={handleChange}
                rowClassName={(record, index) => {
                    let className = 'odd_row';
                    if (index % 2 === 1) className = 'even_row';
                    return className;
                }}
            />
        </Modal>
    );
};
export default GridTable;
