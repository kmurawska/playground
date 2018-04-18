import React from "react";
import * as d3 from "d3";
import data from './data.js'
import Dots from "app/Dots.jsx";
import AxisY from "app/AxisY.jsx";
import AxisX from "app/AxisX.jsx";
import Grid from "app/Grid.jsx";

export default class App extends React.Component {
    constructor(props) {
        super(props);
        this.updateDimensions = this.updateDimensions.bind(this);
        let margin = {top: 5, right: 50, bottom: 20, left: 50};
        this.state = {
            margin: margin,
            width: window.innerWidth * 0.8 - (margin.left + margin.right),
            height: window.innerHeight * 0.6 - (margin.top + margin.bottom),
            svgWidth: window.innerWidth,
            svgHeight: window.innerHeight
        }
    }

    componentDidMount() {
        this.updateDimensions();
        window.addEventListener('resize', this.updateDimensions);
    }

    updateDimensions() {
        let margin = {top: 5, right: 50, bottom: 20, left: 50};

        this.setState({
            width: window.innerWidth * 0.8 - (margin.left + margin.right),
            height: window.innerHeight * 0.6 - (margin.top + margin.bottom)
        });
    }

    render() {
        this.enrichSourceDataWithParsedDate();

        let x = d3.scaleTime()
            .domain(d3.extent(data, d => {return d.date}))
            .rangeRound([0, this.state.width]);

        let y = d3.scaleLinear()
            .domain([0,  this.maxValueY() + 50])
            .range([this.state.height, 0]);

        let line = d3.line()
            .x(d => {
                return x(d.date)
            })
            .y(d => {
                return y(d.value)
            })
            .curve(d3.curveNatural);

        return (
            <div>
                <div>
                    <svg id="line_chart" width={this.state.svgWidth} height={this.state.svgHeight}>
                        <g transform={'translate(' + this.state.margin.left + ',' + this.state.margin.top + ')'}>
                            <AxisY y={y} height={this.state.height} width={this.state.width}/>
                            <AxisX x={x} data={data} height={this.state.height} width={this.state.width}/>
                            <Grid y={y} height={this.state.height} width={this.state.width}/>
                            <path className="line" d={line(data)} strokeLinecap="round"/>
                            <Dots data={data} x={x} y={y}/>
                        </g>
                    </svg>
                </div>
            </div>
        )
    }

    enrichSourceDataWithParsedDate() {
        let parseDate = d3.timeParse("%d-%m-%Y");

        data.forEach(d => {
            d.date = parseDate(d.day);
        });
    }

    maxValueY() {
        return d3.max(data, d => {
            return d.value
        })
    }
}