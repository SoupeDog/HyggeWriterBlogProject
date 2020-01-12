import React from 'react';
import LogHelper from "../utils/LogHelper.jsx";

import '../../css/login.less';
import LoginFrom from "../component/LoginFrom.jsx";
import {Layout, Form} from 'antd';
import clsx from "clsx";

const {Header, Content, Footer} = Layout;

const WrappedNormalLoginForm = Form.create({name: 'normal_login'})(LoginFrom);
class LoginContainer extends React.Component {

    constructor(props) {
        super(props);
        this.state = {hasError: false};
        LogHelper.info({className: "loginContainer", msg: "constructor----------"});
    }

    static getDerivedStateFromProps(nextProps, nextContext) {
        LogHelper.info({className: "loginContainer", msg: "getDerivedStateFromProps----------"});
        LogHelper.debug({className: "loginContainer", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "loginContainer", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return nextProps;
    }

    render() {
        if (this.state.hasError) {
            return <h1>Something went wrong.</h1>;
        } else {
            return (
                <Layout className="layout">
                    <Header>
                        <div className="logo">我的小宅子---登录页面</div>
                    </Header>
                    <Content style={{padding: '0 50px'}}>
                        <WrappedNormalLoginForm/>
                    </Content>
                    <Footer className={clsx('myFooter')}>
                        <div>
                            <div>
                                <span>©2019 Xavier </span><span>Power by</span> <a className="dependentLink"
                                                                                   target="_blank"
                                                                                   href="https://react.docschina.org">React</a>
                                <span>&nbsp;&amp;&nbsp;</span> <a className="dependentLink" target="_blank"
                                                                  href="https://ant.design/index-cn">Ant Design</a>
                            </div>
                        </div>

                        <div><a
                            className="textItem policeLink" target="_blank"
                            href="http://www.beian.miit.gov.cn">津ICP备18004196号-1</a>
                            <a target="_blank"
                               href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=12010402000667"
                               className="textItem policeLink">
                                <img src="https://www.xavierwang.cn/static/icon-police.png"/>
                                <span>&nbsp;津公网安备12010402000667号</span>
                            </a>
                        </div>
                    </Footer>
                </Layout>
            );
        }
    }

    shouldComponentUpdate(nextProps, nextState, nextContext) {
        LogHelper.info({className: "loginContainer", msg: "shouldComponentUpdate----------"});
        LogHelper.debug({className: "loginContainer", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "loginContainer", tag: "nextState", msg: nextState, isJson: true});
        LogHelper.debug({className: "loginContainer", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return true;
    }

    componentDidMount() {
        LogHelper.info({className: "loginContainer", msg: "componentDidMount----------"});
        LogHelper.debug({msg: ""});
    }

    getSnapshotBeforeUpdate(prevProps, prevState) {
        LogHelper.info({className: "loginContainer", msg: "getSnapshotBeforeUpdate----------"});
        LogHelper.debug({className: "loginContainer", tag: "prevProps", msg: prevProps, isJson: true});
        LogHelper.debug({className: "loginContainer", tag: "prevState", msg: prevState, isJson: true});
        LogHelper.debug({msg: ""});
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        LogHelper.info({className: "loginContainer", msg: "componentDidUpdate----------"});
        LogHelper.debug({className: "loginContainer", tag: "prevProps", msg: prevProps, isJson: true});
        LogHelper.debug({className: "loginContainer", tag: "prevState", msg: prevState, isJson: true});
        LogHelper.debug({className: "loginContainer", tag: "snapshot", msg: snapshot, isJson: true});
        LogHelper.debug({msg: ""});
    }

    static getDerivedStateFromError(error) {
        LogHelper.info({className: "loginContainer", msg: "getDerivedStateFromError----------"});
        LogHelper.error({className: "loginContainer", tag: "error", msg: error, isJson: true});
        LogHelper.error({msg: ""});
        return {hasError: true};
    }

    componentDidCatch(error, info) {
        LogHelper.info({className: "loginContainer", msg: "componentDidCatch----------"});
        LogHelper.debug({className: "loginContainer", tag: "error", msg: error, isJson: true});
        LogHelper.debug({className: "loginContainer", tag: "info", msg: info, isJson: true});
        LogHelper.debug({msg: ""});
    }
}

export default LoginContainer;
