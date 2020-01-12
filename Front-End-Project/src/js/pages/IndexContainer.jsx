import React from 'react';
import LogHelper from '../utils/LogHelper.jsx';
import clsx from 'clsx';
import 'antd/dist/antd.less';
import '../../css/index.less';

import csdnLogo from '../../img/csdnLogo.png';

import {Avatar, BackTop, Empty, Icon, Layout, List, Menu, message, Modal, notification, Tabs} from 'antd';
import URLHelper from "../utils/URLHelper.jsx";
import APICaller from "../utils/APICaller.jsx";
import NotTechnologyBoardView from "../component/NotTechnologyBoardView.jsx";
import TechnologyBoardView from "../component/TechnologyBoardView.jsx";

const {Header, Sider, Content, Footer} = Layout;
const {TabPane} = Tabs;


message.config({
    top: 80,
    maxCount: 3
});

notification.config({
    top: 80
});

class IndexContainer extends React.Component {

    constructor(props) {
        super(props);
        let initTechnologyCurrentPage = this.props.technologyCurrentPageInit;
        let initTechnologyPageSize = this.props.technologyPageSizeInit;
        let initTechnologyTotalCount = this.props.technologyTotalCountInit;
        let initTechnologySummaryList = this.props.technologySummaryListInit;

        this.state = {
            headerTransparent: false,
            mainMenuCollapsed: true,
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
            console.log({
                activeKey: activeKey,
                actualCurrentPage: actualCurrentPage,
                totalCount: totalCount,
                summaryList: summaryList
            })
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
                       collapsed={this.state.mainMenuCollapsed}>
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
                            {/*<Icon type="edit"/>*/}
                            <span>CSDN(已停更)</span>
                        </Menu.Item>
                        <Menu.Item key="3" onClick={() => {
                            URLHelper.openNewPage({
                                finalUrl: "https://github.com/SoupeDog",
                                inNewTab: true
                            });
                        }}>
                            <Icon type="github" theme="filled"/>
                            <span>GitHub</span>
                        </Menu.Item>
                        <Menu.Item key="4" onClick={() => {
                            message.warn('暂时还没有，有人在期待着一场 PY 交易嘛~', 2);
                        }}>
                            <Icon type="link"/>
                            <span>友链</span>
                        </Menu.Item>
                        <Menu.Item key="5" onClick={() => {
                            notification.info({
                                message: '关于',
                                description:
                                    '本站前端基于 React 、Antd、Vditor 开发，后端基于 Spring Boot 全家桶开发，已在我的 Github 个人仓库开源。目标使用场景为 PC ，对手机端提供少数功能，平板将被视为手机端。本站全部音频、图片素材来源于网络，若侵犯了您的权益，请联系 xavierpe@qq.com 以便及时删除侵权素材。',
                            });
                        }}>
                            <Icon type="question"/>
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
                        <Icon
                            className="trigger"
                            type={this.state.mainMenuCollapsed ? 'menu-unfold' : 'menu-fold'}
                            onClick={this.mainMenuToggle}
                        />
                    </Header>
                    <Content className={clsx({
                        "myContentCollapsed": this.state.mainMenuCollapsed,
                        "myContent": !this.state.mainMenuCollapsed
                    })}>
                        <Tabs defaultActiveKey="1" onChange={(activeKey) => {
                            this.onTabChange({activeKey: activeKey});
                        }}>
                            <TabPane
                                tab={
                                    <span>
                                      技术
                                    </span>
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
                                    <span>
                                      非技术
                                    </span>
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
                                    <span>
                                      搜索
                                    </span>
                                }
                                key="3"
                            >
                                <Empty description={"搜索功能未启用"}/>
                            </TabPane>
                        </Tabs>
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