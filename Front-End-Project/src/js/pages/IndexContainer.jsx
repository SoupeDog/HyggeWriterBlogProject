import React from 'react';

import '../../css/index.less';
import csdnLogo from '../../img/csdnLogo.png';

import clsx from 'clsx';
// import $ from 'jquery'
import {
    LinkOutlined,
    GithubOutlined,
    QuestionCircleOutlined,
    MenuUnfoldOutlined,
    MenuFoldOutlined,
    CloseCircleOutlined,
    EditOutlined
} from '@ant-design/icons';

import {
    Avatar,
    BackTop,
    Badge,
    Layout,
    Menu,
    message,
    Modal,
    notification,
    Tabs,
    Input,
    Card,
    Spin,
    Typography,
    Dropdown,
    Button,
    Collapse,
    Divider,
} from 'antd';

const {Header, Sider, Content} = Layout;
const {TabPane} = Tabs;
const {Search} = Input;
const {Panel} = Collapse;
const {Title} = Typography;

message.config({
    top: 80,
    maxCount: 3
});

notification.config({
    top: 80
});

import LogHelper from '../utils/LogHelper.jsx';
import URLHelper from "../utils/URLHelper.jsx";
import ArticleAPICaller from "../utils/ArticleAPICaller.jsx";


import NotTechnologyBoardView from "../component/NotTechnologyBoardView.jsx";
import TechnologyBoardView from "../component/TechnologyBoardView.jsx";
import MyFooter from "../component/public/MyFooter.jsx";
import SearchView from "../component/SearchView.jsx";

const userMenu = (
    <Menu onClick={(event) => {
        switch (event.key) {
            case "logout":
                ArticleAPICaller.removeLoginInfo();
                URLHelper.openNewPage({finalUrl: URLHelper.getJumpPrefix(), inNewTab: false})
                break;
            case "editor":
                URLHelper.openNewPage({finalUrl: URLHelper.getJumpPrefix() + "editor.html", inNewTab: false})
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

class IndexContainer extends React.Component {

    constructor(props) {
        super(props);
        let currentPageInit = this.props.currentPageInit;
        let pageSizeInit = this.props.pageSizeInit;
        let initTechnologyTotalCount = this.props.technologyTotalCountInit;
        let initTechnologySummaryList = this.props.technologySummaryListInit;
        let articleCountInfoOfBoardList = this.props.articleCountInfoOfBoardList;
        this.state = {
            loadingCountArray: new Array(0),
            loginUserInfo: JSON.parse(localStorage.getItem('userInfo')),
            headerTransparent: false,
            mainMenuCollapsed: true,
            articleCountInfoOfBoardList: articleCountInfoOfBoardList,
            technologyCurrentPage: currentPageInit,
            technologyPageSize: pageSizeInit,
            technologyTotalCount: initTechnologyTotalCount,
            technologySummaryList: initTechnologySummaryList,
            notTechnologyCurrentPage: currentPageInit,
            notTechnologyPageSize: pageSizeInit,
            notTechnologyTotalCount: articleCountInfoOfBoardList[1].boardArticleCountInfo.totalCount,
            notTechnologySummaryList: [],
            searchType: "keyword",
            searchProperties: "",
            searchCurrentPage: currentPageInit,
            searchPageSize: pageSizeInit,
            searchTotalCount: 0,
            searchSummaryList: []
        };
        // 添加一个加载事件
        this.addLoadingEvent = function () {
            this.state.loadingCountArray.push(0);
            this.setState({loadingCountArray: this.state.loadingCountArray});
        }.bind(this);

        // 移除一个加载事件
        this.removeLoadingEvent = function () {
            this.state.loadingCountArray.pop();
            this.setState({loadingCountArray: this.state.loadingCountArray});
        }.bind(this);

        // 左侧菜单缩进
        this.mainMenuToggle = function () {
            this.setState({
                mainMenuCollapsed: !this.state.mainMenuCollapsed
            });
        }.bind(this);

        // 标签页变更
        this.onTabChange = function ({activeKey, currentPage}) {
            let actualCurrentPage;
            let _react = this;
            switch (activeKey) {
                case "1":
                    // alert("技术板块" + this.props.technologyBoardNo);
                    actualCurrentPage = currentPage == null ? _react.state.technologyCurrentPage : currentPage;
                    ArticleAPICaller.querySummaryByPageOfBoard({
                        boardNo: _react.props.technologyBoardNo,
                        currentPage: actualCurrentPage,
                        pageSize: _react.state.technologyPageSize,
                        requestBefore: function () {
                            _react.addLoadingEvent();
                        },
                        successCallback: function (response) {
                            _react.refreshSummary(activeKey, actualCurrentPage, response.data.totalCount, response.data.resultSet);
                        },
                        errorCallback: function (response) {
                            message.warn(response.msg, 2)
                        },
                        finallyCallback: function () {
                            _react.removeLoadingEvent();
                        }
                    });
                    break;
                case "2":
                    // alert("非技术板块" + this.props.notTechnologyBoardNo);
                    actualCurrentPage = currentPage == null ? _react.state.notTechnologyCurrentPage : currentPage;
                    ArticleAPICaller.querySummaryByPageOfBoard({
                        boardNo: _react.props.notTechnologyBoardNo,
                        currentPage: actualCurrentPage,
                        pageSize: _react.state.notTechnologyPageSize,
                        requestBefore: function () {
                            _react.addLoadingEvent();
                        },
                        successCallback: function (response) {
                            _react.refreshSummary(activeKey, actualCurrentPage, response.data.totalCount, response.data.resultSet);
                        },
                        errorCallback: function (response) {
                            message.warn(response.msg, 2)
                        },
                        finallyCallback: function () {
                            _react.removeLoadingEvent();
                        }
                    });
                    break;
                case "3":
                    break
                default:
                    console.log("非预期的 Tabs [activeKey]");
            }
        }.bind(this);

        this.refreshSummaryOfSearch = function ({searchType, searchProperties, currentPage}) {
            $("#searchTap").click();
            let _react = this;
            let actualCurrentPage = currentPage == null ? this.state.searchCurrentPage : currentPage;
            // 根据 searchType 把 searchProperties 作为 keyword 或者 articleCategoryNo
            switch (searchType) {
                case "keyword":
                    ArticleAPICaller.querySummaryByPageOfSearch({
                        keyword: searchProperties,
                        currentPage: actualCurrentPage,
                        pageSize: _react.state.searchPageSize,
                        requestBefore: function () {
                            _react.addLoadingEvent();
                        },
                        successCallback: function (response) {
                            let finalSummaryList;
                            if (response.data.resultSet == null) {
                                finalSummaryList = new Array(0);
                            } else {
                                finalSummaryList = response.data.resultSet;
                            }
                            _react.setState({
                                searchType: searchType,
                                searchProperties: searchProperties,
                                searchCurrentPage: actualCurrentPage,
                                searchTotalCount: response.data.totalCount,
                                searchSummaryList: finalSummaryList
                            });
                        },
                        errorCallback: function (response) {
                            message.warn(response.msg, 2)
                        },
                        finallyCallback: function () {
                            _react.removeLoadingEvent();
                        }
                    });
                    break;
                case "articleCategory":
                    ArticleAPICaller.querySummaryByPageOfArticleCategory({
                        articleCategoryNo: searchProperties,
                        currentPage: actualCurrentPage,
                        pageSize: _react.state.searchPageSize,
                        requestBefore: function () {
                            _react.addLoadingEvent();
                        },
                        successCallback: function (response) {
                            let finalSummaryList;
                            if (response.data.resultSet == null) {
                                finalSummaryList = new Array(0);
                            } else {
                                finalSummaryList = response.data.resultSet;
                            }
                            _react.setState({
                                searchType: searchType,
                                searchProperties: searchProperties,
                                searchCurrentPage: actualCurrentPage,
                                searchTotalCount: response.data.totalCount,
                                searchSummaryList: finalSummaryList
                            });
                        },
                        errorCallback: function (response) {
                            message.warn(response.msg, 2)
                        },
                        finallyCallback: function () {
                            _react.removeLoadingEvent();
                        }
                    });
                    break;
                default:
                    message.warn("未知的搜索类型", 1);
            }
        }.bind(this);

        this.refreshSummary = function (activeKey, actualCurrentPage, totalCount, summaryList) {
            let finalSummaryList;
            if (summaryList == null) {
                finalSummaryList = new Array(0);
            } else {
                finalSummaryList = summaryList;
            }
            switch (activeKey) {
                // 技术板块更新
                case "1":
                    this.setState({
                        technologyCurrentPage: actualCurrentPage,
                        technologyTotalCount: totalCount,
                        technologySummaryList: finalSummaryList
                    });
                    break;
                // 非技术板块更新
                case "2":
                    this.setState({
                        notTechnologyCurrentPage: actualCurrentPage,
                        notTechnologyTotalCount: totalCount,
                        notTechnologySummaryList: finalSummaryList
                    });
                    break;
                case "3":
                    break
                default:
                    console.log("非预期的 Tabs [activeKey]");
            }
        }.bind(this);

        LogHelper.info({className: "IndexContainer", msg: "constructor----------"});
    }

    static getDerivedStateFromProps(nextProps, nextContext) {
        LogHelper.info({className: "IndexContainer", msg: "getDerivedStateFromProps----------"});
        LogHelper.debug({className: "IndexContainer", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "IndexContainer", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return nextProps;
    }

    shouldComponentUpdate(nextProps, nextState, nextContext) {
        LogHelper.info({className: "IndexContainer", msg: "shouldComponentUpdate----------"});
        LogHelper.debug({className: "IndexContainer", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "IndexContainer", tag: "nextState", msg: nextState, isJson: true});
        LogHelper.debug({className: "IndexContainer", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return true;
    }

    render() {
        return (
            <Layout id={"myPage"}>
                <Sider theme="dark" className="leftMenu" trigger={null} collapsible
                       collapsed={this.state.mainMenuCollapsed} style={{overflow: "hidden"}}>
                    <div className="logo autoOmit_2">{this.state.mainMenuCollapsed ? "宅" : "我的小宅子"}</div>
                    <Menu theme="dark" mode="inline" selectable={false}>
                        <Menu.Item key="1">
                            <i className="anticon anticon-link">
                                <Avatar size={14}
                                        src="https://www.xavierwang.cn/static/我的头像.png"/>
                            </i>
                            <span>Xavier</span>
                        </Menu.Item>
                        <Menu.Item key="2" onClick={() => {
                            URLHelper.openNewPage({
                                finalUrl: "https://blog.csdn.net/u014430366",
                                inNewTab: true
                            });
                        }}>
                            <i className="anticon anticon-link">
                                <Avatar size={14}
                                        src={csdnLogo}/>
                            </i>
                            <span>CSDN(已停更)</span>
                        </Menu.Item>
                        <Menu.Item key="3" onClick={() => {
                            URLHelper.openNewPage({
                                finalUrl: "https://github.com/SoupeDog",
                                inNewTab: true
                            });
                        }}>
                            <GithubOutlined/>
                            <span>GitHub</span>
                        </Menu.Item>
                        <Menu.Item key="4" onClick={() => {
                            message.warn('暂时还没有，有人在期待着一场 PY 交易嘛~', 2);
                        }}>
                            <LinkOutlined/>
                            <span>友链</span>
                        </Menu.Item>
                        <Menu.Item key="5" onClick={() => {
                            notification.info({
                                message: '关于',
                                description:
                                    '本站前端基于 React 、Antd、Vditor、APlayer 开发，后端基于 Spring Boot 全家桶开发，已在我的 Github 个人仓库开源。目标使用场景为 PC ，对手机端提供少数功能，平板将被视为手机端。本站全部音频、图片素材来源于网络，若侵犯了您的权益，请联系 xavierpe@qq.com 以便及时删除侵权素材。',
                            });
                        }}>
                            <QuestionCircleOutlined/>
                            <span>关于</span>
                        </Menu.Item>
                    </Menu>
                </Sider>
                <Layout>
                    <Header className={clsx({
                        "headerCollapsed": this.state.mainMenuCollapsed,
                        "header": !this.state.mainMenuCollapsed,
                        "backgroundTransparent": this.state.headerTransparent
                    })}>
                        <div className={"floatLeft"} style={{"width": "68px"}}>
                            {this.state.mainMenuCollapsed ?
                                <MenuUnfoldOutlined
                                    style={{color: "#fff", fontSize: "20px", padding: "0 24px", display: "inlineBlock"}}
                                    onClick={this.mainMenuToggle}/> :
                                <MenuFoldOutlined
                                    style={{color: "#fff", fontSize: "20px", padding: "0 24px", display: "inlineBlock"}}
                                    onClick={this.mainMenuToggle}/>
                            }
                        </div>
                        <div className={"floatRight"}
                             style={{"width": "20px", "marginRight": "220px"}}>
                            {this.state.loadingCountArray.length > 0 ? <Spin/> : null}
                        </div>
                        <div className={"floatRight"}
                             style={{"marginRight": this.state.loadingCountArray.length > 0 ? "20px" : "260px"}}>
                            {this.state.loginUserInfo == null ?
                                <Button type="success" onClick={(event) => {
                                    URLHelper.openNewPage({
                                        finalUrl: URLHelper.getJumpPrefix() + "login.html",
                                        inNewTab: false
                                    });
                                }}>登录</Button> :
                                <Dropdown overlay={userMenu} placement="bottomCenter" trigger={["click"]}>
                                    <Avatar className={"pointer"} size={48} src={this.state.loginUserInfo.userAvatar}/>
                                </Dropdown>}
                        </div>
                        <div className={"floatRight"} style={{"marginRight": "40px"}}>
                            <Search style={{"marginTop": "16px"}} placeholder="关键字" enterButton="搜索" size="middle"
                                    onSearch={(value) => {
                                        this.refreshSummaryOfSearch({
                                            searchType: "keyword",
                                            searchProperties: value,
                                            currentPage: 1
                                        });
                                        // console.log(value)
                                    }}/>
                        </div>
                    </Header>
                    <Content className={clsx({
                        "myContentCollapsed": this.state.mainMenuCollapsed,
                        "myContent": !this.state.mainMenuCollapsed
                    })}>
                        <Title level={4}>文章类别</Title>
                        {this.state.articleCountInfoOfBoardList.map((articleCountInfoOfBoard, index) => {
                            return (
                                <Collapse key={index}>
                                    <Panel header={articleCountInfoOfBoard.boardArticleCountInfo.boardName}
                                           key={articleCountInfoOfBoard.boardArticleCountInfo.boardId}>
                                        <Card size={"small"}>
                                            {articleCountInfoOfBoard.articleCategoryCountInfoList.map((articleCategoryCountInfo, index) => {
                                                if (articleCategoryCountInfo.totalCount > 0) {
                                                    return (
                                                        <Card.Grid className={"pointer"} key={index}
                                                                   style={{
                                                                       width: '12.5%',
                                                                       padding: "10px",
                                                                       textAlign: 'center'
                                                                   }} onClick={() => {
                                                            this.refreshSummaryOfSearch({
                                                                searchType: "articleCategory",
                                                                searchProperties: articleCategoryCountInfo.articleCategoryNo,
                                                                currentPage: 1
                                                            });
                                                            // alert(articleCategoryCountInfo.articleCategoryName);
                                                        }}>
                                                            <Badge count={articleCategoryCountInfo.totalCount}
                                                                   overflowCount={999} offset={[15, 0]}>
                                                                {articleCategoryCountInfo.articleCategoryName}
                                                            </Badge>
                                                        </Card.Grid>
                                                    )
                                                }
                                            })}
                                        </Card>
                                    </Panel>
                                </Collapse>
                            )
                        })}

                        <Divider/>

                        <Tabs tabBarGutter={50} defaultActiveKey="1" onChange={(activeKey) => {
                            this.onTabChange({activeKey: activeKey});
                        }}>
                            <TabPane
                                tab={
                                    <Badge
                                        overflowCount={99}
                                        offset={[20, -5]}
                                        count={this.state.technologyTotalCount == null ? "?" : this.state.technologyTotalCount}>
                                    <span>
                                      技 术
                                    </span>
                                    </Badge>
                                }
                                key="1"
                            >
                                <TechnologyBoardView technologySummaryList={this.state.technologySummaryList}
                                                     technologyCurrentPage={this.state.technologyCurrentPage}
                                                     technologyPageSize={this.state.technologyPageSize}
                                                     technologyTotalCount={this.state.technologyTotalCount}
                                                     onTabChange={this.onTabChange}/>
                            </TabPane>
                            <TabPane
                                tab={
                                    <Badge
                                        overflowCount={99}
                                        offset={[20, -5]}
                                        count={this.state.notTechnologyTotalCount == null ? "?" : this.state.notTechnologyTotalCount}>
                                        <span>
                                          非技术
                                        </span>
                                    </Badge>
                                }
                                key="2"
                            >
                                <NotTechnologyBoardView notTechnologySummaryList={this.state.notTechnologySummaryList}
                                                        notTechnologyCurrentPage={this.state.notTechnologyCurrentPage}
                                                        notTechnologyPageSize={this.state.notTechnologyPageSize}
                                                        notTechnologyTotalCount={this.state.notTechnologyTotalCount}
                                                        onTabChange={this.onTabChange}/>
                            </TabPane>
                            <TabPane
                                tab={
                                    <Badge
                                        overflowCount={99}
                                        offset={[20, -5]}
                                        count={this.state.searchTotalCount == null ? "?" : this.state.searchTotalCount}>
                                        <span id={"searchTap"}>
                                          搜索
                                        </span>
                                    </Badge>
                                }
                                key="3"
                            >
                                <SearchView searchType={this.state.searchType}
                                            searchProperties={this.state.searchProperties}
                                            searchSummaryList={this.state.searchSummaryList}
                                            searchCurrentPage={this.state.searchCurrentPage}
                                            searchPageSize={this.state.searchPageSize}
                                            searchTotalCount={this.state.searchTotalCount}
                                            refreshSummaryOfSearch={this.refreshSummaryOfSearch}/>
                                {/*<Empty description={"搜索功能未启用"}/>*/}
                            </TabPane>
                        </Tabs>
                    </Content>
                    <MyFooter id={"index"}/>
                </Layout>
                <BackTop>
                    <div id="ant-back-top-inner">Top</div>
                </BackTop>
            </Layout>
        );
    }

    componentDidMount() {
        let isPC = !(navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i));
        if (!isPC) {
            Modal.warning({
                title: '提示',
                content: '检测到客户端为移动端，本站未对移动端做特殊适配、优化处理，推荐您使用 PC 访问，若执意使用移动端，产生的故障请见谅。',
            });
        }
        let _react = this;
        LogHelper.info({className: "IndexContainer", msg: "componentDidMount----------"});
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        LogHelper.info({className: "IndexContainer", msg: "componentDidUpdate----------"});
        LogHelper.debug({className: "IndexContainer", tag: "prevProps", msg: prevProps, isJson: true});
        LogHelper.debug({className: "IndexContainer", tag: "prevState", msg: prevState, isJson: true});
        LogHelper.debug({className: "IndexContainer", tag: "snapshot", msg: snapshot, isJson: true});
        LogHelper.debug({msg: ""});
    }

    componentDidCatch(error, info) {
        LogHelper.info({className: "IndexContainer", msg: "componentDidCatch----------"});
        LogHelper.debug({className: "IndexContainer", tag: "error", msg: error, isJson: true});
        LogHelper.debug({className: "IndexContainer", tag: "info", msg: info, isJson: true});
        LogHelper.debug({msg: ""});
    }
}

export default IndexContainer;