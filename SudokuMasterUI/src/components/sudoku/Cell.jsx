/**
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

import React, { useContext, useState, useEffect } from 'react';
import { SudokuContext } from '@/context/SudokuContext';
import { updateCellCandidate } from '@/context/SudokuAction';
import {} from '@/context/SudokuAction';

import { message } from 'antd';

const formatCandidates = value => {
    let formattedValue = '';
    formattedValue += value.includes('1') ? '1' : '\xA0';
    formattedValue += value.includes('2') ? '2' : '\xA0';
    formattedValue += value.includes('3') ? '3' : '\xA0';
    formattedValue += '\n';
    formattedValue += value.includes('4') ? '4' : '\xA0';
    formattedValue += value.includes('5') ? '5' : '\xA0';
    formattedValue += value.includes('6') ? '6' : '\xA0';
    formattedValue += '\n';
    formattedValue += value.includes('7') ? '7' : '\xA0';
    formattedValue += value.includes('8') ? '8' : '\xA0';
    formattedValue += value.includes('9') ? '9' : '\xA0';
    return formattedValue;
};

const Cell = React.memo(props => {
    let { state: sudokuState, dispatch: sudokuDispatch } = useContext(SudokuContext);
    let [inputValue, setInputValue] = useState('');
    let [inputClassName, setInputClassName] = useState();
    let [showValue, setShowValue] = useState('');
    let [showClassName, setShowClassName] = useState();
    const originalValue = sudokuState.originalCells[props.index];
    const isReadOnly = originalValue !== '0';
    let value = sudokuState.cells[props.index];

    useEffect(() => {
        setInputClassName(`word${value}`);
        setShowValue(value.length > 1 ? formatCandidates(value) : value);
        setShowClassName(value.length > 1 ? 'candidates' : `word${value}`);
    }, [value]);

    const handleKeyDown = event => {
        if (event.keyCode === 8) {
            setShowValue('');
            setShowClassName(`word0`);
            sudokuDispatch(updateCellCandidate(props.index, ''));
        }
    };
    const handleValueChange = event => {
        let switchValue = event.target.value;
        let candidates = sudokuState.cells[props.index];
        let newValue = '';
        let formatted = '';

        setInputValue('');
        if (!'1234567890'.includes(switchValue)) {
            message.warn('请按1-9键切换单元格内的候选数值，按0键或退格键清空单元格');
            return;
        }

        if (switchValue.includes('0')) {
        } else if (candidates === '') {
            newValue = switchValue;
            formatted = switchValue;
        } else {
            for (let i = 1; i <= 9; i++) {
                let iString = i.toString();
                if (candidates.includes(iString) !== (switchValue === iString)) {
                    newValue += iString;
                    formatted += iString;
                } else {
                    formatted += '\xA0';
                }
                if (i === 3 || i === 6) {
                    formatted += '\n';
                }
            }
            if (newValue.length === 9) {
                newValue = '';
                formatted = '';
            }
        }

        if (newValue.length > 1) {
            setShowValue(formatted);
            setShowClassName(`candidates`);
        } else {
            setShowValue(newValue);
            setShowClassName(`word${newValue}`);
        }
        sudokuDispatch(updateCellCandidate(props.index, newValue));
    };

    if (isReadOnly) {
        return (
            <td className="cell">
                <input className={inputClassName} readOnly type={'text'} value={originalValue} />
            </td>
        );
    } else {
        return (
            <td className="cell">
                <span key={`inputspan${props.index}`} className="inputspan">
                    <input
                        id={`input${props.index}`}
                        name={`input${props.index}`}
                        className={inputClassName}
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
    }
});
export default Cell;
