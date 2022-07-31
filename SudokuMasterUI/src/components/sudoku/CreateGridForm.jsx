import React, { useState } from 'react';

import { Form, Modal, Input } from 'antd';
import bg from './bg.png';
import InputCell from './InputCell';

const CreateGridForm = Form.create()(props => {
    const { visible, onCancel, onCreate, form } = props;
    const { getFieldDecorator } = form;
    const [ inputString, setInputString] = useState("");
    const cellRef = [];
    for (let i = 0; i < 81; i++) {
        cellRef[i] = React.createRef();
    }
    var inputBoxRef = React.createRef()

    const onGridKeyPress = e => {
        const keychar = String.fromCharCode(e.which);
        const numcheck = /[\d ]/;
        if (!numcheck.test(keychar)) {
            e.preventDefault();
        }
    };

    const onGridInput = e => {
        const value = e.target.value;
        const numcheck = /[^\d]/;

        if (numcheck.test(value)) {
            var newValue = value.replace(/ /gi, '0');
            newValue = newValue.replace(/[^\d]/gi, '');
            e.target.value = newValue;
        }
        setInputString(e.target.value)
    };

    const onCellValueChange = (index, newValue) => {
        console.log(inputBoxRef)
        console.log(inputBoxRef.current)
        var tempString = inputString;

        while (tempString.length <= index) {
            tempString += "0"
        }
        tempString = tempString.substring(0, index) + newValue + tempString.substring(index + 1);
        setInputString(tempString)
        props.form.setFieldsValue({'newGridId':tempString});

        if (index < 80) {
            console.log("changed focus when index " + index)
            cellRef[index + 1].current.focus()
        } else {
            /*
            * Todo On user input last cell, should focus on the inputbox or the ok button.
            * However, there are issue:
            * 1. don't know how to focus on antd Modal Ok button
            * 2. After setInputString(tempString) or props.form.setFieldValue() line, the "inputBoxRef" will lost, we cannot focus anymore.
            * Seems like after reactJS component changed state, it may change to another Dom so original ref lot.
            *
            *
            * inputBoxRef.current.focus()
            */
        }
    }

    const onClickOkButton = e => {
        var tempString = inputString;

        while (tempString.length < 81) {
            tempString += "0"
        }
        props.form.setFieldsValue({'newGridId':tempString});
        setInputString(tempString)
        onCreate(e)
    }

    return (
        <Modal
            visible={visible}
            title="自定义盘"
            okText="创建"
            cancelText="取消"
            onCancel={onCancel}
            onOk={onClickOkButton}
            width={710}
        >
            <div style={{ textAlign: 'center', margin: '0px auto', height: 444 }}>
                <img src={bg} className="gridBg" alt="bg" />

                <div className="gridFg">
                    <div style={{ position: 'relative', left: 140 }}>
                        <table id="tab" className="gridTable">
                            <tbody>
                                {[0, 1, 2, 3, 4, 5, 6, 7, 8].map(iRow => {
                                    return (
                                        <tr key={'tr' + iRow} className="grid">
                                            {[0, 1, 2, 3, 4, 5, 6, 7, 8].map(iCol => {
                                                return (
                                                    <InputCell
                                                        //Todo remove duplication on index
                                                        key={'cell' + (iRow * 9 + iCol)}
                                                        index={iRow * 9 + iCol}
                                                        ref={cellRef[iRow * 9 + iCol]}
                                                        inputString={inputString}
                                                        onCellValueChange={onCellValueChange}
                                                    />
                                                );
                                            })}
                                        </tr>
                                    );
                                })}
                            </tbody>
                        </table>
                    </div>
                    <p />
                    <Form layout="vertical">
                        <Form.Item label="请在下面输入框内输入盘面，空单元格按0或者空格。或点击上图中的单元格开始输入。">
                            {getFieldDecorator('newGridId', {
                                initialValue: '',
                                autoFocus: true,
                                rules: [
                                    {
                                        type: 'string',
                                        message: '请输入81个数字，0表示空格。不足81个数字时系统自动在结尾补0',
                                    },
                                    {
                                        required: true,
                                        message: '请输入新盘',
                                    },
                                ],
                            })(
                                <Input
                                    ref={inputBoxRef}
                                    maxLength={81}
                                    placeholder="请从上到下从左到右输入盘面，空单元格输入0或者空格"
                                    autoFocus
                                    pattern="[0-9]{81}"
                                    width={660}
                                    onKeyPress={onGridKeyPress}
                                    onInput={onGridInput}
                                />
                            )}
                        </Form.Item>
                    </Form>
                </div>
            </div>

        </Modal>
    );
});

export default CreateGridForm;
