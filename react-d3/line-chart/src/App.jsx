import React from "react";
import LineChart from './LineChart.jsx'
import css from './style.css';

export default class App extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <h4>Europe Weather Conditions: 11.04.2018</h4>
                <LineChart />
            </div>
        )
    }
}