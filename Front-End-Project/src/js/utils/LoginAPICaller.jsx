import LogHelper from "./LogHelper.jsx";
import HttpHelper from "./HttpHelper.jsx";
import URLHelper from "./URLHelper.jsx";


export default class LoginAPICaller {
    // 获取 headers
    static getCurrentHeaders() {
        let _this = this;
        let currentSecretKey = URLHelper.getQueryString("secretKey");
        if (currentSecretKey != null) {
            currentSecretKey = currentSecretKey.substring(0, 32);
        }
        let currentHeaders = null;
        let currentUId = localStorage.getItem("uid");
        let currentToken = localStorage.getItem("token");
        let currentRefreshKey = localStorage.getItem("refreshKey");

        if (currentUId == null || currentToken == null || currentRefreshKey == null) {
            _this.removeLoginInfo();
        } else {
            if (currentSecretKey != null) {
                currentHeaders = {
                    secretKey: currentSecretKey
                };
            } else {
                currentHeaders = {
                    uid: currentUId,
                    token: currentToken,
                    refreshKey: currentRefreshKey
                };
            }
        }
        return currentHeaders;
    }

    // 设置登录信息
    static resetLoginInfo({uid, token, refreshKey, userInfo}) {
        localStorage.setItem('uid', uid);
        localStorage.setItem('token', token);
        localStorage.setItem('refreshKey', refreshKey);
        localStorage.setItem('userInfo', JSON.stringify(userInfo));
        // window.setTimeout(function () {
        //     URLHelper.openNewPage({finalUrl: URLHelper.getJumpPrefix(), inNewTab: false});
        // }, 1000)
    }

    // 清空登录信息
    static removeLoginInfo() {
        localStorage.removeItem('uid');
        localStorage.removeItem('token');
        localStorage.removeItem('refreshKey');
        localStorage.removeItem('userInfo');
        LogHelper.info({msg: "登录身份信息已失效", isJson: true})
    }

    static defaultResponseCallback({response, successCallback, errorCallback}) {
        let _this = this;
        if (response == null) {
            LogHelper.error({msg: "请求返回值为空"});
        } else {
            if (response.code == 200) {
                if (successCallback != null) {
                    successCallback(response);
                }
            } else {
                // 登录身份验证
                switch (response.code) {
                    case 403001:
                    case 403002:
                    case 403003:
                        // 登录身份有误
                        _this.refreshLoginInfo({});
                        break;
                    default:
                        // 默认失败回调
                        if (errorCallback != null) {
                            errorCallback();
                        }
                }
            }
        }
    }

    // 尝试刷新登录信息
    static refreshLoginInfo({errorCallback, timeOutCallback, finallyCallback}) {
        LogHelper.info({msg: "尝试刷新身份信息"});
        let _this = this;
        let currentHeaders = _this.getCurrentHeaders();
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
                    _this.resetLoginInfo({
                        uid: response.data.uid,
                        token: response.data.token,
                        refreshKey: response.data.refreshKey,
                        userInfo: response.data.userInfo
                    });
                } else {
                    _this.removeLoginInfo();
                    alert("自动登录失败，即将重置身份信息");
                    URLHelper.openNewPage({
                        finalUrl: URLHelper.getJumpPrefix()
                    });
                }
            },
            errorCallback: errorCallback,
            timeOutCallback: timeOutCallback,
            finallyCallback: finallyCallback
        });
    }

    static login({ac, pw, requestBefore, successCallback, errorCallback, timeOutCallback, finallyCallback}) {
        let _this = this;
        let currentHeaders = _this.getCurrentHeaders();
        HttpHelper.httpPost({
            headers: currentHeaders,
            path: "blog-service/extra/token/login",
            requestData: {
                "uid": ac,
                "pw": pw,
                "scope": "web"
            },
            requestBefore: requestBefore,
            successCallback: function (response) {
                _this.defaultResponseCallback({
                    response: response,
                    successCallback: function (response) {
                        if (successCallback != null) {
                            successCallback(response);
                        }
                        _this.resetLoginInfo({
                            uid: response.data.uid,
                            token: response.data.token,
                            refreshKey: response.data.refreshKey,
                            userInfo: response.data.userInfo
                        });
                    },
                    errorCallback: errorCallback
                });
            },
            errorCallback: errorCallback,
            timeOutCallback: timeOutCallback,
            finallyCallback: finallyCallback
        });
    }
}