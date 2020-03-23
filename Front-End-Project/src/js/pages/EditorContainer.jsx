import React from 'react';
import LogHelper from "../utils/LogHelper.jsx";
import {Form, Layout, Menu} from 'antd';
import EditorForm from "../component/EditorForm.jsx";
import "vditor/src/assets/scss/classic.scss"
import clsx from "clsx";
const {Header, Footer, Sider, Content} = Layout;

const EditorArticleForm = Form.create({name: 'editorArticle'})(EditorForm);

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
                            <EditorArticleForm />
                            <div id="preview" style={{background: '#fff', padding: 24, minHeight: 300}}>
                            </div>
                        </div>
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
