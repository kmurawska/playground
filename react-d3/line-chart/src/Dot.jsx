import React from "react";

export default class Dot extends React.Component {
    constructor(props) {
        super(props);
        this.onMouseOver = this.onMouseOver.bind(this);
        this.onMouseOut = this.onMouseOut.bind(this);
        this.state = {
            fillColor: "#3db79f",
            strokeColor: "#2f8e7b"
        }
    }

    render() {
        return <circle className="dot" r="6"
                       cx={this.props.cx}
                       cy={this.props.cy}
                       data-key={this.props.dataKey}
                       data-value={this.props.dataValue}
                       fill={this.state.fillColor} stroke={this.state.strokeColor} strokeWidth="2px"
                       onMouseOver={this.onMouseOver}
                       onMouseOut={this.onMouseOut}/>
    }

    onMouseOver(e) {
        this.setState({fillColor: "#CC445F", strokeColor: "#7a2839"});
        console.log(e.target.getAttribute('data-value') + ' ' + e.target.getAttribute('data-key'))
    }

    onMouseOut() {
        this.setState({fillColor: "#3db79f", strokeColor: "#2f8e7b"});
    }
}