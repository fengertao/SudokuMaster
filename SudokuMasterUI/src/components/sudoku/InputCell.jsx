/**
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

import React, { useState, useEffect } from 'react';

import { message } from 'antd';
import { KEYCODE } from "../../utils/dom"

const InputCell = React.memo(React.forwardRef((props,ref) => {


    let [inputValue, setInputValue] = useState('');
    let [, setInputClassName] = useState();
    let [showValue, setShowValue] = useState('');
    let [showClassName, setShowClassName] = useState();

    var gridString = props.inputString;
    let value = gridString.length <= props.index ? "0" : gridString[props.index];

    const calculateShowValue = n => {
        if ((n >= "1") && (n <="9")) {
            return n
        } else {
            return ""
        }
    }

    useEffect(() => {
        setInputClassName(`word${value}`);
        setShowValue(calculateShowValue(value));
        setShowClassName(`word${value}`);
    }, [value]);

    const handleKeyDown = event => {
        switch(event.keyCode) {
            case KEYCODE.DELETE:
            case KEYCODE.BACK_SPACE:
            case KEYCODE.SPACE:
            case KEYCODE.CHAR_0:
            case KEYCODE.CHAR_1:
            case KEYCODE.CHAR_2:
            case KEYCODE.CHAR_3:
            case KEYCODE.CHAR_4:
            case KEYCODE.CHAR_5:
            case KEYCODE.CHAR_6:
            case KEYCODE.CHAR_7:
            case KEYCODE.CHAR_8:
            case KEYCODE.CHAR_9:
            case KEYCODE.LEFT:
            case KEYCODE.RIGHT:
            case KEYCODE.UP:
            case KEYCODE.DOWN:
                props.onCellKeyDown(props.index, event.keyCode);
                break;
            default:
                message.warn('请按1-9键切换单元格内的候选数值，按0或空格键或退格键清空单元格');
                return false;
        }


        // if ((event.keyCode === KEYCODE.DELETE) || (event.keyCode === KEYCODE.BACK_SPACE) || (event.keyCode === KEYCODE.SPACE) || ((event.keyCode >= KEYCODE.CHAR_0 ) && (event.keyCode <= KEYCODE.CHAR_9)) ){
        //     props.onCellKeyDown(props.index, event.keyCode);
        // } else if (event.keyCode === KEYCODE.TAB) {
        //     //Just default action
        // } else {
        //     message.warn('请按1-9键切换单元格内的候选数值，按0或空格键或退格键清空单元格');
        //     return false;
        // }
    };

    const handleValueChange = event => {
        setInputValue("");
        setInputClassName(`word${value}`);
        setShowValue(calculateShowValue(value));
        setShowClassName(`word${value}`);
    };

    return (
        <td className="cell">
            <span key={`inputspan${props.index}`} className="inputspan">
                <input
                    id={`input${props.index}`}
                    name={`input${props.index}`}
                    ref={ref}
                    className={"inputClassName"}
                    type={'text'}
                    value={inputValue}
                    onChange={handleValueChange}
                    onKeyDown={handleKeyDown}
                />
            </span>
            <span key={`displayspan${props.index}`} className={`displayspan ${showClassName}`}>
                {showValue}
            </span>
        </td>
    );
}));
export default InputCell;
