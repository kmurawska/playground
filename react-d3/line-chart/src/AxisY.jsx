import React from "react";
import * as ReactDOM from "react-dom";
import * as d3 from "d3";

export default class AxisY extends React.Component {
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
        let yAxis = d3.axisLeft(this.props.y).ticks(10);

        let node = ReactDOM.findDOMNode(this);
        d3.select(node).call(yAxis);

    }

    render() {
        return <g className="axis"/>
    }
}