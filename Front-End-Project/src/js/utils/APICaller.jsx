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
                uid: currentUId,
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

            this.refreshLoginInfo({});
            // 令牌信息有误
            // URLHelper.openNewPage({finalUrl: URLHelper.getJumpPrefix(), inNewTab: false});
        }
    }

    // 尝试刷新登录信息
    static refreshLoginInfo({errorCallback, timeOutCallback, finallyCallback}) {
        let currentHeaders = this.getCurrentHeaders();
        HttpHelper.httpPost({
            headers: currentHeaders,
            requestData: {
                uid: currentHeaders.uid,
                token: currentHeaders.token,
                refreshKey: currentHeaders.refreshKey,
                scope: "web"
            },
            path: "blog-service/extra/token/keep",
            successCallback: function (response) {
                if (response.code == 200) {
                    localStorage.setItem('uid', response.data.uid);
                    localStorage.setItem('token', response.data.token);
                    localStorage.setItem('refreshKey', response.data.refreshKey);
                    URLHelper.openNewPage({finalUrl: URLHelper.getJumpPrefix(), inNewTab: false});
                } else {
                    localStorage.removeItem('uid');
                    localStorage.removeItem('token');
                    localStorage.removeItem('refreshKey');
                    alert("刷新 token 失败")
                }
            },
            errorCallback: errorCallback,
            timeOutCallback: timeOutCallback,
            finallyCallback: finallyCallback
        });
    }

    static login({ac, pw, successCallback, errorCallback, timeOutCallback, finallyCallback}) {
        HttpHelper.httpPost({
            headers: APICaller.getCurrentHeaders(),
            path: "blog-service/extra/token/login",
            requestData: {
                "uid": ac,
                "pw": pw,
                "scope": "web"
            },
            successCallback: function (response) {
                if (successCallback != null) {
                    successCallback(response);
                }
                if (response.code == 200) {
                    localStorage.setItem('uid', response.data.uid);
                    localStorage.setItem('token', response.data.token);
                    localStorage.setItem('refreshKey', response.data.refreshKey);
                    window.setTimeout(function () {
                        URLHelper.openNewPage({finalUrl: URLHelper.getJumpPrefix(), inNewTab: false});
                    }, 1000)
                }
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
            path: "blog-service/main/file",
            successCallback: function (response) {
                APICaller.defaultSuccessCallback(response);
                successCallback(response);
            },
            errorCallback: errorCallback,
            timeOutCallback: timeOutCallback,
            finallyCallback: finallyCallback
        });
    }

    static queryArticleCategoryAll({successCallback, errorCallback, timeOutCallback, finallyCallback}) {
        HttpHelper.httpGet({
            headers: APICaller.getCurrentHeaders(),
            path: "blog-service/main/article/category/all",
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

    static updateArticle({articleNo, boardNo, articleCategoryNo, title, summary, content, bgi, bgmType, bgmSrc, ts, successCallback, errorCallback, timeOutCallback, finallyCallback}) {
        let finalRequestBody = {};
        if (boardNo != null) {
            finalRequestBody.boardNo = boardNo;
        }
        if (articleCategoryNo != null) {
            finalRequestBody.articleCategoryNo = articleCategoryNo;
        }
        if (title != null) {
            finalRequestBody.title = title;
        }
        if (summary != null) {
            finalRequestBody.summary = summary;
        }
        if (content != null) {
            finalRequestBody.content = content;
        }
        let propertiesConfig = {};
        if (bgi != null) {
            propertiesConfig.bgi = URLHelper.getStaticPrefix() + bgi;
        }
        let bgmConfig = {};

        if (bgmSrc != null && bgmType != "3") {
            bgmConfig.bgmType = bgmType;
            bgmConfig.src = bgmSrc;
        }
        propertiesConfig.bgmConfig = bgmConfig;
        finalRequestBody.ts = ts;
        finalRequestBody.properties = propertiesConfig;

        HttpHelper.httpPut({
            headers: APICaller.getCurrentHeaders(),
            path: "blog-service/main/article/" + articleNo,
            requestData: finalRequestBody,
            successCallback: function (response) {
                APICaller.defaultSuccessCallback(response);
                successCallback(response);
            },
            errorCallback: errorCallback,
            timeOutCallback: timeOutCallback,
            finallyCallback: finallyCallback
        });
    }

    static addArticle({boardNo, articleCategoryNo, title, summary, content, bgi, bgmType, bgmSrc, successCallback, errorCallback, timeOutCallback, finallyCallback}) {
        let finalRequestBody = {};
        if (boardNo != null) {
            finalRequestBody.boardNo = boardNo;
        }
        if (articleCategoryNo != null) {
            finalRequestBody.articleCategoryNo = articleCategoryNo;
        }
        if (title != null) {
            finalRequestBody.title = title;
        }
        if (summary != null) {
            finalRequestBody.summary = summary;
        }
        if (content != null) {
            finalRequestBody.content = content;
        }
        let propertiesConfig = {};
        if (bgi != null) {
            propertiesConfig.bgi = URLHelper.getStaticPrefix() + bgi;
        }
        let bgmConfig = {};

        if (bgmSrc != null && bgmType != "3") {
            bgmConfig.bgmType = bgmType;
            bgmConfig.src = bgmSrc;
        }
        propertiesConfig.bgmConfig = bgmConfig;
        finalRequestBody.properties = JSON.stringify(propertiesConfig);
        HttpHelper.httpPost({
            headers: APICaller.getCurrentHeaders(),
            path: "blog-service/main/article",
            requestData: finalRequestBody,
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