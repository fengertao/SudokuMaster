import React from 'react';
import ReactEcharts from 'echarts-for-react';
import echarts from 'echarts';

const today = new Date();
var todayMinus1 = new Date(today);
todayMinus1.setDate(today.getDate() - 1);
var todayMinus2 = new Date(today);
todayMinus2.setDate(today.getDate() - 2);
var todayMinus3 = new Date(today);
todayMinus3.setDate(today.getDate() - 3);
var todayMinus4 = new Date(today);
todayMinus4.setDate(today.getDate() - 4);
var todayMinus5 = new Date(today);
todayMinus5.setDate(today.getDate() - 5);
var todayMinus6 = new Date(today);
todayMinus6.setDate(today.getDate() - 6);
var todayMinus7 = new Date(today);
todayMinus7.setDate(today.getDate() - 7);

const option = {
    title: {
        text: '最近7天用户访问量',
        left: '50%',
        show: false,
        textAlign: 'center',
    },
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            lineStyle: {
                color: '#ddd',
            },
        },
        backgroundColor: 'rgba(255,255,255,1)',
        padding: [5, 10],
        textStyle: {
            color: '#7588E4',
        },
        extraCssText: 'box-shadow: 0 0 5px rgba(0,0,0,0.3)',
    },
    legend: {
        right: 20,
        orient: 'vertical',
    },
    xAxis: {
        type: 'category',
        data: [
            todayMinus7.toLocaleDateString(),
            todayMinus6.toLocaleDateString(),
            todayMinus5.toLocaleDateString(),
            todayMinus4.toLocaleDateString(),
            todayMinus3.toLocaleDateString(),
            todayMinus2.toLocaleDateString(),
            todayMinus1.toLocaleDateString(),
        ],
        boundaryGap: false,
        splitLine: {
            show: true,
            interval: 'auto',
            lineStyle: {
                color: ['#D4DFF5'],
            },
        },
        axisTick: {
            show: false,
        },
        axisLine: {
            lineStyle: {
                color: '#609ee9',
            },
        },
        axisLabel: {
            margin: 10,
            textStyle: {
                fontSize: 10,
            },
        },
    },
    yAxis: {
        type: 'value',
        splitLine: {
            lineStyle: {
                color: ['#D4DFF5'],
            },
        },
        axisTick: {
            show: false,
        },
        axisLine: {
            lineStyle: {
                color: '#609ee9',
            },
        },
        axisLabel: {
            margin: 0,
            textStyle: {
                fontSize: 8,
            },
        },
    },
    series: [
        {
            name: '访问量',
            type: 'line',
            smooth: true,
            showSymbol: false,
            symbol: 'circle',
            symbolSize: 6,
            data: ['1200', '1400', '808', '811', '626', '488', '1600'],
            areaStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(
                        0,
                        0,
                        0,
                        1,
                        [
                            {
                                offset: 0,
                                color: 'rgba(216, 244, 247,1)',
                            },
                            {
                                offset: 1,
                                color: 'rgba(216, 244, 247,1)',
                            },
                        ],
                        false
                    ),
                },
            },
            itemStyle: {
                normal: {
                    color: '#58c8da',
                },
            },
            lineStyle: {
                normal: {
                    width: 3,
                },
            },
        },
    ],
};

const EchartsViews = () => (
    <ReactEcharts
        option={option}
        style={{ height: '350px', width: '100%' }}
        className={'react_for_echarts'}
    />
);

export default EchartsViews;
