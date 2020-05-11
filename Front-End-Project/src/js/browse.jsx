import '../css/default.css';
import React from 'react';
import ReactDOM from 'react-dom';

import {message} from 'antd';

message.config({
    top: 80,
    maxCount: 3
});

import URLHelper from "./utils/URLHelper.jsx";

import BrowseContainer from "./pages/BrowseContainer.jsx";
import ArticleAPICaller from "./utils/ArticleAPICaller.jsx";

let currentArticleNo = URLHelper.getQueryString("articleNo");
if (currentArticleNo != null) {
    ArticleAPICaller.queryArticleByArticleNo({
        articleNo: currentArticleNo,
        successCallback: function (response) {
            ReactDOM.render(<BrowseContainer
                article={response.data}/>, document.getElementById('root'));
        },
        errorCallback: function (response) {
            message.warn(response.msg, 1);
        }
    });
} else {
    message.warn('未输入目标文章编号', 1);
}