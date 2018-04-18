import React from "react";
import Dot from "app/Dot.jsx";

export default class Dots extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            width: 800,
            height: 300
        }
    }

    render() {
        return <g>{
            this.props.data.map((d, i) => {
                return <Dot key={i} cx={this.props.x(d.date)} cy={this.props.y(d.value)} dataValue={d.value} dataKey={d.date}/>
            })
        }</g>
    }
}