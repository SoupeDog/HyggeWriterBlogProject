import React from 'react';
import LogHelper from "../../utils/LogHelper.jsx";

class Template extends React.Component {

    constructor(props) {
        super(props);
        this.state = {};
        LogHelper.info({className: "Template", msg: "constructor----------"});
    }

    static getDerivedStateFromProps(nextProps, nextContext) {
        LogHelper.info({className: "Template", msg: "getDerivedStateFromProps----------"});
        LogHelper.debug({className: "Template", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "Template", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return nextProps;
    }

    shouldComponentUpdate(nextProps, nextState, nextContext) {
        LogHelper.info({className: "Template", msg: "shouldComponentUpdate----------"});
        LogHelper.debug({className: "Template", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "Template", tag: "nextState", msg: nextState, isJson: true});
        LogHelper.debug({className: "Template", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return true;
    }

    render() {
        return (
            null
        );
    }

    componentDidMount() {
        LogHelper.info({className: "Template", msg: "componentDidMount----------"});
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        LogHelper.info({className: "Template", msg: "componentDidUpdate----------"});
        LogHelper.debug({className: "Template", tag: "prevProps", msg: prevProps, isJson: true});
        LogHelper.debug({className: "Template", tag: "prevState", msg: prevState, isJson: true});
        LogHelper.debug({className: "Template", tag: "snapshot", msg: snapshot, isJson: true});
        LogHelper.debug({msg: ""});
    }

    componentWillUnmount() {
        LogHelper.info({className: "Template", msg: "componentWillUnmount----------"});
    }
}

export default Template;