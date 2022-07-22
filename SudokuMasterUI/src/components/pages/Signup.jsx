/**
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

import React, { useState } from 'react';

import { Form, Icon, Input, Button, Checkbox, Spin, message, Tooltip } from 'antd';
import { signup } from '@/axios/UserService';
const FormItem = Form.Item;

const Signup = props => {
    const { getFieldDecorator } = props.form;
    const [loading, setLoading] = useState(false);
    const [confirmDirty, setConfirmDirty] = useState(false);

    const formItemLayout = {
        labelCol: {
            xs: { span: 24 },
            sm: { span: 8 },
        },
        wrapperCol: {
            xs: { span: 24 },
            sm: { span: 14 },
        },
    };
    const tailFormItemLayout = {
        wrapperCol: {
            xs: {
                span: 24,
                offset: 0,
            },
            sm: {
                span: 14,
                offset: 8,
            },
        },
    };

    const handleConfirmBlur = e => {
        const value = e.target.value;
        setConfirmDirty(confirmDirty || !!value);
    };

    const checkPassword = (rule, value, callback) => {
        const form = props.form;
        if (value && value !== form.getFieldValue('password')) {
            callback('两次输入密码不一致!');
        } else {
            callback();
        }
    };
    const checkConfirm = (rule, value, callback) => {
        const form = props.form;
        if (value && confirmDirty) {
            form.validateFields(['confirm'], { force: true });
        }
        callback();
    };

    const handleSubmit = e => {
        e.preventDefault();
        props.form.validateFields((err, values) => {
            if (!err) {
                async function trySignup() {
                    setLoading(true);
                    await signup({
                        username: values.username,
                        password: values.password,
                        fullname: values.nickname,
                        email: values.email,
                    })
                        .then(response => {
                            message.success(response.data);
                            props.history.push('/login');
                        })
                        .catch(error => {
                            message.error(
                                '注册失败: ' +
                                    (error.response && error.response.data
                                        ? error.response.data.message
                                        : error.message)
                            );
                            setLoading(false);
                        });
                }

                trySignup();
            }
        });
    };

    const handleLogin = e => {
        e.preventDefault();
        props.history.push('/login');
    };

    return (
        <div className="signup">
            <Spin spinning={loading} size="large" tip="注册中...">
                <div className="signup-form">
                    <div className="signup-logo">
                        <span>注册用户</span>
                    </div>
                    <FormItem {...formItemLayout} label="用户名" hasFeedback>
                        {getFieldDecorator('username', {
                            rules: [
                                {
                                    required: true,
                                    message: '用户名长度6至15位，以字母开头',
                                    whitespace: false,
                                    max: 15,
                                    min: 6,
                                    pattern: /^[a-zA-Z][a-zA-Z0-9_]{5,15}$/,
                                },
                            ],
                        })(<Input />)}
                    </FormItem>
                    <Form onSubmit={handleSubmit}>
                        <FormItem {...formItemLayout} label="密码" hasFeedback>
                            {getFieldDecorator('password', {
                                rules: [
                                    {
                                        required: true,
                                        message: '请输入密码',
                                    },
                                    {
                                        validator: checkConfirm,
                                    },
                                ],
                            })(<Input type="password" />)}
                        </FormItem>
                        <FormItem {...formItemLayout} label="确认密码" hasFeedback>
                            {getFieldDecorator('confirm', {
                                rules: [
                                    {
                                        required: true,
                                        message: '请确认你的密码',
                                    },
                                    {
                                        validator: checkPassword,
                                    },
                                ],
                            })(<Input type="password" onBlur={handleConfirmBlur} />)}
                        </FormItem>
                        <FormItem
                            {...formItemLayout}
                            label={
                                <span>
                                    昵称&nbsp;
                                    <Tooltip title="别人怎么称呼你?">
                                        <Icon type="question-circle-o" />
                                    </Tooltip>
                                </span>
                            }
                            hasFeedback
                        >
                            {getFieldDecorator('nickname', {
                                rules: [
                                    {
                                        required: true,
                                        message: '请输入昵称',
                                        whitespace: true,
                                        max: 20,
                                        min: 2,
                                    },
                                ],
                            })(<Input />)}
                        </FormItem>
                        <FormItem {...formItemLayout} label="邮箱" hasFeedback>
                            {getFieldDecorator('email', {
                                rules: [
                                    {
                                        type: 'email',
                                        message: '请输入合理的邮箱地址',
                                    },
                                    {
                                        required: true,
                                        message: '请输入邮箱地址',
                                    },
                                ],
                            })(<Input />)}
                        </FormItem>
                        <FormItem {...tailFormItemLayout} style={{ marginBottom: 8 }}>
                            {getFieldDecorator('agreement', {
                                valuePropName: 'checked',
                                rules: [
                                    {
                                        required: true,
                                        message: '需要接受协议方可注册用户',
                                    },
                                ],
                            })(
                                <Checkbox>
                                    <Tooltip title="数独大师为技术验证和演示网站，非商业运营网站。站内包括用户数据在内所有数据等均为公开测试数据。请勿录入私人信息。">
                                        我已经阅读过协议
                                    </Tooltip>
                                </Checkbox>
                            )}
                        </FormItem>
                        <FormItem {...tailFormItemLayout}>
                            <span>
                                <Button type="primary" htmlType="submit">
                                    注册
                                </Button>
                            </span>
                            <span>&nbsp;&nbsp;&nbsp;</span>
                            <span onClick={handleLogin}>
                                <Button type="link">返回登录页面</Button>
                            </span>
                        </FormItem>
                    </Form>
                </div>
            </Spin>
        </div>
    );
};

export default Form.create()(Signup);
