import React from 'react'
import {scaleBand, scaleLinear} from 'd3-scale'
import data from '../data.js'
import Axes from "../axes/Axes.jsx";
import Bars from "../bars/Bars.jsx"

export default class Chart extends React.Component {
    constructor() {
        super();
        this.xScale = scaleBand();
        this.yScale = scaleLinear();
    }

    render() {
        const margins = {top: 50, right: 20, bottom: 100, left: 60};
        const svgDimensions = {width: 800, height: 500};

        const maxValue = Math.max(...data.map(d => d.value));

        const xScale = this.xScale
            .padding(0.5)
            .domain(data.map(d => d.title))
            .range([margins.left, svgDimensions.width - margins.right]);

        const yScale = this.yScale
            .domain([0, maxValue + 1])
            .range([svgDimensions.height - margins.bottom, margins.top]);

        return (
            <div>
                <svg width={svgDimensions.width} height={svgDimensions.height}>
                    <Axes
                        scales={{xScale, yScale}}
                        margins={margins}
                        svgDimensions={svgDimensions}
                    />
                    <Bars
                        scales={{xScale, yScale}}
                        margins={margins}
                        data={data}
                        maxValue={maxValue}
                        svgDimensions={svgDimensions}
                    />
                </svg>
            </div>
        )
    }
}