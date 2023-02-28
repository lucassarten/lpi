const React = require('react');
const ReactDOM = require('react-dom');
import WidgetLayout from "./WidgetLayout.jsx";

class Dashboard extends React.Component {
    render() {
        return (
            <div className="dashboard">
                <WidgetLayout />
            </div>
        );
    }
}

ReactDOM.render(
    <Dashboard />,
    document.getElementById('react')
)
