import React from 'react';
import LogHelper from '../utils/LogHelper.jsx';
import clsx from 'clsx';
import 'antd/dist/antd.less';
import '../../css/index.less';

import csdnLogo from '../../img/csdnLogo.png';

import {Avatar, BackTop, Empty, Icon, Layout, List, Menu, message, notification, Tabs} from 'antd';
import URLHelper from "../utils/URLHelper.jsx";
import IconText from "../component/IconText.jsx";
import ArticleCategoryBreadcrumb from "../component/ArticleCategoryBreadcrumb.jsx";
import APICaller from "../utils/APICaller.jsx";

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
        this.state = {
            headerTransparent: false,
            mainMenuCollapsed: true,
            currentPage: 1,
            pageSize: 2,
            totalCount: 0,
            technologySummaryList: [],
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
           let actualCurrentPage= currentPage == null ? this.state.currentPage : currentPage;
            let _react = this;
            switch (activeKey) {
                case "1":
                    alert("技术板块" + this.props.technologyBoardNo);
                    APICaller.querySummaryByPage({
                        boardNo: this.props.technologyBoardNo,
                        currentPage: actualCurrentPage,
                        pageSize: this.state.pageSize,
                        successCallback: function (response) {
                            _react.refreshSummary(activeKey, response.data.totalCount, response.data.resultSet);
                        }
                    });
                    break;
                case "2":
                    alert("非技术板块" + this.props.notTechnologyBoardNo);
                    APICaller.querySummaryByPage({
                        boardNo: this.props.notTechnologyBoardNo,
                        currentPage: actualCurrentPage,
                        pageSize: this.state.pageSize,
                        successCallback: function (response) {
                            _react.refreshSummary(activeKey, response.data.totalCount, response.data.resultSet);
                        }
                    });
                    break;
                case "3":
                    break
                default:
                    console.log("非预期的 Tabs [activeKey]");
            }
        }.bind(this);

        this.refreshSummary = function (activeKey, totalCount, summaryList) {
            let finalSummaryList;
            if (summaryList == null) {
                finalSummaryList = new Array(0);
            } else {
                finalSummaryList = summaryList;
            }
            switch (activeKey) {
                case "1":
                    this.setState({
                        totalCount: totalCount,
                        technologySummaryList: finalSummaryList
                    });
                    break;
                case "2":
                    this.setState({
                        totalCount: totalCount,
                        notTechnologySummaryList: finalSummaryList
                    });
                    console.log(finalSummaryList)
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
                                        src="https://www.xavierwang.cn/images/47454e58e7f249c3968524d25a6c9c7a_M.png"/>
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
                                <List
                                    itemLayout="vertical"
                                    size="large"
                                    pagination={{
                                        onChange: page => {
                                            this.setState({
                                                currentPage: page
                                            });
                                        },
                                        current: this.state.currentPage,
                                        pageSize: this.state.pageSize,
                                        total: this.state.totalCount
                                    }}
                                    dataSource={this.state.technologySummaryList}
                                    renderItem={summaryItem => (
                                        <List.Item
                                            key={summaryItem.articleNo}
                                            actions={[
                                                <IconText type="&#xe640;" tip={"浏览量"} text={summaryItem.pageViews}
                                                          key="view"/>,
                                                <IconText type="&#xe61b;" tip={"字数统计"} text={summaryItem.wordCount}
                                                          key="wordCount"/>,
                                                <IconText type="&#xe638;" tip={"创建时间"} text={summaryItem.createTs}
                                                          key="createTs" isTimeStamp={true}/>,
                                            ]}
                                            extra={
                                                <img
                                                    width={300}
                                                    alt="logo"
                                                    src={summaryItem.properties.bgi}
                                                />
                                            }
                                        >
                                            <List.Item.Meta
                                                title={<a href={summaryItem.articleCategoryNo}>{summaryItem.title}</a>}
                                                description={<ArticleCategoryBreadcrumb articleInfo={summaryItem}/>}
                                            />
                                            {summaryItem.summary}
                                        </List.Item>
                                    )}
                                />
                            </TabPane>
                            <TabPane
                                tab={
                                    <span>
                                      非技术
                                    </span>
                                }
                                key="2"
                            >
                                <List
                                    itemLayout="vertical"
                                    size="large"
                                    pagination={{
                                        onChange: page => {
                                            this.setState({
                                                currentPage: page
                                            });
                                            this.onTabChange({activeKey: "2", currentPage: page});
                                        },
                                        current: this.state.currentPage,
                                        pageSize: this.state.pageSize,
                                        total: this.state.totalCount
                                    }}
                                    dataSource={this.state.notTechnologySummaryList}
                                    renderItem={summaryItem => (
                                        <List.Item
                                            key={summaryItem.articleNo}
                                            actions={summaryItem == null ? [] : [
                                                <IconText type="&#xe640;" tip={"浏览量"} text={summaryItem.pageViews}
                                                          key="view"/>,
                                                <IconText type="&#xe61b;" tip={"字数统计"} text={summaryItem.wordCount}
                                                          key="wordCount"/>,
                                                <IconText type="&#xe638;" tip={"创建时间"} text={summaryItem.createTs}
                                                          key="createTs" isTimeStamp={true}/>,
                                            ]}
                                            extra={
                                                <img
                                                    width={300}
                                                    alt="logo"
                                                    src={summaryItem.properties.bgi}
                                                />
                                            }
                                        >
                                            <List.Item.Meta
                                                title={<a href={summaryItem.articleCategoryNo}>{summaryItem.title}</a>}
                                                description={<ArticleCategoryBreadcrumb articleInfo={summaryItem}/>}
                                            />
                                            {summaryItem.summary}
                                        </List.Item>
                                    )}
                                />
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
                                <img src="https://www.xavierwang.cn/images/icon-police.png"/>
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
        let _react = this;
        LogHelper.info({className: "IndexContainer", msg: "componentDidMount----------"});
        // const listData = [];
        // for (let i = 0; i < 23; i++) {
        //     listData.push({
        //         href: 'http://ant.design',
        //         title: `ant design part ${i}`,
        //         avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
        //         description:
        //             'Ant Design, a design language for background applications, is refined by Ant UED Team.',
        //         content:
        //             'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.',
        //     });
        // }
        // this.refreshSummary("1", [
        //     {
        //         "articleNo": "b6482323dcd74708a1b06f5e50e43321",
        //         "boardNo": "b9cc9df574b448b088303f8056198d91",
        //         "boardName": "非技术",
        //         "articleCategoryNo": "5635bc7cb8fc4611bb1c4dcaf1e509c0",
        //         "articleCategoryName": "随笔",
        //         "parentArticleCategoryList": null,
        //         "title": "测试标题2",
        //         "uid": "U00000003",
        //         "summary": "摘要",
        //         "wordCount": 2,
        //         "pageViews": 2,
        //         "properties": {
        //             "bgi": "https://s1.ax2x.com/2018/10/24/5XWiJq.jpg",
        //             "bgmConfig": {
        //                 "bgmType": null,
        //                 "src": null
        //             }
        //         },
        //         "state": "ACTIVE",
        //         "createTs": 1578234180391,
        //         "lastUpdateTs": 1578234180391
        //     },
        //     {
        //         "articleNo": "b6bc50d6a0114e8c8e98f2922a0b5402",
        //         "boardNo": "b9cc9df574b448b088303f8056198d91",
        //         "boardName": "非技术",
        //         "articleCategoryNo": "99ec540b01914a70a2be3f400bc9197a",
        //         "articleCategoryName": "Beholder",
        //         "parentArticleCategoryList": [
        //             {
        //                 "articleCategoryNo": "123456",
        //                 "articleCategoryName": "305那帮缩男"
        //             }
        //         ],
        //         "title": "测试标题改",
        //         "uid": "U00000003",
        //         "summary": "摘要改",
        //         "wordCount": 2,
        //         "pageViews": 59,
        //         "properties": {
        //             "bgi": "123",
        //             "bgmConfig": {
        //                 "bgmType": 1,
        //                 "src": "456"
        //             }
        //         },
        //         "state": "ACTIVE",
        //         "createTs": 1578234099601,
        //         "lastUpdateTs": 1578572855006
        //     }
        // ]);
        // WindowsEventHelper.addCallback_Scroll({
        //     name: "APPBar 透明判定", delta: 50, callbackFunction: function ({currentScrollY}) {
        //         if (currentScrollY > 270) {
        //             _react.setState({headerTransparent: false});
        //         } else {
        //             _react.setState({headerTransparent: true});
        //         }
        //     }
        // });
        // WindowsEventHelper.start_OnScroll();
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