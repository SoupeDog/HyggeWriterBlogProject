import React from 'react';
import LogHelper from "../utils/LogHelper.jsx";
import {Layout, Menu, Sider} from "antd";
import Vditor from "vditor";
import $ from 'jquery'
import 'antd/dist/antd.less';
import '../../css/browse.less';
import "vditor/src/assets/scss/classic.scss"
import WindowsEventHelper from "../utils/WindowsEventHelper.jsx";
import clsx from "clsx";
// 或者使用 dark
const {Header, Content, Footer} = Layout;

class BrowseContainer extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            hasError: false,
            toc: [],
            headerTransparent: true
        }
        this.initTOC = function () {
            let currentTOC = new Array();
            $("#preview").find("h1,h2,h3,h4,h5").each(function () {
                let currentTarget = $(this);
                let text = currentTarget.text();
                let nodeName = currentTarget.prop("nodeName");
                currentTOC.push({
                    text: text,
                    nodeName: nodeName
                });
            });
            if (currentTOC.length > 0) {
                this.setState({
                    toc: currentTOC
                });
            }
        }.bind(this);
        LogHelper.info({className: "BrowseContainer", msg: "constructor----------"});
    }

    static getDerivedStateFromProps(nextProps, nextContext) {
        LogHelper.info({className: "BrowseContainer", msg: "getDerivedStateFromProps----------"});
        LogHelper.debug({className: "BrowseContainer", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "BrowseContainer", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return nextProps;
    }

    render() {
        if (this.state.hasError) {
            return <h1>Something went wrong.</h1>;
        } else {
            return (
                <Layout className="layout">
                    <Header className={clsx({
                        "backgroundTransparent": this.state.headerTransparent
                    })} style={{position: 'fixed', zIndex: 1, width: '100%'}}>
                        <div className="logo"/>
                        <Menu
                            className={clsx({
                                "backgroundTransparent": this.state.headerTransparent
                            })}
                            theme="dark"
                            mode="horizontal"
                            defaultSelectedKeys={['0']}
                            style={{lineHeight: '64px'}}
                        >
                            {this.state.toc.map((item, index) => {
                                return (
                                    <Menu.Item key={index} onClick={() => {
                                        $("#preview").find(item.nodeName).each(function () {
                                            let currentTarget = $(this);
                                            let text = currentTarget.text();
                                            if (text == item.text) {
                                                $('html, body').animate({scrollTop: currentTarget.offset().top - 64}, 300);
                                            }
                                        });
                                    }}>{item.text}</Menu.Item>
                                );
                            })}
                        </Menu>
                    </Header>
                    <div id="mainImage" style={{width: "100%"}}></div>
                    <Content style={{marginTop: '20px', padding: '0 50px'}}>
                        <div id="preview" style={{background: '#fff', padding: 24, minHeight: 300}}>
                        </div>
                    </Content>
                    <Footer style={{textAlign: 'center'}}>Ant Design ©2018 Created by Ant UED</Footer>
                </Layout>
            );
        }
    }

    shouldComponentUpdate(nextProps, nextState, nextContext) {
        LogHelper.info({className: "BrowseContainer", msg: "shouldComponentUpdate----------"});
        LogHelper.debug({className: "BrowseContainer", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "BrowseContainer", tag: "nextState", msg: nextState, isJson: true});
        LogHelper.debug({className: "BrowseContainer", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return true;
    }

    componentDidMount() {
        Vditor.preview(document.getElementById('preview'),
            this.props.article.content, {
                className: 'preview vditor-reset vditor-reset--anchor',
                anchor: false
            });
        let _react = this;
        window.setTimeout(function () {
            _react.initTOC();
        }, 1000);
        WindowsEventHelper.addCallback_Scroll({
            name: "APPBar 透明判定", delta: 50, callbackFunction: function ({currentScrollY}) {
                if (currentScrollY > 336) {
                    _react.setState({headerTransparent: false});
                } else {
                    _react.setState({headerTransparent: true});
                }
            }
        });
        WindowsEventHelper.start_OnScroll();
        LogHelper.debug({className: "BrowseContainer", msg: "componentDidMount----------"});
        LogHelper.debug({msg: ""});
    }

    getSnapshotBeforeUpdate(prevProps, prevState) {
        LogHelper.info({className: "BrowseContainer", msg: "getSnapshotBeforeUpdate----------"});
        LogHelper.debug({className: "BrowseContainer", tag: "prevProps", msg: prevProps, isJson: true});
        LogHelper.debug({className: "BrowseContainer", tag: "prevState", msg: prevState, isJson: true});
        LogHelper.debug({msg: ""});
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        LogHelper.info({className: "BrowseContainer", msg: "componentDidUpdate----------"});
        LogHelper.debug({className: "BrowseContainer", tag: "prevProps", msg: prevProps, isJson: true});
        LogHelper.debug({className: "BrowseContainer", tag: "prevState", msg: prevState, isJson: true});
        LogHelper.debug({className: "BrowseContainer", tag: "snapshot", msg: snapshot, isJson: true});
        LogHelper.debug({msg: ""});
    }

    static getDerivedStateFromError(error) {
        LogHelper.info({className: "BrowseContainer", msg: "getDerivedStateFromError----------"});
        LogHelper.error({className: "BrowseContainer", tag: "error", msg: error, isJson: true});
        LogHelper.error({msg: ""});
        return {hasError: true};
    }

    componentDidCatch(error, info) {
        LogHelper.info({className: "BrowseContainer", msg: "componentDidCatch----------"});
        LogHelper.debug({className: "BrowseContainer", tag: "error", msg: error, isJson: true});
        LogHelper.debug({className: "BrowseContainer", tag: "info", msg: info, isJson: true});
        LogHelper.debug({msg: ""});
    }
}

export default BrowseContainer;
