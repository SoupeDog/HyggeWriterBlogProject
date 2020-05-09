import React from 'react';
import LogHelper from "../utils/LogHelper.jsx";
import {List} from "antd";
import IconText from "./IconText.jsx";
import ArticleCategoryBreadcrumb from "./ArticleCategoryBreadcrumb.jsx";
import URLHelper from "../utils/URLHelper.jsx";

class SearchView extends React.Component {

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
        LogHelper.info({className: "SearchView", msg: "constructor----------"});
    }

    static getDerivedStateFromProps(nextProps, nextContext) {
        LogHelper.info({className: "SearchView", msg: "getDerivedStateFromProps----------"});
        LogHelper.debug({className: "SearchView", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "SearchView", tag: "nextContext", msg: nextContext, isJson: true});
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
                            this.props.refreshSummaryOfSearch({
                                searchType: this.props.searchType,
                                searchProperties: this.props.searchProperties,
                                currentPage: page
                            });
                        },
                        current: this.props.searchCurrentPage,
                        pageSize: this.props.searchPageSize,
                        total: this.props.searchTotalCount
                    }}
                    dataSource={this.props.searchSummaryList}
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
                                    width={300}
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
        LogHelper.info({className: "SearchView", msg: "shouldComponentUpdate----------"});
        LogHelper.debug({className: "SearchView", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "SearchView", tag: "nextState", msg: nextState, isJson: true});
        LogHelper.debug({className: "SearchView", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return true;
    }

    componentDidMount() {
        LogHelper.info({className: "SearchView", msg: "componentDidMount----------"});
        LogHelper.debug({msg: ""});
    }

    getSnapshotBeforeUpdate(prevProps, prevState) {
        LogHelper.info({className: "SearchView", msg: "getSnapshotBeforeUpdate----------"});
        LogHelper.debug({className: "SearchView", tag: "prevProps", msg: prevProps, isJson: true});
        LogHelper.debug({className: "SearchView", tag: "prevState", msg: prevState, isJson: true});
        LogHelper.debug({msg: ""});
        return null;
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        LogHelper.info({className: "SearchView", msg: "componentDidUpdate----------"});
        LogHelper.debug({className: "SearchView", tag: "prevProps", msg: prevProps, isJson: true});
        LogHelper.debug({className: "SearchView", tag: "prevState", msg: prevState, isJson: true});
        LogHelper.debug({className: "SearchView", tag: "snapshot", msg: snapshot, isJson: true});
        LogHelper.debug({msg: ""});
    }

    static getDerivedStateFromError(error) {
        LogHelper.info({className: "SearchView", msg: "getDerivedStateFromError----------"});
        LogHelper.debug({className: "SearchView", tag: "error", msg: error, isJson: true});
        LogHelper.debug({msg: ""});
        return {hasError: true};
    }

    componentDidCatch(error, info) {
        LogHelper.info({className: "SearchView", msg: "componentDidCatch----------"});
        LogHelper.debug({className: "SearchView", tag: "error", msg: error, isJson: true});
        LogHelper.debug({className: "SearchView", tag: "info", msg: info, isJson: true});
        LogHelper.debug({msg: ""});
    }
}

export default SearchView;
