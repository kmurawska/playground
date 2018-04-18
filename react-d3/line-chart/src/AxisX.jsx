import React from "react";
import * as ReactDOM from "react-dom";
import * as d3 from "d3";
import data from "app/data";

export default class AxisX extends React.Component {
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
        let xAxis = d3.axisBottom(this.props.x)
            .tickValues(this.props.data.map((d) => {return d.date}))
            .ticks(4)
            .tickFormat(d3.timeFormat("%d-%m-%Y"));

        let node = ReactDOM.findDOMNode(this);
        d3.select(node).call(xAxis).selectAll("text")
            .style("text-anchor", "end")
            .attr("dx", "-.8em")
            .attr("dy", ".15em")
            .attr("transform", () => { return "rotate(-65)"});
    }

    render() {
        return <g className="axis" transform={"translate(0," + (this.props.height) + ")"}/>
    }
}