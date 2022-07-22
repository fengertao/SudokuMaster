/**
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

import React, { useState, useEffect } from 'react';
import { Table, Input, Button, Icon, message, Modal, Popover } from 'antd';
import Position from './Position';
import GridService from '@/axios/GridService';

const PositionTable = props => {
    const { visible, onCancel, onLoadPosition } = props;
    let [loading, setLoading] = useState(false);
    let [data, setData] = useState([]);
    let [, setFilteredInfo] = useState({});
    let [, setSearchText] = useState('');
    let [, setSortedInfo] = useState({});
    let searchInput;

    useEffect(() => {
        if (!visible) return;
        setLoading(true);
        GridService.findAllPosition()
            .then(resp => {
                setLoading(false);
                setData(resp.data.positions);
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

    const positionContent = code => {
        return <Position position={code} cell={null} refCells={[]} preChangeCandidates={null} />;
    };

    const columns = [
        {
            title: '盘面',
            dataIndex: 'grid',
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
            title: '注解',
            dataIndex: 'comment',
            ...getColumnSearchProps('comment'),
        },
        {
            title: '复盘',
            dataIndex: 'code',
            width: 50,
            render: (code, record) => (
                <span>
                    <Popover
                        placement="left"
                        content={positionContent(code)}
                        // title={``}
                        trigger="hover"
                    >
                        查看
                    </Popover>
                </span>
            ),
        },
        {
            title: '动作',
            key: 'action',
            render: (text, record) => (
                <Button
                    key={record.id}
                    type="link"
                    onClick={() => onLoadPosition(record.grid, record.code)}
                >
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
            width={1200}
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
export default PositionTable;
