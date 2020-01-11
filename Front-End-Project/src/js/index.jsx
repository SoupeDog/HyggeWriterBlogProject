import '../css/default.css';
// import "vditor/src/assets/scss/classic.scss"// 或者使用 dark

import React from 'react';
import ReactDOM from 'react-dom';
import IndexContainer from "./pages/IndexContainer.jsx";
import APICaller from "./utils/APICaller.jsx";

// APICaller.querySummaryByPage({
//     boardNo: "b9cc9df574b448b088303f8056198d91",
//     currentPage: 1,
//     pageSize: 10,
//     successCallback: function (response) {
//         console.log(response);
//     }
// });

ReactDOM.render(<IndexContainer
    // 技术板块
    technologyBoardNo={"ae81801808f8485384b66da526169da2"}
    // 非技术板块
    notTechnologyBoardNo={"b9cc9df574b448b088303f8056198d91"}/>, document.getElementById('root'));