/**
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

import React, { Component } from 'react';

export default class MarkedCell extends Component {
    constructor(props) {
        super(props);
        this.state = {};
    }

    render() {
        let tdClassName, showValue, preChangeShowValue;
        const value = this.props.value;
        const isKeyCell = this.props.isKeyCell;
        const isRefCell = this.props.isRefCell;
        const preChangeCandidates = this.props.preChangeCandidates;

        if (value.length === 1) {
            tdClassName = `cell word${value}`;
            showValue = value;
        } else if (value === '123456789') {
            tdClassName = 'cell word0';
            showValue = '\xA0';
        } else {
            tdClassName = 'cell candidates';
            showValue = '';
            showValue += value.includes('1') ? '1' : '\xA0';
            showValue += value.includes('2') ? '2' : '\xA0';
            showValue += value.includes('3') ? '3' : '\xA0';
            showValue += '\n';
            showValue += value.includes('4') ? '4' : '\xA0';
            showValue += value.includes('5') ? '5' : '\xA0';
            showValue += value.includes('6') ? '6' : '\xA0';
            showValue += '\n';
            showValue += value.includes('7') ? '7' : '\xA0';
            showValue += value.includes('8') ? '8' : '\xA0';
            showValue += value.includes('9') ? '9' : '\xA0';
        }

        if (isRefCell) {
            tdClassName += ' slowlyBlink';
        }

        if (isKeyCell) {
            preChangeShowValue = '';
            if (preChangeCandidates) {
                preChangeShowValue += preChangeCandidates.includes('1') ? '1' : '\xA0';
                preChangeShowValue += preChangeCandidates.includes('2') ? '2' : '\xA0';
                preChangeShowValue += preChangeCandidates.includes('3') ? '3' : '\xA0';
                preChangeShowValue += '\n';
                preChangeShowValue += preChangeCandidates.includes('4') ? '4' : '\xA0';
                preChangeShowValue += preChangeCandidates.includes('5') ? '5' : '\xA0';
                preChangeShowValue += preChangeCandidates.includes('6') ? '6' : '\xA0';
                preChangeShowValue += '\n';
                preChangeShowValue += preChangeCandidates.includes('7') ? '7' : '\xA0';
                preChangeShowValue += preChangeCandidates.includes('8') ? '8' : '\xA0';
                preChangeShowValue += preChangeCandidates.includes('9') ? '9' : '\xA0';
            }
            return (
                <td className={'switch'}>
                    <span />
                    <span className={'cell candidates'}>{preChangeShowValue}</span>
                    <span className={tdClassName}>{showValue}</span>
                </td>
            );
        } else {
            return <td className={tdClassName}>{showValue}</td>;
        }
    }
}
