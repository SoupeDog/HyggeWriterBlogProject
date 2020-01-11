

import '../css/default.css';

// import "vditor/src/assets/scss/dark.scss"// 或者使用 dark

import React from 'react';
import ReactDOM from 'react-dom';
import BrowseContainer from "./pages/BrowseContainer.jsx";
import APICaller from "./utils/APICaller.jsx";


APICaller.queryArticle({
    articleNo: "b6bc50d6a0114e8c8e98f2922a0b5402",
    successCallback: function (response) {
        console.log(response);
        if (response.code == 200) {
            ReactDOM.render(<BrowseContainer
                article={response.data}/>, document.getElementById('root'));
        }else {
            alert("请求失败")
        }
    },
    errorCallback:function (response) {
        console.log(response);
        alert("Error")
    }
});

