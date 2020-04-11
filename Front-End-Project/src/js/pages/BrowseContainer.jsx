import React from 'react';
import LogHelper from "../utils/LogHelper.jsx";
import {Card, Layout, Menu, Sider} from "antd";
import Vditor from "vditor";
import $ from 'jquery'
import 'antd/dist/antd.less';
import '../../css/browse.less';
import WindowsEventHelper from "../utils/WindowsEventHelper.jsx";
import clsx from "clsx";
import ArticleCategoryBreadcrumb from "../component/ArticleCategoryBreadcrumb.jsx";
import IconText from "../component/IconText.jsx";
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
            // 清除代码高度限制
            $("#preview").find(".hljs").each(function () {
                let currentTarget = $(this);
                currentTarget.css("max-height", "");
            });
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
                <Layout id={"myPage"}>
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
                        <div id="mainImage" style={{
                            width: "100%",
                            background: "url("+this.props.article.properties.bgi+") no-repeat center"
                        }}>
                        </div>
                        {this.props.article.properties.bgmConfig.src == null ? null :
                            <div id="bgmPlayer">
                                {this.props.article.properties.bgmConfig.bgmType != 1 ? null :
                                    // 网易音乐播放
                                    <div id={"wyPlayer"}>
                                        <iframe frameBorder="no" border="0" marginWidth="0" width={"100%"}
                                                height={"100px"}
                                                marginHeight="0"
                                                src={this.props.article.properties.bgmConfig.src}></iframe>
                                    </div>}
                                {this.props.article.properties.bgmConfig.bgmType != 2 ? null :
                                    // 音频直连播放
                                    <div id={"txPlayer"}>
                                        <audio id="h5audio_media" controls
                                               style={{margin: "0 auto 0 auto", width: "100%"}} autoPlay={true}
                                               src={this.props.article.properties.bgmConfig.src}>
                                        </audio>
                                    </div>}
                            </div>
                        }

                        <Content style={{marginTop: '0px', padding: '0 50px'}}>
                            <Card title={this.props.article.title} bordered={false}
                                  style={{marginTop: '20px', marginBottom: "10px"}}>
                                <ArticleCategoryBreadcrumb articleInfo={this.props.article}/>
                                <div style={{marginTop: "20px"}}>
                                    <IconText type="&#xe61b;" tip={"字数统计"}
                                              text={this.props.article.wordCount}
                                              key="wordCount"/>
                                    <IconText type="&#xe640;" tip={"浏览量"}
                                              text={this.props.article.pageViews}
                                              key="view"/>
                                    <IconText type="&#xe638;" tip={"创建时间"}
                                              text={this.props.article.createTs}
                                              key="createTs" isTimeStamp={true}/>
                                    <IconText type="&#xe614;" tip={"最后修改时间"}
                                              text={this.props.article.lastUpdateTs}
                                              key="lastUpdateTs" isTimeStamp={true}/>
                                </div>
                            </Card>

                            <div id="preview" style={{background: '#fff', padding: 24, minHeight: 300}}>
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
        // 修改页面 title
        $("title").text(this.props.article.title);
        Vditor.preview(document.getElementById('preview'),
            this.props.article.content, {
                className: 'preview vditor-reset vditor-reset--anchor',
                anchor: false
            });
        let _react = this;
        // 生成目录
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
        return null;
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
