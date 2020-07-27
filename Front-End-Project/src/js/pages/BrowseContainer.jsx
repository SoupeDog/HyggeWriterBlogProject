import React from 'react';

import '../../css/browse.less';
import '../../css/musicPlayer.css';

import clsx from "clsx";
import {Card, Layout, Affix, Tree, message, Avatar, Dropdown, Menu, Tooltip} from "antd";
import {DownOutlined, EditOutlined, CloseCircleOutlined} from '@ant-design/icons';

message.config({
    top: 80,
    maxCount: 3
});

const {TreeNode} = Tree;
const {Header, Content} = Layout;

import WindowsEventHelper from "../utils/WindowsEventHelper.jsx";
import LogHelper from "../utils/LogHelper.jsx";
import MDHelper from "../utils/MDHelper.jsx";
import ArticleAPICaller from "../utils/ArticleAPICaller.jsx";
import URLHelper from "../utils/URLHelper.jsx";

import ArticleCategoryBreadcrumb from "../component/ArticleCategoryBreadcrumb.jsx";
import IconText from "../component/IconText.jsx";
import MyFooter from "../component/public/MyFooter.jsx";

const userMenu = (
    <Menu onClick={(event) => {
        switch (event.key) {
            case "logout":
                ArticleAPICaller.removeLoginInfo();
                URLHelper.openNewPage({finalUrl: URLHelper.getJumpPrefix(), inNewTab: false})
                break;
            case "editor":
                let finalUrl = URLHelper.getJumpPrefix() + "editor.html";
                let currentArticleNo = URLHelper.getQueryString("articleNo");
                if (currentArticleNo != null) {
                    finalUrl = finalUrl + "?articleNo=" + currentArticleNo;
                }
                URLHelper.openNewPage({finalUrl: finalUrl, inNewTab: false})
                break;
        }
    }}>
        <Menu.Item key="editor" icon={<EditOutlined/>}>
            编辑文章
        </Menu.Item>
        <Menu.Item key="logout" icon={<CloseCircleOutlined/>}>
            登出
        </Menu.Item>
    </Menu>
);


class BrowseContainer extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            hasError: false,
            rootTocTreeList: [],
            loginUserInfo: JSON.parse(localStorage.getItem('userInfo')),
            headerTransparent: true
        }
        this.initTOC = function () {
            let currentTOC = new Array();
            $("#preview").find("h1,h2,h3,h4,h5").each(function () {
                let currentTarget = $(this);
                let title = currentTarget.text();
                let nodeName = currentTarget.prop("nodeName");
                currentTOC.push({
                    title: title,
                    nodeName: nodeName
                });
            });
            // 初始化全部节点的 map 容器
            let allTocNodeMap = new Map();
            currentTOC.map((item, index) => {
                item.key = index;
                allTocNodeMap.set(index, item);
            });

            let currentRootTocTreeList = MDHelper.getTocTree({
                currentTOCArray: currentTOC,
                allTocNodeMap: allTocNodeMap
            });

            if (currentRootTocTreeList.length > 0) {
                this.setState({
                    rootTocTreeList: currentRootTocTreeList
                });
            }
            // 清除代码高度限制
            $("#preview").find(".hljs").each(function () {
                let currentTarget = $(this);
                currentTarget.css("max-height", "");
            });
        }.bind(this);

        this.jumpToToc = function (selectedKeys, info) {
            $("#preview").find(info.node.nodeName).each(function () {
                let currentTarget = $(this);
                let title = currentTarget.text();
                if (title == info.node.title) {
                    $('html, body').animate({scrollTop: currentTarget.offset().top - 64}, 300);
                }
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
                    <Header className={clsx({
                        "backgroundTransparent": this.state.headerTransparent
                    })} style={{position: 'fixed', zIndex: 99999, width: '100%'}}>
                        <div className={"floatLeft"} style={{"marginLeft": "0px"}}>
                            <Tooltip placement="bottom" title={"返回首页"}>
                                <i className="pointer iconfont" style={{color: "#fff", fontSize: 24}} onClick={() => {
                                    URLHelper.openNewPage({finalUrl: URLHelper.getJumpPrefix()});
                                }}>&#xe67a;</i>
                            </Tooltip>
                        </div>
                        {this.state.loginUserInfo != null ?
                            <div className={"floatRight"} style={{"marginRight": "20px"}}>
                                <Dropdown overlay={userMenu} placement="bottomCenter" trigger={["click"]}>
                                    <Avatar className={"pointer"} size={48} src={this.state.loginUserInfo.userAvatar}/>
                                </Dropdown>
                            </div> :
                            null}
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
                                    {/*<audio id="h5audio_media" controls*/}
                                    {/*       style={{margin: "0 auto 0 auto", width: "100%"}} autoPlay={true}*/}
                                    {/*       src={this.props.article.properties.bgmConfig.src}>*/}
                                    {/*</audio>*/}
                                </div>}
                        </div>
                    }
                    <Layout className="layout">
                        {this.state.rootTocTreeList.length > 0 ? (
                            <Affix style={{zIndex: 9999, position: 'absolute', top: 664, right: 20}} offsetTop={164}>
                                <div className="tocBox">
                                    <div className="tocTitle">目录</div>
                                    <Tree
                                        className="tocBody"
                                        showLine
                                        treeData={this.state.rootTocTreeList}
                                        switcherIcon={<DownOutlined/>}
                                        defaultExpandedKeys={['0-0-0']}
                                        onSelect={this.jumpToToc}
                                    >
                                    </Tree>
                                </div>
                            </Affix>
                        ) : null}
                        <Content style={{
                            marginTop: '0px',
                            padding: '0 ' + (this.state.rootTocTreeList.length > 0 ? '300px' : '50px') + ' 0 50px'
                        }}>
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
            return (<TreeNode title={item.title} key={item.key}/>);
        } else {
            return (
                <TreeNode title={item.title} key={item.key}>
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
                    sanitize: false,
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
        if (_react.props.article.properties.bgmConfig.src != null &&
            // 为直连音频时
            _react.props.article.properties.bgmConfig.bgmType == 2) {
            let musicURL = _react.props.article.properties.bgmConfig.src;
            let musicName;
            let musicSinger;
            let musicCover = _react.props.article.properties.bgmConfig.cover == null ? "https://www.xavierwang.cn/static/defaultCover.png" : _react.props.article.properties.bgmConfig.cover;

            if (_react.props.article.properties.bgmConfig.name == null ||
                _react.props.article.properties.bgmConfig.artist == null) {
                // 样例 優しい嘘-上原れな.mp3
                let index = _react.props.article.properties.bgmConfig.src.lastIndexOf("/");
                // 不包括 “/” 本身所以 +1
                let singerAndName = musicURL.slice(index + 1);
                index = singerAndName.indexOf(".");
                // 去除扩展名
                singerAndName = singerAndName.slice(0, index);
                index = singerAndName.indexOf("-");
                musicName = singerAndName.slice(0, index);
                musicSinger = singerAndName.slice(index + 1);
            } else {
                musicName = _react.props.article.properties.bgmConfig.name;
                musicSinger = _react.props.article.properties.bgmConfig.artist;
            }

            let myMusicPlayer = new APlayer({
                container: document.getElementById('txPlayer'),
                autoplay: true,
                audio: [{
                    name: musicName,
                    artist: musicSinger,
                    url: _react.props.article.properties.bgmConfig.src,
                    cover: musicCover
                }]
            });
        }

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
