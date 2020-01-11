import React from 'react';
import LogHelper from "../utils/LogHelper.jsx";
import {Breadcrumb} from "antd";

class ArticleCategoryBreadcrumb extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            hasError: false
        };
        LogHelper.info({className: "ArticleCategoryBreadcrumb", msg: "constructor----------"});
    }

    static getDerivedStateFromProps(nextProps, nextContext) {
        LogHelper.info({className: "ArticleCategoryBreadcrumb", msg: "getDerivedStateFromProps----------"});
        LogHelper.debug({className: "ArticleCategoryBreadcrumb", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "ArticleCategoryBreadcrumb", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return nextProps;
    }

    render() {
        if (this.state.hasError) {
            return <h1>Something went wrong.</h1>;
        } else {
            if (this.props.articleInfo.parentArticleCategoryList == null) {
                return (
                    <Breadcrumb>
                        <Breadcrumb.Item>{this.props.articleInfo.boardName}</Breadcrumb.Item>
                        <Breadcrumb.Item>
                            <span>{this.props.articleInfo.articleCategoryName}</span>
                        </Breadcrumb.Item>
                    </Breadcrumb>
                );
            } else {
                return (
                    <Breadcrumb>
                        <Breadcrumb.Item>{this.props.articleInfo.boardName}</Breadcrumb.Item>
                        {
                            this.props.articleInfo.parentArticleCategoryList.map((articleCategoryInfo, index) => {
                                return (
                                    <Breadcrumb.Item key={index}>
                                        <span>{articleCategoryInfo.articleCategoryName}</span>
                                    </Breadcrumb.Item>
                                )
                            })
                        }
                        <Breadcrumb.Item>
                            <span>{this.props.articleInfo.articleCategoryName}</span>
                        </Breadcrumb.Item>
                    </Breadcrumb>
                );
            }
        }
    }

    shouldComponentUpdate(nextProps, nextState, nextContext) {
        LogHelper.info({className: "ArticleCategoryBreadcrumb", msg: "shouldComponentUpdate----------"});
        LogHelper.debug({className: "ArticleCategoryBreadcrumb", tag: "nextProps", msg: nextProps, isJson: true});
        LogHelper.debug({className: "ArticleCategoryBreadcrumb", tag: "nextState", msg: nextState, isJson: true});
        LogHelper.debug({className: "ArticleCategoryBreadcrumb", tag: "nextContext", msg: nextContext, isJson: true});
        LogHelper.debug({msg: ""});
        return true;
    }

    componentDidMount() {
        LogHelper.info({className: "ArticleCategoryBreadcrumb", msg: "componentDidMount----------"});
        LogHelper.debug({msg: ""});
    }

    getSnapshotBeforeUpdate(prevProps, prevState) {
        LogHelper.info({className: "ArticleCategoryBreadcrumb", msg: "getSnapshotBeforeUpdate----------"});
        LogHelper.debug({className: "ArticleCategoryBreadcrumb", tag: "prevProps", msg: prevProps, isJson: true});
        LogHelper.debug({className: "ArticleCategoryBreadcrumb", tag: "prevState", msg: prevState, isJson: true});
        LogHelper.debug({msg: ""});
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        LogHelper.info({className: "ArticleCategoryBreadcrumb", msg: "componentDidUpdate----------"});
        LogHelper.debug({className: "ArticleCategoryBreadcrumb", tag: "prevProps", msg: prevProps, isJson: true});
        LogHelper.debug({className: "ArticleCategoryBreadcrumb", tag: "prevState", msg: prevState, isJson: true});
        LogHelper.debug({className: "ArticleCategoryBreadcrumb", tag: "snapshot", msg: snapshot, isJson: true});
        LogHelper.debug({msg: ""});
    }

    static getDerivedStateFromError(error) {
        LogHelper.info({className: "ArticleCategoryBreadcrumb", msg: "getDerivedStateFromError----------"});
        LogHelper.debug({className: "ArticleCategoryBreadcrumb", tag: "error", msg: error, isJson: true});
        LogHelper.debug({msg: ""});
        return {hasError: true};
    }

    componentDidCatch(error, info) {
        LogHelper.info({className: "ArticleCategoryBreadcrumb", msg: "componentDidCatch----------"});
        LogHelper.debug({className: "ArticleCategoryBreadcrumb", tag: "error", msg: error, isJson: true});
        LogHelper.debug({className: "ArticleCategoryBreadcrumb", tag: "info", msg: info, isJson: true});
        LogHelper.debug({msg: ""});
    }
}

export default ArticleCategoryBreadcrumb;
