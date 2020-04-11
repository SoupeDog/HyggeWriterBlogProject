import '../css/default.css';
import React from 'react';
import ReactDOM from 'react-dom';
import IndexContainer from "./pages/IndexContainer.jsx";
import APICaller from "./utils/APICaller.jsx";

APICaller.querySummaryByPage({
    boardNo: "ae81801808f8485384b66da526169da2",
    currentPage: 1,
    pageSize: 5,
    successCallback: function (response) {
        if (response.code == 200) {
            ReactDOM.render(<IndexContainer
                technologyCurrentPageInit={1}
                technologyPageSizeInit={5}
                technologyTotalCountInit={response.data.totalCount}
                technologySummaryListInit={response.data.resultSet}
                // 技术板块
                technologyBoardNo={"ae81801808f8485384b66da526169da2"}
                // 非技术板块
                notTechnologyBoardNo={"b9cc9df574b448b088303f8056198d91"}/>, document.getElementById('root'));
        } else {
            alert(JSON.stringify(response));
        }
    },
    errorCallback: function (response) {
        alert(JSON.stringify(response));
    }
});