import React from 'react';
import LogHelper from "../utils/LogHelper.jsx";
import {List} from "antd";
import IconText from "./IconText.jsx";
import ArticleCategoryBreadcrumb from "./ArticleCategoryBreadcrumb.jsx";
import URLHelper from "../utils/URLHelper.jsx";

class NotTechnologyBoardView extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            hasError: false
        };
        this.getBrowseURL = function (articleNo) {
            let finalURL = URLHelper.getJumpPrefix() + "browse.html?articleNo=" + articleNo;
            let currentSecretKey = URLHelper.getQueryString("secretKey");
            if (currentSecretKey != null) {
                currentSecretKey = currentSecretKey.substring(0, 32);
                finalURL = finalURL + "&secretKey=" + currentSecretKey;
            }
            return finalURL;
        }.bind(this);
        LogHelper.info({className: "NotTechnologyBoardView", msg: "constructor----------"});
    }

    static getDerivedStateFromProps(nextProps, nextContext) {
        LogHelper.info({className: "NotTechnologyBoardView", msg: "getDerivedStateFromProps----------"});
        LogHelper.debug({className: "NotTechnologyBoardView", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "NotTechnologyBoardView", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return nextProps;
    }

    render() {
        if (this.state.hasError) {
            return (
                <h1>Something went wrong.</h1>
            );
        } else {
            return (
                <List
                    itemLayout="vertical"
                    size="large"
                    pagination={{
                        onChange: page => {
                            this.props.onTabChange({activeKey: "2", currentPage: page});
                        },
                        showSizeChanger: false,
                        current: this.props.notTechnologyCurrentPage,
                        pageSize: this.props.notTechnologyPageSize,
                        total: this.props.notTechnologyTotalCount
                    }}
                    dataSource={this.props.notTechnologySummaryList}
                    // 这个组件有此处 bug
                    renderItem={summaryItem => (
                        <List.Item
                            key={summaryItem.articleNo}
                            actions={[
                                <IconText type="&#xe61b;" tip={"字数统计"} text={summaryItem.wordCount}
                                          key="wordCount"/>,
                                <IconText type="&#xe640;" tip={"浏览量"} text={summaryItem.pageViews}
                                          key="view"/>,
                                <IconText type="&#xe638;" tip={"创建时间"} text={summaryItem.createTs}
                                          key="createTs" isTimeStamp={true}/>,
                            ]}
                            extra={
                                <img
                                    width={220}
                                    alt="logo"
                                    src={summaryItem.properties.bgi}
                                />
                            }
                        >
                            <List.Item.Meta
                                title={<a target="_blank"
                                          href={this.getBrowseURL(summaryItem.articleNo)}>{summaryItem.title}</a>}
                                // 这个组件有此处 bug
                                description={<ArticleCategoryBreadcrumb articleInfo={summaryItem}/>}
                            />
                            {summaryItem.summary}
                        </List.Item>
                    )}
                />
            );
        }
    }

    shouldComponentUpdate(nextProps, nextState, nextContext) {
        LogHelper.info({className: "NotTechnologyBoardView", msg: "shouldComponentUpdate----------"});
        LogHelper.debug({className: "NotTechnologyBoardView", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "NotTechnologyBoardView", tag: "nextState", msg: nextState, isJson: true});
        LogHelper.debug({className: "NotTechnologyBoardView", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return true;
    }

    componentDidMount() {
        LogHelper.info({className: "NotTechnologyBoardView", msg: "componentDidMount----------"});
        LogHelper.debug({msg: ""});
    }

    getSnapshotBeforeUpdate(prevProps, prevState) {
        LogHelper.info({className: "NotTechnologyBoardView", msg: "getSnapshotBeforeUpdate----------"});
        LogHelper.debug({className: "NotTechnologyBoardView", tag: "prevProps", msg: prevProps, isJson: true});
        LogHelper.debug({className: "NotTechnologyBoardView", tag: "prevState", msg: prevState, isJson: true});
        LogHelper.debug({msg: ""});
        return null;
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        LogHelper.info({className: "NotTechnologyBoardView", msg: "componentDidUpdate----------"});
        LogHelper.debug({className: "NotTechnologyBoardView", tag: "prevProps", msg: prevProps, isJson: true});
        LogHelper.debug({className: "NotTechnologyBoardView", tag: "prevState", msg: prevState, isJson: true});
        LogHelper.debug({className: "NotTechnologyBoardView", tag: "snapshot", msg: snapshot, isJson: true});
        LogHelper.debug({msg: ""});
    }

    static getDerivedStateFromError(error) {
        LogHelper.info({className: "NotTechnologyBoardView", msg: "getDerivedStateFromError----------"});
        LogHelper.debug({className: "NotTechnologyBoardView", tag: "error", msg: error, isJson: true});
        LogHelper.debug({msg: ""});
        return {hasError: true};
    }

    componentDidCatch(error, info) {
        LogHelper.info({className: "NotTechnologyBoardView", msg: "componentDidCatch----------"});
        LogHelper.debug({className: "NotTechnologyBoardView", tag: "error", msg: error, isJson: true});
        LogHelper.debug({className: "NotTechnologyBoardView", tag: "info", msg: info, isJson: true});
        LogHelper.debug({msg: ""});
    }
}

export default NotTechnologyBoardView;
