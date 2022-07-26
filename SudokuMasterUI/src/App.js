/**
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

import React, { useContext, useEffect, useState } from 'react';
import Routes from './routes';
import DocumentTitle from 'react-document-title';
import SiderCustom from './components/SiderCustom';
import HeaderCustom from './components/HeaderCustom';
import { Icon, Layout, notification } from 'antd';
import { ThemePicker } from './components/widget';
import { AuthContext } from '@/context/AuthContext';
import { ResponsiveContext } from '@/context/ResponsiveContext';
import { SET_MOBILE_FLAG } from '@/context/ResponsiveAction';

const { Content, Footer } = Layout;

const App = props => {
    let [collapsed, setCollapsed] = useState(false);
    let [title] = useState('Sudoku Master');
    const { state: authState } = useContext(AuthContext);
    const { state: responsiveState, dispatch: responsiveDispatch } = useContext(ResponsiveContext);

    useEffect(
        () => {
            function getClientWidth() {
                // 获取当前浏览器宽度并设置responsive管理响应式
                const clientWidth = window.innerWidth;
                responsiveDispatch({
                    type: SET_MOBILE_FLAG,
                    data: { isMobile: clientWidth <= 992 },
                });
            }
            getClientWidth();
            window.addEventListener('resize', getClientWidth);
            return () => {
                window.removeEventListener('resize', getClientWidth);
            };
        },
        [responsiveDispatch] //this parameter should be useless, just remove verbose startup msg.
    );

    useEffect(() => {
        const openNotification = () => {
            notification.open({
                message: '博主：Charlie Feng',
                description: (
                    <div>
                        <p>
                            GitHub地址：{' '}
                            <a
                                href="Https://github.com/fengertao"
                                target="_blank"
                                rel="noopener noreferrer"
                            >
                                Https://github.com/fengertao
                            </a>
                        </p>
                        <p>
                            博客地址：{' '}
                            <a
                                href="Http://blog.warrenfeng.com/"
                                target="_blank"
                                rel="noopener noreferrer"
                            >
                                Http://blog.warrenfeng.com/
                            </a>
                        </p>
                    </div>
                ),
                icon: <Icon type="smile-circle" style={{ color: 'red' }} />,
                duration: 0,
            });
            localStorage.setItem('isFirst', JSON.stringify(true));
        };
        const isFirst = JSON.parse(localStorage.getItem('isFirst'));
        !isFirst && openNotification();
    });

    const toggle = () => {
        setCollapsed(!collapsed);
    };
    return (
        <DocumentTitle title={title}>
            <Layout>
                {!responsiveState.isMobile && <SiderCustom collapsed={collapsed} />}
                <ThemePicker />
                <Layout style={{ flexDirection: 'column' }}>
                    <HeaderCustom toggle={toggle} collapsed={collapsed} user={authState || {}} />
                    <Content style={{ margin: '0 16px', overflow: 'initial', flex: '1 1 0' }}>
                        <Routes auth={authState} />
                    </Content>
                    <Footer style={{ textAlign: 'center' }}>
                        Sudoku Master ©{new Date().getFullYear()} Created by Charlie Feng
                    </Footer>
                </Layout>
            </Layout>
        </DocumentTitle>
    );
};

export default App;
