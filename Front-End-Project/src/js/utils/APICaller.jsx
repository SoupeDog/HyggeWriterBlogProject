import LogHelper from "./LogHelper.jsx";
import HttpHelper from "./HttpHelper.jsx";
import URLHelper from "./URLHelper.jsx";


export default class APICaller {
    // 获取 headers
    static getCurrentHeaders() {
        let currentSecretKey = URLHelper.getQueryString("secretKey");
        let currentHeaders = null;
        if (currentSecretKey != null) {
            currentHeaders = {
                secretKey: URLHelper.getQueryString("secretKey")
            };
        }
        let currentUId = localStorage.getItem("uid");
        let currentToken = localStorage.getItem("token");
        if (currentUId == null || currentToken == null || currentRefreshKey == null) {
            localStorage.removeItem('uid');
            localStorage.removeItem('token');
            localStorage.removeItem('refreshKey');
            // 令牌信息有误
            // URLHelper.openNewPage({finalUrl: URLHelper.getJumpPrefix() + "login.html", inNewTab: false});
        } else {
            currentHeaders = {
                uId: currentUId,
                token: currentToken,
                refreshKey: currentRefreshKey,
                secretKey: URLHelper.getQueryString("secretKey")
            };
        }
        return currentHeaders;
    }

    static querySummaryByPage({boardNo, currentPage, pageSize, successCallback, errorCallback, timeOutCallback, finallyCallback}) {
        HttpHelper.httpGet({
            headers: APICaller.getCurrentHeaders(),
            path: "blog-service/main/article/summary/" + boardNo + "?currentPage=" + currentPage + "&pageSize=" + pageSize,
            successCallback: function (response) {
                successCallback(response);
            },
            errorCallback: errorCallback,
            timeOutCallback: timeOutCallback,
            finallyCallback: finallyCallback
        });
    }

}