import React from "react";
import * as ReactDOM from "react-dom";
import * as d3 from "d3";

export default class Grid extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        this.renderAxis();
    }

    componentDidUpdate() {
        this.renderAxis();
    }

    renderAxis() {
        let yGrid = d3.axisLeft(this.props.y).ticks(10).tickSize(-this.props.width, 0, 0).tickFormat("");
        let node = ReactDOM.findDOMNode(this);
        d3.select(node).call(yGrid);
    }

    render() {
        return <g className="y-grid"/>
    }
}