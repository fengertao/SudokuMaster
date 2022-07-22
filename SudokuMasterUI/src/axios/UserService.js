/**
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

import axios from 'axios';
import { USER_URL } from './config';

const signup = jsonBody => {
    return axios.post(`${USER_URL}/signup`, jsonBody);
};

const getAll = () => {
    return axios.get(`${USER_URL}/all`);
};

//pageNumber start from page 1. however Spring Data pageNumber start from page 0
//So we have to minus 1 when call rest API.
const getPage = pageNumber => {
    let restPage = 0;
    if (pageNumber == null) {
        restPage = 0;
    } else if (pageNumber === 0) {
        restPage = 0;
    } else {
        restPage = pageNumber - 1;
    }
    return axios.get(`${USER_URL}/page?page=${restPage}`);
};

const enable = username => {
    return axios.post(`${USER_URL}/enable`, { username });
};

const disable = username => {
    return axios.post(`${USER_URL}/disable`, { username });
};

export { signup, getAll, getPage, enable, disable };
