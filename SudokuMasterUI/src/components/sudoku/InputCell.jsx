/**
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

import React, { useState, useEffect } from 'react';

import { message } from 'antd';


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
        //46：delete. 8: backspace. 32: space
        //48~57: from 0 to 9
        console.log(event.keyCode)
        if ((event.keyCode === 46) || (event.keyCode === 8) || (event.keyCode === 32) || ((event.keyCode >= 48 ) && (event.keyCode <= 57)) ){
            console.log("good");
            props.onCellKeyDown(props.index, event.keyCode);
        } else {
            console.log("bad")
            message.warn('请按1-9键切换单元格内的候选数值，按0或空格键或退格键清空单元格');
            return false;
        }
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
