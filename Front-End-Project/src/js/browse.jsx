import '../css/default.css';

// import "vditor/src/assets/scss/dark.scss"// 或者使用 dark

import React from 'react';
import ReactDOM from 'react-dom';
import BrowseContainer from "./pages/BrowseContainer.jsx";
import APICaller from "./utils/APICaller.jsx";
import URLHelper from "./utils/URLHelper.jsx";

let currentArticleNo = URLHelper.getQueryString("articleNo");
if (currentArticleNo != null) {
    APICaller.queryArticle({
        articleNo: currentArticleNo,
        successCallback: function (response) {
            console.log(response);
            if (response.code == 200) {
                ReactDOM.render(<BrowseContainer
                    article={response.data}/>, document.getElementById('root'));
            } else {
                alert("请求失败")
            }
        },
        errorCallback: function (response) {
            console.log(response);
            alert("Error")
        }
    });
}else {
    alert("未输入目标文章编号");
}


