/**
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

const BACKEND_API_URL = process.env.NODE_ENV === 'production' ? '' : 'http://127.0.0.1:8080';
export const LOGIN_URL = BACKEND_API_URL + '/authenticate';
export const GRID_URL = BACKEND_API_URL + '/grid';
export const POSITION_URL = BACKEND_API_URL + '/position';
export const USER_URL = BACKEND_API_URL + '/user';

// github授权
export const GIT_OAUTH = 'https://github.com/login/oauth';
// github用户
export const GIT_USER = 'https://api.github.com/user';

// bbc top news
export const NEWS_BBC =
    'https://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=429904aa01f54a39a278a406acf50070';
