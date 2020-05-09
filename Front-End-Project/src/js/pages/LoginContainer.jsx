import React from 'react';
import LogHelper from "../utils/LogHelper.jsx";

import '../../css/login.less';
import LoginFrom from "../component/LoginFrom.jsx";
import {Layout} from 'antd';
import MyFooter from "../component/public/MyFooter.jsx";

const {Header, Content} = Layout;

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
                        <LoginFrom/>
                    </Content>
                    <MyFooter id={"login"}/>
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
        return null;
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
