import '../css/default.css';
// import "vditor/src/assets/scss/classic.scss"// 或者使用 dark

import React from 'react';
import ReactDOM from 'react-dom';
import 'antd/dist/antd.less';
import EditorContainer from "./pages/EditorContainer.jsx";

// APICaller.querySummaryByPage({
//     boardNo: "b9cc9df574b448b088303f8056198d91",
//     currentPage: 1,
//     pageSize: 10,
//     successCallback: function (response) {
//         alert(1)
//     },
//     errorCallback:function (response) {
//         alert(response)
//     }
// });

ReactDOM.render(<EditorContainer
    // 技术板块
    technologyBoardNo={"ae81801808f8485384b66da526169da2"}
    // 非技术板块
    notTechnologyBoardNo={"b9cc9df574b448b088303f8056198d91"}/>, document.getElementById('root'));