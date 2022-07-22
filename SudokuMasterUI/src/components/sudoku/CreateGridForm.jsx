import React from 'react';
import { Form, Modal, Input } from 'antd';

const DEMO_GRID2 =
    '002010000040500360706300500000100000500030007000004000004001203061003040000020800';

const CreateGridForm = Form.create()(props => {
    const { visible, onCancel, onCreate, form } = props;
    const { getFieldDecorator } = form;
    const onGridKeyPress = e => {
        const keychar = String.fromCharCode(e.which);
        const numcheck = /\d/;
        if (!numcheck.test(keychar)) {
            e.preventDefault();
        }
    };

    const onGridInput = e => {
        const value = e.target.value;
        const numcheck = /[^\d]/;
        if (numcheck.test(value)) {
            const newValue = value.replace(/[^\d]/gi, '');
            e.target.value = newValue;
        }
    };

    return (
        <Modal
            visible={visible}
            title="自定义盘"
            okText="创建"
            cancelText="取消"
            onCancel={onCancel}
            onOk={onCreate}
            width={710}
        >
            <Form layout="vertical">
                <Form.Item label="盘面">
                    {getFieldDecorator('newGridId', {
                        initialValue: DEMO_GRID2,
                        autoFocus: true,
                        rules: [
                            {
                                type: 'string',
                                len: 81,
                                message: '请输入81个数字，0表示空格',
                            },
                            {
                                required: true,
                                message: '请输入新盘',
                            },
                        ],
                    })(
                        <Input
                            maxLength={81}
                            placeholder="请按行依次输入81个单元格的数字（0表示空格）"
                            autoFocus
                            pattern="[0-9]{81}"
                            width={660}
                            onKeyPress={onGridKeyPress}
                            onInput={onGridInput}
                        />
                    )}
                </Form.Item>
            </Form>
        </Modal>
    );
});

export default CreateGridForm;
