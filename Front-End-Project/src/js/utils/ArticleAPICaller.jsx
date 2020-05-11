import LogHelper from "./LogHelper.jsx";
import HttpHelper from "./HttpHelper.jsx";
import LoginAPICaller from "./LoginAPICaller.jsx";


export default class ArticleAPICaller extends LoginAPICaller {

    // 查询全部板块下文章数量
    static queryArticleCategoryArticleCountOfBoard({requestBefore, successCallback, errorCallback, timeOutCallback, finallyCallback}) {
        let _this = this;
        HttpHelper.httpGet({
            headers: _this.getCurrentHeaders(),
            path: "blog-service/main/index/article/count/board",
            requestBefore: requestBefore,
            successCallback: function (response) {
                _this.defaultResponseCallback({
                    response: response,
                    successCallback: successCallback,
                    errorCallback: errorCallback
                });
            },
            errorCallback: errorCallback,
            timeOutCallback: timeOutCallback,
            finallyCallback: finallyCallback
        });
    }

    // 查询全部文章类别下文章数量
    static queryAllArticleCategoryArticleCount({requestBefore, successCallback, errorCallback, timeOutCallback, finallyCallback}) {
        let _this = this;
        let currentHeaders = _this.getCurrentHeaders();
        HttpHelper.httpGet({
            headers: currentHeaders,
            path: "blog-service/main/index/article/count/articleCategory",
            requestBefore: requestBefore,
            successCallback: function (response) {
                _this.defaultResponseCallback({
                    response: response,
                    successCallback: successCallback,
                    errorCallback: errorCallback
                });
            },
            errorCallback: errorCallback,
            timeOutCallback: timeOutCallback,
            finallyCallback: finallyCallback
        });
    }

    // 根据板块查询文章摘要
    static querySummaryByPageOfBoard({boardNo, currentPage, pageSize, requestBefore, successCallback, errorCallback, timeOutCallback, finallyCallback}) {
        let _this = this;
        let currentHeaders = _this.getCurrentHeaders();
        HttpHelper.httpGet({
            headers: currentHeaders,
            path: "blog-service/main/index/article?boardNo=" + boardNo + "&currentPage=" + currentPage + "&pageSize=" + pageSize,
            requestBefore: requestBefore,
            successCallback: function (response) {
                _this.defaultResponseCallback({
                    response: response,
                    successCallback: successCallback,
                    errorCallback: errorCallback
                });
            },
            errorCallback: errorCallback,
            timeOutCallback: timeOutCallback,
            finallyCallback: finallyCallback
        });
    }

    // 根据文章类别查询文章摘要
    static querySummaryByPageOfArticleCategory({articleCategoryNo, currentPage, pageSize, requestBefore, successCallback, errorCallback, timeOutCallback, finallyCallback}) {
        let _this = this;
        let currentHeaders = _this.getCurrentHeaders();
        HttpHelper.httpGet({
            headers: currentHeaders,
            path: "blog-service/main/index/article?articleCategoryNo=" + articleCategoryNo + "&currentPage=" + currentPage + "&pageSize=" + pageSize,
            requestBefore: requestBefore,
            successCallback: function (response) {
                _this.defaultResponseCallback({
                    response: response,
                    successCallback: successCallback,
                    errorCallback: errorCallback
                });
            },
            errorCallback: errorCallback,
            timeOutCallback: timeOutCallback,
            finallyCallback: finallyCallback
        });
    }

    // 全文检索文章摘要
    static querySummaryByPageOfSearch({keyword, currentPage, pageSize, requestBefore, successCallback, errorCallback, timeOutCallback, finallyCallback}) {
        let _this = this;
        let currentHeaders = _this.getCurrentHeaders();
        HttpHelper.httpGet({
            headers: currentHeaders,
            path: "blog-service/main/index/article/search?keyword=" + keyword + "&currentPage=" + currentPage + "&pageSize=" + pageSize,
            requestBefore: requestBefore,
            successCallback: function (response) {
                _this.defaultResponseCallback({
                    response: response,
                    successCallback: successCallback,
                    errorCallback: errorCallback
                });
            },
            errorCallback: errorCallback,
            timeOutCallback: timeOutCallback,
            finallyCallback: finallyCallback
        });
    }

    // 查询特定文章全文
    static queryArticleByArticleNo({articleNo,  requestBefore, successCallback, errorCallback, timeOutCallback, finallyCallback}) {
        let _this = this;
        let currentHeaders = _this.getCurrentHeaders();
        HttpHelper.httpGet({
            headers: currentHeaders,
            path: "blog-service/main/article/" + articleNo,
            requestBefore: requestBefore,
            successCallback: function (response) {
                _this.defaultResponseCallback({
                    response: response,
                    successCallback: successCallback,
                    errorCallback: errorCallback
                });
            },
            errorCallback: errorCallback,
            timeOutCallback: timeOutCallback,
            finallyCallback: finallyCallback
        });
    }

}