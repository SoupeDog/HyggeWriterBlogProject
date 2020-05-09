import LogHelper from "./LogHelper.jsx";
import HttpHelper from "./HttpHelper.jsx";
import URLHelper from "./URLHelper.jsx";
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
        HttpHelper.httpGet({
            headers: _this.getCurrentHeaders(),
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
}