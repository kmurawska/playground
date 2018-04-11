import React from "react";
import Axis from "../axis/Axis.jsx";

export default class Axes extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        const {height, width} = this.props.svgDimensions;

        const xProps = {
            orient: 'Bottom',
            scale: this.props.scales.xScale,
            translate: `translate(0, ${height - this.props.margins.bottom})`,
            tickSize: height - this.props.margins.top - this.props.margins.bottom,
        };

        const yProps = {
            orient: 'Left',
            scale: this.props.scales.yScale,
            translate: `translate(${this.props.margins.left}, 0)`,
            tickSize: width - this.props.margins.left - this.props.margins.right,
        };

        return (
            <g>
                <Axis {...xProps} />
                <Axis {...yProps} />
            </g>
        )
    }
}