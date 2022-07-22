/**
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

import React, { useContext, useState } from 'react';
import { Form, Icon, Input, Button, Checkbox, Spin, message } from 'antd';
import { PwaInstaller } from '../widget';
import { AuthContext } from '@/context/AuthContext';
import { LOGIN_SUCCESSFUL } from '@/context/AuthAction';
import { login } from '@/axios/AuthService';

const FormItem = Form.Item;

const Login = props => {
    const { dispatch } = useContext(AuthContext);
    const [loading, setLoading] = useState(false);
    const { getFieldDecorator } = props.form;

    const handleSubmit = e => {
        e.preventDefault();
        props.form.validateFields((err, values) => {
            if (!err) {
                async function tryLogin() {
                    setLoading(true);
                    await login({
                        username: values.userName,
                        password: values.password,
                    })
                        .then(response => {
                            dispatch({ type: LOGIN_SUCCESSFUL, data: response.data });
                            props.history.push('/');
                        })
                        .catch(error => {
                            let msg = error.response ? error.response.data : error.message;
                            switch (msg) {
                                case 'USER_DISABLED':
                                    msg = '需要等待管理员激活账户';
                                    break;
                                case 'INVALID_CREDENTIALS':
                                    msg = '账号密码错误';
                                    break;
                                default:
                                    break;
                            }
                            message.error('登录失败: ' + msg);
                            setLoading(false);
                        });
                }

                tryLogin();
            }
        });
    };

    const handleGuestLogin = e => {
        e.preventDefault();

        async function tryLogin() {
            setLoading(true);
            await login({
                username: 'guest',
                password: 'guest',
            })
                .then(response => {
                    dispatch({ type: LOGIN_SUCCESSFUL, data: response.data });
                    props.history.push('/');
                })
                .catch(error => {
                    let msg = error.response ? error.response.data : error.message;
                    switch (msg) {
                        case 'USER_DISABLED':
                            msg = '需要等待管理员激活账户';
                            break;
                        case 'INVALID_CREDENTIALS':
                            msg = '账号密码错误';
                            break;
                        default:
                            break;
                    }
                    message.error('登录失败: ' + msg);
                    setLoading(false);
                });
        }

        tryLogin();
    };

    const handleSignup = e => {
        e.preventDefault();
        props.history.push('/signup');
    };

    // const gitHub = () => {
    //     window.location.href =
    //         'https://github.com/login/oauth/authorize?client_id=792cdcd244e98dcd2dee&redirect_uri=http://localhost:3006/&scope=user&state=reactAdmin';
    // };

    return (
        <div className="login">
            <Spin spinning={loading} size="large" tip="登录中...">
                <div className="login-form">
                    <div className="login-logo">
                        <span>数独大师</span>
                        <PwaInstaller />
                    </div>
                    <Form onSubmit={handleSubmit} style={{ maxWidth: '300px' }}>
                        <FormItem>
                            {getFieldDecorator('userName', {
                                rules: [{ required: true, message: '请输入用户名!' }],
                            })(
                                <Input
                                    prefix={<Icon type="user" style={{ fontSize: 13 }} />}
                                    placeholder="guest"
                                />
                            )}
                        </FormItem>
                        <FormItem>
                            {getFieldDecorator('password', {
                                rules: [{ required: true, message: '请输入密码!' }],
                            })(
                                <Input
                                    prefix={<Icon type="lock" style={{ fontSize: 13 }} />}
                                    type="password"
                                    placeholder="guest"
                                />
                            )}
                        </FormItem>
                        <FormItem>
                            {getFieldDecorator('remember', {
                                valuePropName: 'checked',
                                initialValue: true,
                            })(<Checkbox>记住我</Checkbox>)}
                            <span className="login-form-forgot" href="" style={{ float: 'right' }}>
                                忘记密码
                            </span>
                            <Button
                                type="primary"
                                htmlType="submit"
                                className="login-form-button"
                                style={{ width: '100%' }}
                            >
                                登录
                            </Button>
                            <p style={{ display: 'flex', justifyContent: 'space-between' }}>
                                <span onClick={handleGuestLogin}>
                                    <Button type="dashed">游客模式</Button>
                                </span>
                                <span onClick={handleSignup}>
                                    <Button type="dashed">现在就去注册!</Button>
                                </span>
                                {/* <span>
                                    <Icon type="github" onClick={gitHub} />
                                    (第三方登录)
                                </span> */}
                            </p>
                        </FormItem>
                    </Form>
                </div>
            </Spin>
        </div>
    );
};

export default Form.create()(Login);
