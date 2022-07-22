/**
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

import axios from 'axios';
import { GRID_URL, POSITION_URL } from './config';

class GridService {
    getPosition = cells => cells.reduce((a, b) => a.concat('|').concat(b));

    resolveGrid = gridId => {
        return axios.get(`${GRID_URL}/${gridId}/resolve`);
    };

    saveGrid = gridId => {
        return axios.put(`${GRID_URL}/${gridId}`);
    };

    findAllGrid = () => {
        return axios.get(`${GRID_URL + 's'}`);
    };

    resolvePosition = (gridId, cells) => {
        return axios.get(`${POSITION_URL}/${gridId}/${this.getPosition(cells)}/resolve`);
    };

    ValidatePosition = (gridId, cells) => {
        return axios.get(`${POSITION_URL}/${gridId}/${this.getPosition(cells)}/validate`);
    };

    savePosition = (gridId, cells) => {
        return axios.put(`${POSITION_URL}/${gridId}/${this.getPosition(cells)}`);
    };

    findAllPosition = () => {
        return axios.get(`${POSITION_URL + 's'}`);
    };
}

export default new GridService();
