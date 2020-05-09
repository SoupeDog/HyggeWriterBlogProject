import React from 'react';
import LogHelper from "./../../utils/LogHelper.jsx";
import clsx from "clsx";

import {Layout, Typography} from 'antd';

const {Footer} = Layout;
const {Text, Paragraph} = Typography;

class MyFooter extends React.Component {

    constructor(props) {
        super(props);
        this.state = {hasError: false};
        LogHelper.info({className: "MyFooter", msg: "constructor----------"});
    }

    static getDerivedStateFromProps(nextProps, nextContext) {
        LogHelper.info({className: "MyFooter", msg: "getDerivedStateFromProps----------"});
        LogHelper.debug({className: "MyFooter", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "MyFooter", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return nextProps;
    }

    render() {
        if (this.state.hasError) {
            return <h1>Something went wrong.</h1>;
        } else {
            return (
                <Footer className={clsx('myFooter')}>
                    <Paragraph strong={true}>
                        Made with
                        <Text><a className="dependentLink" target="_blank" href="https://react.docschina.org"> React</a></Text>
                        <Text>&nbsp;&amp;&nbsp;<a className="dependentLink" target="_blank"
                                                  href="https://ant.design/index-cn">Ant Design</a></Text>
                    </Paragraph>
                    <Paragraph strong={true}>
                        Copyright© 20019-2020 我的小宅子 Power by Xavier
                    </Paragraph>
                    <Paragraph >
                        <a className="textItem policeLink" target="_blank"
                           href="http://www.beian.miit.gov.cn">津ICP备18004196号-1</a>
                        <a className="textItem policeLink" target="_blank"
                           href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=12010402000667">
                            <img src="https://www.xavierwang.cn/static/icon-police.png"/>
                            <span>&nbsp;津公网安备12010402000667号</span>
                        </a>
                    </Paragraph>
                </Footer>
            );
        }
    }

    shouldComponentUpdate(nextProps, nextState, nextContext) {
        LogHelper.info({className: "MyFooter", msg: "shouldComponentUpdate----------"});
        LogHelper.debug({className: "MyFooter", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "MyFooter", tag: "nextState", msg: nextState, isJson: true});
        LogHelper.debug({className: "MyFooter", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return true;
    }

    componentDidMount() {
        LogHelper.info({className: "MyFooter", msg: "componentDidMount----------"});
        LogHelper.debug({msg: ""});
    }

    getSnapshotBeforeUpdate(prevProps, prevState) {
        LogHelper.info({className: "MyFooter", msg: "getSnapshotBeforeUpdate----------"});
        LogHelper.debug({className: "MyFooter", tag: "prevProps", msg: prevProps, isJson: true});
        LogHelper.debug({className: "MyFooter", tag: "prevState", msg: prevState, isJson: true});
        LogHelper.debug({msg: ""});
        return null;
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        LogHelper.info({className: "MyFooter", msg: "componentDidUpdate----------"});
        LogHelper.debug({className: "MyFooter", tag: "prevProps", msg: prevProps, isJson: true});
        LogHelper.debug({className: "MyFooter", tag: "prevState", msg: prevState, isJson: true});
        LogHelper.debug({className: "MyFooter", tag: "snapshot", msg: snapshot, isJson: true});
        LogHelper.debug({msg: ""});
    }

    static getDerivedStateFromError(error) {
        LogHelper.info({className: "MyFooter", msg: "getDerivedStateFromError----------"});
        LogHelper.error({className: "MyFooter", tag: "error", msg: error, isJson: true});
        LogHelper.error({msg: ""});
        return {hasError: true};
    }

    componentDidCatch(error, info) {
        LogHelper.info({className: "MyFooter", msg: "componentDidCatch----------"});
        LogHelper.debug({className: "MyFooter", tag: "error", msg: error, isJson: true});
        LogHelper.debug({className: "MyFooter", tag: "info", msg: info, isJson: true});
        LogHelper.debug({msg: ""});
    }
}

export default MyFooter;
