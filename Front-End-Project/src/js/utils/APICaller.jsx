import LogHelper from "./LogHelper.jsx";
import HttpHelper from "./HttpHelper.jsx";
import URLHelper from "./URLHelper.jsx";


export default class APICaller {
    // 获取 headers
    static getCurrentHeaders() {
        let currentSecretKey = URLHelper.getQueryString("secretKey");
        if (currentSecretKey != null) {
            currentSecretKey = currentSecretKey.substring(0, 32);
        }
        let currentHeaders = null;
        if (currentSecretKey != null) {
            currentHeaders = {
                secretKey: URLHelper.getQueryString("secretKey")
            };
        }
        let currentUId = localStorage.getItem("uid");
        let currentToken = localStorage.getItem("token");
        let currentRefreshKey = localStorage.getItem("refreshKey");
        ;
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

    static defaultSuccessCallback(response) {
        if (response != null &&
            (response.code == 403001 ||
                response.code == 403002 ||
                response.code == 403003)) {
            // 令牌信息有误
            URLHelper.openNewPage({finalUrl: URLHelper.getJumpPrefix() + "/login.html", inNewTab: false});
        }
    }

    static lgoin({ac, pw, successCallback, errorCallback, timeOutCallback, finallyCallback}) {
        HttpHelper.httpPost({
            headers: APICaller.getCurrentHeaders(),
            path: "blog-service/extra/token/login",
            requestData: {
                "uid": ac,
                "pw": pw,
                "scope": "web"
            },
            successCallback: function (response) {
                APICaller.defaultSuccessCallback(response);
                successCallback(response);
            },
            errorCallback: errorCallback,
            timeOutCallback: timeOutCallback,
            finallyCallback: finallyCallback
        });
    }


    static querySummaryByPage({boardNo, currentPage, pageSize, successCallback, errorCallback, timeOutCallback, finallyCallback}) {
        HttpHelper.httpGet({
            headers: APICaller.getCurrentHeaders(),
            path: "blog-service/main/article/summary/" + boardNo + "?currentPage=" + currentPage + "&pageSize=" + pageSize,
            successCallback: function (response) {
                APICaller.defaultSuccessCallback(response);
                successCallback(response);
            },
            errorCallback: errorCallback,
            timeOutCallback: timeOutCallback,
            finallyCallback: finallyCallback
        });
    }

    static queryFileInfo({successCallback, errorCallback, timeOutCallback, finallyCallback}) {
        HttpHelper.httpGet({
            headers: APICaller.getCurrentHeaders(),
            path: "blog-service/main/article/summary/" + boardNo + "?currentPage=" + currentPage + "&pageSize=" + pageSize,
            successCallback: function (response) {
                APICaller.defaultSuccessCallback(response);
                successCallback(response);
            },
            errorCallback: errorCallback,
            timeOutCallback: timeOutCallback,
            finallyCallback: finallyCallback
        });
    }

    static queryArticle({articleNo, successCallback, errorCallback, timeOutCallback, finallyCallback}) {
        HttpHelper.httpGet({
            headers: APICaller.getCurrentHeaders(),
            path: "blog-service/main/article/" + articleNo,
            successCallback: function (response) {
                APICaller.defaultSuccessCallback(response);
                successCallback(response);
            },
            errorCallback: errorCallback,
            timeOutCallback: timeOutCallback,
            finallyCallback: finallyCallback
        });
    }

}