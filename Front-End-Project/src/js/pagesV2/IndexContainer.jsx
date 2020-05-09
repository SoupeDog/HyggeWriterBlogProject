import React from 'react';
import LogHelper from '../utils/LogHelper.jsx';
import clsx from 'clsx';
import '../../css/index.less';

import csdnLogo from '../../img/csdnLogo.png';
import {
    LinkOutlined,
    GithubOutlined,
    QuestionCircleOutlined,
    MenuUnfoldOutlined,
    MenuFoldOutlined,
    CloseCircleOutlined,
    UserOutlined,
    EditOutlined
} from '@ant-design/icons';

import {
    Avatar,
    BackTop,
    Badge,
    Empty,
    Layout,
    Menu,
    message,
    Modal,
    notification,
    Tabs,
    Input,
    Row,
    Col,
    Spin, Typography, Dropdown, Button
} from 'antd';

import URLHelper from "../utils/URLHelper.jsx";
import APICaller from "../utils/APICaller.jsx";
import NotTechnologyBoardView from "../component/NotTechnologyBoardView.jsx";
import TechnologyBoardView from "../component/TechnologyBoardView.jsx";
import MyFooter from "../component/public/MyFooter.jsx";
import LoginAPICaller from "../utils/LoginAPICaller.jsx";
import ArticleAPICaller from "../utils/ArticleAPICaller.jsx";

const {Header, Sider, Content} = Layout;
const {TabPane} = Tabs;
const {Search} = Input;
const {Text, Paragraph} = Typography;


message.config({
    top: 80,
    maxCount: 3
});

notification.config({
    top: 80
});

const userMenu = (
    <Menu onClick={(event) => {
        message.info('Click on menu item —> ' + event.key);
        switch (event.key) {
            case "logout":
                LoginAPICaller.removeLoginInfo();
                URLHelper.openNewPage({finalUrl: URLHelper.getJumpPrefix(), inNewTab: false})
                break;
            case "editor":
                URLHelper.openNewPage({finalUrl: URLHelper.getJumpPrefix()+"editor.html", inNewTab: false})
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
        let initTechnologyCurrentPage = this.props.technologyCurrentPageInit;
        let initTechnologyPageSize = this.props.technologyPageSizeInit;
        let initTechnologyTotalCount = this.props.technologyTotalCountInit;
        let initTechnologySummaryList = this.props.technologySummaryListInit;

        this.state = {
            loadingCountArray: new Array(0),
            loginUserInfo: JSON.parse(localStorage.getItem('userInfo')),
            headerTransparent: false,
            mainMenuCollapsed: false,
            technologyCurrentPage: initTechnologyCurrentPage,
            technologyPageSize: initTechnologyPageSize,
            technologyTotalCount: initTechnologyTotalCount,
            notTechnologyCurrentPage: 1,
            notTechnologyPageSize: 5,
            notTechnologyTotalCount: 0,
            technologySummaryList: initTechnologySummaryList,
            notTechnologySummaryList: []
        };

        LogHelper.info({className: "IndexContainer", msg: "constructor----------"});

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
                    actualCurrentPage = currentPage == null ? this.state.technologyCurrentPage : currentPage;
                    APICaller.querySummaryByPage({
                        boardNo: this.props.technologyBoardNo,
                        currentPage: actualCurrentPage,
                        pageSize: this.state.technologyPageSize,
                        successCallback: function (response) {
                            _react.refreshSummary(activeKey, actualCurrentPage, response.data.totalCount, response.data.resultSet);
                        }
                    });
                    break;
                case "2":
                    // alert("非技术板块" + this.props.notTechnologyBoardNo);
                    actualCurrentPage = currentPage == null ? this.state.notTechnologyCurrentPage : currentPage;
                    APICaller.querySummaryByPage({
                        boardNo: this.props.notTechnologyBoardNo,
                        currentPage: actualCurrentPage,
                        pageSize: this.state.notTechnologyPageSize,
                        successCallback: function (response) {
                            _react.refreshSummary(activeKey, actualCurrentPage, response.data.totalCount, response.data.resultSet);
                        },
                        errorCallback: function (response) {
                            console.log(response);
                        }
                    });
                    break;
                case "3":
                    break
                default:
                    console.log("非预期的 Tabs [activeKey]");
            }
        }.bind(this);

        this.refreshSummary = function (activeKey, actualCurrentPage, totalCount, summaryList) {
            let finalSummaryList;
            if (summaryList == null) {
                finalSummaryList = new Array(0);
            } else {
                finalSummaryList = summaryList;
            }
            // console.log({
            //     activeKey: activeKey,
            //     actualCurrentPage: actualCurrentPage,
            //     totalCount: totalCount,
            //     summaryList: summaryList
            // })
            switch (activeKey) {
                case "1":
                    this.setState({
                        technologyCurrentPage: actualCurrentPage,
                        technologyTotalCount: totalCount,
                        technologySummaryList: finalSummaryList
                    });
                    break;
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
                                    '本站前端基于 React 、Antd、Vditor 开发，后端基于 Spring Boot 全家桶开发，已在我的 Github 个人仓库开源。目标使用场景为 PC ，对手机端提供少数功能，平板将被视为手机端。本站全部音频、图片素材来源于网络，若侵犯了您的权益，请联系 xavierpe@qq.com 以便及时删除侵权素材。',
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
                                    onSearch={value => console.log(value)}/>
                        </div>


                        {/*<Row justify="start" align="top">*/}
                        {/*<Col span={4}>*/}

                        {/*</Col>*/}
                        {/*<Col span={20}>*/}


                        {/*<Row justify="start" align="middle">*/}
                        {/*<Col span={6} offset={4}>*/}

                        {/*</Col>*/}
                        {/*<Col span={8}>*/}

                        {/*/!*</Col>*!/*/}
                        {/*/!*<Col span={2} >*!/*/}
                        {/*{this.state.loadingCountArray.length > 0 ? <Spin/> : null}*/}
                        {/*/!*</Col>*!/*/}
                        {/*/!*</Row>*!/*/}
                        {/*</Col>*/}
                        {/*</Row>*/}
                    </Header>
                    <Content className={clsx({
                        "myContentCollapsed": this.state.mainMenuCollapsed,
                        "myContent": !this.state.mainMenuCollapsed
                    })}>
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

        ArticleAPICaller.queryAllArticleCategoryArticleCount({
            requestBefore: function () {
                _react.addLoadingEvent();
            },
            successCallback: function (response) {
                // console.log(response);
            },
            errorCallback: function (response) {
                message.warn(response.msg);
            },
            finallyCallback: function () {
                _react.removeLoadingEvent();
            }
        });

        // LoginAPICaller.login({
        //     ac: "U00000001", pw: "555555",
        //     successCallback: (response) => {
        //         console.log(response);
        //     }
        // });
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