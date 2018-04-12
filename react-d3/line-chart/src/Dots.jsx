import React from "react";

export default class Dots extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            width: 800,
            height: 300
        }
    }

    render() {
        return <g>{this.dataToDots()}</g>
    }

    dataToDots()  {
        return this.props.data.map((d,i) => {
            return <circle className="dot" r="5"
                           cx={this.props.x(d.date)}
                           cy={this.props.y(d.value)}
                           fill="#7dc7f4" stroke="#3f5175" strokeWidth="2px" key={i}
                           onMouseOver={this.props.showTooltip} onMouseOut={this.props.hidetoolTip}
            />
        });
    }
}