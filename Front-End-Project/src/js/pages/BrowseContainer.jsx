import React from 'react';

import '../../css/browse.less';

import clsx from "clsx";
import {Card, Layout, Menu, Tree} from "antd";
import {DownOutlined} from '@ant-design/icons';

const {TreeNode} = Tree;
const {Header, Content} = Layout;

import WindowsEventHelper from "../utils/WindowsEventHelper.jsx";
import LogHelper from "../utils/LogHelper.jsx";

import ArticleCategoryBreadcrumb from "../component/ArticleCategoryBreadcrumb.jsx";
import IconText from "../component/IconText.jsx";
import MyFooter from "../component/public/MyFooter.jsx";
import MDHelper from "../utils/MDHelper.jsx";

class BrowseContainer extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            hasError: false,
            toc: [],
            rootTocTreeList: [],
            allTocNodeMap: new Map(),
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
            // 初始化全部节点的 map 容器
            let allTocNodeMap = new Map();
            currentTOC.map((item, index) => {
                item.nodeId = index;
                allTocNodeMap.set(index, item);
            });

            console.log(currentTOC);
            let currentRootTocTreeList = MDHelper.getTocTree({
                currentTOCArray: currentTOC,
                allTocNodeMap: allTocNodeMap
            });

            if (currentTOC.length > 0) {
                this.setState({
                    toc: currentTOC,
                    rootTocTreeList: currentRootTocTreeList,
                    allTocNodeMap: allTocNodeMap
                });
            }
            // 清除代码高度限制
            $("#preview").find(".hljs").each(function () {
                let currentTarget = $(this);
                currentTarget.css("max-height", "");
            });
        }.bind(this);

        this.jumpToToc = function (selectedKeys, info) {
            // 强转成数字类型
            let nodeId = selectedKeys[0] * 1;
            let tocNode = this.state.allTocNodeMap.get(nodeId);
            console.log(nodeId);
            console.log(tocNode);
            console.log(this.state.allTocNodeMap);
            if (tocNode != null) {
                $("#preview").find(tocNode.nodeName).each(function () {
                    let currentTarget = $(this);
                    let text = currentTarget.text();
                    if (text == tocNode.text) {
                        $('html, body').animate({scrollTop: currentTarget.offset().top - 64}, 300);
                    }
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
                <Layout id={"myPage"}>
                    <Layout className="layout">
                        <Header className={clsx({
                            "backgroundTransparent": this.state.headerTransparent
                        })} style={{position: 'fixed', zIndex: 99999, width: '100%'}}>
                            <div className="logo"/>
                            <Menu
                                className={clsx({
                                    "backgroundTransparent": this.state.headerTransparent
                                })}
                                theme={this.state.headerTransparent ? "light" : "dark"}
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
                            background: "url(" + this.props.article.properties.bgi + ") no-repeat center"
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
                            {this.state.rootTocTreeList.length ? (
                                <Tree
                                    showLine
                                    treeData={[{key: 1, title: "测试", children: []}]}
                                    switcherIcon={<DownOutlined/>}
                                    defaultExpandedKeys={['0-0-0']}
                                    onSelect={this.jumpToToc}
                                >
                                    {/*{this.state.rootTocTreeList.map((rootItem) => {*/}
                                        {/*return (this.renderTocItem({item: rootItem}))*/}
                                    {/*})}*/}
                                </Tree>
                            ) : null}

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
                        <MyFooter id={"browse"}/>
                    </Layout>
                </Layout>
            );
        }
    }

    renderTocItem({item}) {
        let _react = this;
        if (item.childList.length < 1) {
            return (<TreeNode title={item.text} key={item.nodeId}/>);
        } else {
            return (
                <TreeNode title={item.text} key={item.nodeId}>
                    {
                        item.childList.map((childItem) => {
                            return (_react.renderTocItem({item: childItem}))
                        })
                    }
                </TreeNode>
            )
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
                anchor: false,
                hljs: {
                    style: "native",
                    lineNumber: true
                },
                markdown: {
                    toc: true
                }
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
