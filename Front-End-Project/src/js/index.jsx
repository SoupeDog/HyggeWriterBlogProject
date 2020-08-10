import '../css/default.css';
import React from 'react';
import ReactDOM from 'react-dom';
import IndexContainer from "./pages/IndexContainer.jsx";
import ArticleAPICaller from "./utils/ArticleAPICaller.jsx";
import {message} from 'antd';

message.config({
    top: 80,
    maxCount: 3
});

let currentPageInit = 1;
let pageSizeInit = 10;
// 技术板块
let technologyBoardNo = "ae81801808f8485384b66da526169da2";
// 非技术板块
let notTechnologyBoardNo = "b9cc9df574b448b088303f8056198d91";
// 全部板块数量信息
let articleCountInfoOfBoardList = null;

if (sessionStorage.getItem("loadFlag") == null) {
    sessionStorage.setItem("loadFlag", "true");
    message.info('依赖加载完成', 1);
}

ArticleAPICaller.queryAllArticleCategoryArticleCount({
    successCallback: function (response) {
        articleCountInfoOfBoardList = response.data;
        ArticleAPICaller.querySummaryByPageOfBoard({
            boardNo: technologyBoardNo,
            currentPage: currentPageInit,
            pageSize: pageSizeInit,
            successCallback: function (response2) {
                ReactDOM.render(<IndexContainer
                    currentPageInit={currentPageInit}
                    pageSizeInit={pageSizeInit}
                    technologyTotalCountInit={response2.data.totalCount}
                    technologySummaryListInit={response2.data.resultSet}
                    // 技术板块
                    technologyBoardNo={technologyBoardNo}
                    // 非技术板块
                    notTechnologyBoardNo={notTechnologyBoardNo}
                    articleCountInfoOfBoardList={articleCountInfoOfBoardList}/>, document.getElementById('root'));

            },
            errorCallback: function (response2) {
                message.warn(response2.msg, 2)
            }
        });
    },
    errorCallback: function (response) {
        message.warn(response.msg);
    }
});