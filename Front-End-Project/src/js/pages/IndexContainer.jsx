import React from 'react';
import LogHelper from '../utils/LogHelper.jsx';
import clsx from 'clsx';
import 'antd/dist/antd.less';
import '../../css/index.less';

import csdnLogo from '../../img/csdnLogo.png';

import {Layout, Menu, Icon, Avatar, BackTop,message, notification} from 'antd';
import URLHelper from "../utils/URLHelper.jsx";
import WindowsEventHelper from "../utils/WindowsEventHelper.jsx";

const {Header, Sider, Content} = Layout;

class IndexContainer extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            headerTransparent: true,
            mainMenuCollapsed: true
        };
        LogHelper.info({className: "IndexContainer", msg: "constructor----------"});

        this.mainMenuToggle = function () {
            this.setState({
                mainMenuCollapsed: !this.state.mainMenuCollapsed
            });
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
                        Content
                    </Content>
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
        WindowsEventHelper.addCallback_Scroll({
            name: "APPBar 透明判定", delta: 50, callbackFunction: function ({currentScrollY}) {
                if (currentScrollY > 270) {
                    _react.setState({headerTransparent: false});
                } else {
                    _react.setState({headerTransparent: true});
                }
            }
        });
        WindowsEventHelper.start_OnScroll();
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        LogHelper.info({className: "IndexContainer", msg: "componentDidUpdate----------"});
        LogHelper.debug({className: "IndexContainer", tag: "prevProps", msg: prevProps, isJson: true});
        LogHelper.debug({className: "IndexContainer", tag: "prevState", msg: prevState, isJson: true});
        LogHelper.debug({className: "IndexContainer", tag: "snapshot", msg: snapshot, isJson: true});
        LogHelper.debug({msg: ""});
    }

    componentWillUnmount() {
        LogHelper.info({className: "IndexContainer", msg: "componentWillUnmount----------"});
    }
}

export default IndexContainer;