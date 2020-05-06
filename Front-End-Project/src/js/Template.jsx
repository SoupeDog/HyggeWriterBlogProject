import React from 'react';
import LogHelper from "./utils/LogHelper.jsx";

class Template extends React.Component {

    constructor(props) {
        super(props);
        this.state = {hasError: false};
        LogHelper.info({className: "Template", msg: "constructor----------"});
    }

    static getDerivedStateFromProps(nextProps, nextContext) {
        LogHelper.info({className: "Template", msg: "getDerivedStateFromProps----------"});
        LogHelper.debug({className: "Template", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "Template", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return nextProps;
    }

    render() {
        if (this.state.hasError) {
            return <h1>Something went wrong.</h1>;
        } else {
            return (
                null
            );
        }
    }

    shouldComponentUpdate(nextProps, nextState, nextContext) {
        LogHelper.info({className: "Template", msg: "shouldComponentUpdate----------"});
        LogHelper.debug({className: "Template", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "Template", tag: "nextState", msg: nextState, isJson: true});
        LogHelper.debug({className: "Template", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return true;
    }

    componentDidMount() {
        LogHelper.info({className: "Template", msg: "componentDidMount----------"});
        LogHelper.debug({msg: ""});
    }

    getSnapshotBeforeUpdate(prevProps, prevState) {
        LogHelper.info({className: "Template", msg: "getSnapshotBeforeUpdate----------"});
        LogHelper.debug({className: "Template", tag: "prevProps", msg: prevProps, isJson: true});
        LogHelper.debug({className: "Template", tag: "prevState", msg: prevState, isJson: true});
        LogHelper.debug({msg: ""});
        return null;
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        LogHelper.info({className: "Template", msg: "componentDidUpdate----------"});
        LogHelper.debug({className: "Template", tag: "prevProps", msg: prevProps, isJson: true});
        LogHelper.debug({className: "Template", tag: "prevState", msg: prevState, isJson: true});
        LogHelper.debug({className: "Template", tag: "snapshot", msg: snapshot, isJson: true});
        LogHelper.debug({msg: ""});
    }

    static getDerivedStateFromError(error) {
        LogHelper.info({className: "Template", msg: "getDerivedStateFromError----------"});
        LogHelper.debug({className: "Template", tag: "error", msg: error, isJson: true});
        LogHelper.debug({msg: ""});
        return {hasError: true};
    }

    componentDidCatch(error, info) {
        LogHelper.info({className: "Template", msg: "componentDidCatch----------"});
        LogHelper.debug({className: "Template", tag: "error", msg: error, isJson: true});
        LogHelper.debug({className: "Template", tag: "info", msg: info, isJson: true});
        LogHelper.debug({msg: ""});
    }
}

export default Template;