import React from 'react';

import {Layout} from 'antd';
const {Header,  Content} = Layout;

import LogHelper from "../utils/LogHelper.jsx";

// import 'antd/dist/antd.css'
import EditorForm from "../component/EditorForm.jsx";
import MyFooter from "../component/public/MyFooter.jsx";



class EditorContainer extends React.Component {

    constructor(props) {
        super(props);
        this.state = {hasError: false};
        LogHelper.info({className: "EditorContainer", msg: "constructor----------"});
    }

    static getDerivedStateFromProps(nextProps, nextContext) {
        LogHelper.info({className: "EditorContainer", msg: "getDerivedStateFromProps----------"});
        LogHelper.debug({className: "EditorContainer", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "EditorContainer", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return nextProps;
    }

    render() {
        if (this.state.hasError) {
            return <h1>Something went wrong.</h1>;
        } else {
            return (
                <Layout>
                    <Header>
                        <div className="logo">我的小宅子---编辑页面</div>
                    </Header>
                    <Content style={{padding: '0 50px', marginTop: 64}}>
                        <div style={{background: '#fff', padding: 24, minHeight: 380}}>
                            <EditorForm />
                            <div id="preview" style={{background: '#fff', padding: 24, minHeight: 300}}>
                            </div>
                        </div>
                    </Content>
                    <MyFooter id={"editor"}/>
                </Layout>
            );
        }
    }

    shouldComponentUpdate(nextProps, nextState, nextContext) {
        LogHelper.info({className: "EditorContainer", msg: "shouldComponentUpdate----------"});
        LogHelper.debug({className: "EditorContainer", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "EditorContainer", tag: "nextState", msg: nextState, isJson: true});
        LogHelper.debug({className: "EditorContainer", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return true;
    }

    componentDidMount() {
        LogHelper.info({className: "EditorContainer", msg: "componentDidMount----------"});
        LogHelper.debug({msg: ""});
    }

    getSnapshotBeforeUpdate(prevProps, prevState) {
        LogHelper.info({className: "EditorContainer", msg: "getSnapshotBeforeUpdate----------"});
        LogHelper.debug({className: "EditorContainer", tag: "prevProps", msg: prevProps, isJson: true});
        LogHelper.debug({className: "EditorContainer", tag: "prevState", msg: prevState, isJson: true});
        LogHelper.debug({msg: ""});
        return null;
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        LogHelper.info({className: "EditorContainer", msg: "componentDidUpdate----------"});
        LogHelper.debug({className: "EditorContainer", tag: "prevProps", msg: prevProps, isJson: true});
        LogHelper.debug({className: "EditorContainer", tag: "prevState", msg: prevState, isJson: true});
        LogHelper.debug({className: "EditorContainer", tag: "snapshot", msg: snapshot, isJson: true});
        LogHelper.debug({msg: ""});
    }

    static getDerivedStateFromError(error) {
        LogHelper.info({className: "EditorContainer", msg: "getDerivedStateFromError----------"});
        LogHelper.error({className: "EditorContainer", tag: "error", msg: error, isJson: true});
        LogHelper.error({msg: ""});
        return {hasError: true};
    }

    componentDidCatch(error, info) {
        LogHelper.info({className: "EditorContainer", msg: "componentDidCatch----------"});
        LogHelper.debug({className: "EditorContainer", tag: "error", msg: error, isJson: true});
        LogHelper.debug({className: "EditorContainer", tag: "info", msg: info, isJson: true});
        LogHelper.debug({msg: ""});
    }
}

export default EditorContainer;
