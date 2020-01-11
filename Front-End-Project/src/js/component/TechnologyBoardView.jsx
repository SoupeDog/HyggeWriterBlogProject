import React from 'react';
import LogHelper from "../utils/LogHelper.jsx";
import {List} from "antd";
import IconText from "./IconText.jsx";
import ArticleCategoryBreadcrumb from "./ArticleCategoryBreadcrumb.jsx";

class TechnologyBoardView extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            hasError: false
        };
        LogHelper.info({className: "TechnologyBoardView", msg: "constructor----------"});
    }

    static getDerivedStateFromProps(nextProps, nextContext) {
        LogHelper.info({className: "TechnologyBoardView", msg: "getDerivedStateFromProps----------"});
        LogHelper.debug({className: "TechnologyBoardView", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "TechnologyBoardView", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return nextProps;
    }

    render() {
        if (this.state.hasError) {
            return <h1>Something went wrong.</h1>;
        } else {
            return (
                <List
                    itemLayout="vertical"
                    size="large"
                    pagination={{
                        onChange: page => {
                            this.props.onTabChange({activeKey: "1", currentPage: page});
                        },
                        current: this.props.technologyCurrentPage,
                        pageSize: this.props.technologyPageSize,
                        total: this.props.technologyTotalCount
                    }}
                    dataSource={this.props.technologySummaryList}
                    // 这个组件有此处 bug
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
        LogHelper.info({className: "TechnologyBoardView", msg: "shouldComponentUpdate----------"});
        LogHelper.debug({className: "TechnologyBoardView", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "TechnologyBoardView", tag: "nextState", msg: nextState, isJson: true});
        LogHelper.debug({className: "TechnologyBoardView", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return true;
    }

    componentDidMount() {
        LogHelper.info({className: "TechnologyBoardView", msg: "componentDidMount----------"});
        LogHelper.debug({msg: ""});
    }

    getSnapshotBeforeUpdate(prevProps, prevState) {
        LogHelper.info({className: "TechnologyBoardView", msg: "getSnapshotBeforeUpdate----------"});
        LogHelper.debug({className: "TechnologyBoardView", tag: "prevProps", msg: prevProps, isJson: true});
        LogHelper.debug({className: "TechnologyBoardView", tag: "prevState", msg: prevState, isJson: true});
        LogHelper.debug({msg: ""});
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        LogHelper.info({className: "TechnologyBoardView", msg: "componentDidUpdate----------"});
        LogHelper.debug({className: "TechnologyBoardView", tag: "prevProps", msg: prevProps, isJson: true});
        LogHelper.debug({className: "TechnologyBoardView", tag: "prevState", msg: prevState, isJson: true});
        LogHelper.debug({className: "TechnologyBoardView", tag: "snapshot", msg: snapshot, isJson: true});
        LogHelper.debug({msg: ""});
    }

    static getDerivedStateFromError(error) {
        LogHelper.info({className: "TechnologyBoardView", msg: "getDerivedStateFromError----------"});
        LogHelper.debug({className: "TechnologyBoardView", tag: "error", msg: error, isJson: true});
        LogHelper.debug({msg: ""});
        return {hasError: true};
    }

    componentDidCatch(error, info) {
        LogHelper.info({className: "TechnologyBoardView", msg: "componentDidCatch----------"});
        LogHelper.debug({className: "TechnologyBoardView", tag: "error", msg: error, isJson: true});
        LogHelper.debug({className: "TechnologyBoardView", tag: "info", msg: info, isJson: true});
        LogHelper.debug({msg: ""});
    }
}

export default TechnologyBoardView;
