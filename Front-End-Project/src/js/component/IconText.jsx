import React from 'react';
import LogHelper from "../utils/LogHelper.jsx";
import {Tooltip} from "antd";
import TimeHelper from "../utils/TimeHelper.jsx";



class IconText extends React.Component {

    constructor(props) {
        super(props);
        this.state = {hasError: false};
        LogHelper.info({className: "IconText", msg: "constructor----------"});
    }

    static getDerivedStateFromProps(nextProps, nextContext) {
        LogHelper.info({className: "IconText", msg: "getDerivedStateFromProps----------"});
        LogHelper.debug({className: "IconText", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "IconText", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return nextProps;
    }

    render() {
        if (this.state.hasError) {
            return (
                <h1>Something went wrong.</h1>
            );
        } else {
            return (
                <div>
                    <Tooltip title={this.props.tip}>
                    <span>
                        <i className="iconfont" style={{marginRight: 8}}>{this.props.type}</i>
                        {this.props.isTimeStamp ? TimeHelper.formatTimeStampToString({target: this.props.text}) : this.props.text}
                    </span>
                    </Tooltip>
                </div>
            );
        }
    }

    shouldComponentUpdate(nextProps, nextState, nextContext) {
        LogHelper.info({className: "IconText", msg: "shouldComponentUpdate----------"});
        LogHelper.debug({className: "IconText", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "IconText", tag: "nextState", msg: nextState, isJson: true});
        LogHelper.debug({className: "IconText", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return true;
    }

    componentDidMount() {
        LogHelper.info({className: "IconText", msg: "componentDidMount----------"});
        LogHelper.debug({msg: ""});
    }

    getSnapshotBeforeUpdate(prevProps, prevState) {
        LogHelper.info({className: "IconText", msg: "getSnapshotBeforeUpdate----------"});
        LogHelper.debug({className: "IconText", tag: "prevProps", msg: prevProps, isJson: true});
        LogHelper.debug({className: "IconText", tag: "prevState", msg: prevState, isJson: true});
        LogHelper.debug({msg: ""});
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        LogHelper.info({className: "IconText", msg: "componentDidUpdate----------"});
        LogHelper.debug({className: "IconText", tag: "prevProps", msg: prevProps, isJson: true});
        LogHelper.debug({className: "IconText", tag: "prevState", msg: prevState, isJson: true});
        LogHelper.debug({className: "IconText", tag: "snapshot", msg: snapshot, isJson: true});
        LogHelper.debug({msg: ""});
    }

    static getDerivedStateFromError(error) {
        LogHelper.info({className: "IconText", msg: "getDerivedStateFromError----------"});
        LogHelper.debug({className: "IconText", tag: "error", msg: error, isJson: true});
        LogHelper.debug({msg: ""});
        return {hasError: true};
    }

    componentDidCatch(error, info) {
        LogHelper.info({className: "IconText", msg: "componentDidCatch----------"});
        LogHelper.debug({className: "IconText", tag: "error", msg: error, isJson: true});
        LogHelper.debug({className: "IconText", tag: "info", msg: info, isJson: true});
        LogHelper.debug({msg: ""});
    }
}

export default IconText;
