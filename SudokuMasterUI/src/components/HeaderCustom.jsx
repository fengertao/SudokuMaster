import React, { useContext, useState } from 'react';
import { AuthContext } from '@/context/AuthContext';
import * as AuthService from '@/axios/AuthService';
// import screenfull from 'screenfull';
import avater from '@/style/imgs/avatar_man.svg';
import SiderCustom from './SiderCustom';
import { Menu, Icon, Layout, Popover } from 'antd';
import { withRouter } from 'react-router-dom';
// import { PwaInstaller } from './widget';
import { ResponsiveContext } from '@/context/ResponsiveContext';

const { Header } = Layout;
const SubMenu = Menu.SubMenu;
const MenuItemGroup = Menu.ItemGroup;

const HeaderCustom = props => {
    const { state: authState, dispatch: authDispatch } = useContext(AuthContext);
    const [visible, setVisible] = useState(false);
    const { state: responsiveState } = useContext(ResponsiveContext);

    // const screenFull = () => {
    //     if (screenfull.enabled) {
    //         screenfull.request();
    //     }
    // };
    const menuClick = e => {
        e.key === 'logout' && logout();
    };
    const logout = () => {
        AuthService.logout(authState, authDispatch);
        props.history.push('/login');
    };
    const popoverHide = () => {
        setVisible(false);
    };
    const handleVisibleChange = newVisible => {
        setVisible(newVisible);
    };
    const { path } = props;
    return (
        <Header className="custom-theme header">
            {responsiveState.isMobile ? (
                <Popover
                    content={<SiderCustom path={path} popoverHide={popoverHide} />}
                    trigger="click"
                    placement="bottomLeft"
                    visible={visible}
                    onVisibleChange={handleVisibleChange}
                >
                    <Icon type="bars" className="header__trigger custom-trigger" />
                </Popover>
            ) : (
                <Icon
                    className="header__trigger custom-trigger"
                    type={props.collapsed ? 'menu-unfold' : 'menu-fold'}
                    onClick={props.toggle}
                />
            )}
            <Menu
                mode="horizontal"
                style={{ lineHeight: '64px', float: 'right' }}
                onClick={menuClick}
            >
                {/* <Menu.Item key="pwa">
                    <PwaInstaller />
                </Menu.Item>
                <Menu.Item key="full" onClick={screenFull}>
                    <Icon type="arrows-alt" onClick={screenFull} />
                </Menu.Item>
                <Menu.Item key="1">
                    <Badge count={25} overflowCount={10} style={{ marginLeft: 10 }}>
                        <Icon type="notification" />
                    </Badge>
                </Menu.Item> */}
                <SubMenu
                    title={
                        <span className="avatar">
                            <img src={avater} alt="头像" />
                            <i className="on bottom b-white" />
                        </span>
                    }
                >
                    <MenuItemGroup title="用户中心">
                        <Menu.Item key="setting:1">你好 - {authState.username}</Menu.Item>
                        <Menu.Item key="logout">
                            <span onClick={logout}>退出登录</span>
                        </Menu.Item>
                    </MenuItemGroup>
                </SubMenu>
            </Menu>
        </Header>
    );
};

export default withRouter(HeaderCustom);
