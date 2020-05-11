// 根据项目页面有无 jquery 环境自行决定引入方式
// import $ from 'jquery'
import PropertiesHelper from './PropertiesHelper.jsx';

export default class HttpHelper {
    // 是否开启日志输出
    static isEnableLog() {
        return false;
    }

    // url 默认前缀
    static getPrefix() {
        return "https://www.xavierwang.cn/";
        // return "http://127.0.0.1:8080/";
    }

    // ajax 请求缓存是否开启标识(false:不开启)
    static getDefaultCache() {
        return false;
    }

    // 默认 Content-Type
    static getDefaultContentType() {
        return "application/json";
    }

    // 默认请求超时时间(毫秒)
    static getDefaultTimeOut() {
        return 5000;
    }

    static httpGet({path, finalUrl, headers, contentType, cache, timeout, requestBefore, successCallback, errorCallback, timeOutCallback, finallyCallback}) {
        let requestUrl;
        let needFinallyCallback = false;
        let needTimeOutCallback = false;
        let needErrorCallback = false;
        let needRequestBefore = false;
        let needLog = this.isEnableLog();
        let currentTimeOut = timeout == null ? this.getDefaultTimeOut() : timeout;
        if (finallyCallback != null) {
            if (PropertiesHelper.isFunctionNotNull(finallyCallback)) {
                needFinallyCallback = true;
            } else {
                throw new Error("HttpHelper:[finallyCallback] should be Function ,and it can't be null.");
            }
        }
        if (requestBefore != null) {
            if (PropertiesHelper.isFunctionNotNull(requestBefore)) {
                needRequestBefore = true;
            } else {
                if (needFinallyCallback) {
                    finallyCallback({httpHelperMsg: "HttpHelper:[requestBefore] should be Function ,and it can't be null."})
                }
                throw new Error("HttpHelper:[requestBefore] should be Function ,and it can't be null.");
            }
        }
        if (!PropertiesHelper.isFunctionNotNull(successCallback)) {
            if (needFinallyCallback) {
                finallyCallback({httpHelperMsg: "HttpHelper:[successCallback] should be Function ,and it can't be null."})
            }
            throw new Error("HttpHelper:[successCallback] should be Function ,and it can't be null.");
        }
        if (errorCallback != null) {
            if (PropertiesHelper.isFunctionNotNull(errorCallback)) {
                needErrorCallback = true;
            } else {
                if (needFinallyCallback) {
                    finallyCallback({httpHelperMsg: "HttpHelper:[errorCallback] should be Function ,and it can't be null."})
                }
                throw new Error("HttpHelper:[errorCallback] should be Function ,and it can't be null.");
            }
        }
        if (timeOutCallback != null) {
            if (PropertiesHelper.isFunctionNotNull(timeOutCallback)) {
                needTimeOutCallback = true;
            } else {
                if (needFinallyCallback) {
                    finallyCallback({httpHelperMsg: "HttpHelper:[timeOutCallback] should be Function ,and it can't be null."})
                }
                throw new Error("HttpHelper:[timeOutCallback] should be Function ,and it can't be null.");
            }
        }
        if (contentType != null && !PropertiesHelper.isStringNotNull(contentType)) {
            if (needFinallyCallback) {
                finallyCallback({httpHelperMsg: "HttpHelper:[contentType] should be String ,and it can't be null."})
            }
            throw new Error("HttpHelper:[contentType] should be String ,and it can't be null.");
        }
        if (finalUrl == null) {
            if (!PropertiesHelper.isStringNotNull(path)) {
                if (needFinallyCallback) {
                    finallyCallback({httpHelperMsg: "HttpHelper:[path] should be String ,and it can't be null."})
                }
                throw new Error("HttpHelper:[path] should be String ,and it can't be null.");
            }
            requestUrl = this.getPrefix() + path;
        } else {
            requestUrl = finalUrl;
        }
        if (needRequestBefore) {
            requestBefore();
        }
        $.ajax({
            url: requestUrl,
            headers: headers,
            type: 'GET',
            contentType: contentType != null ? contentType : this.getDefaultContentType(),
            cache: cache != null ? cache : this.getDefaultCache(),
            success: function (response) {
                if (needFinallyCallback) {
                    finallyCallback(response);
                }
                if (needLog) {
                    console.log(response);
                }
                successCallback(response);
            },
            error: function (response) {
                if (needFinallyCallback) {
                    finallyCallback(response);
                }
                if (needLog) {
                    console.log("HttpHelper(error):" + JSON.stringify(response));
                }
                if (response.status === 0 && response.statusText === "timeout") {
                    if (needTimeOutCallback) {
                        timeOutCallback(response);
                    } else {
                        alert("HttpHelper:请求超时(" + currentTimeOut + "ms).");
                    }
                } else {
                    if (needErrorCallback) {
                        errorCallback(response);
                    }
                }
            },
            timeout: currentTimeOut
        });
    }

    static httpPost({path, finalUrl, headers, contentType, cache, timeout, requestData, requestBefore, successCallback, errorCallback, timeOutCallback, finallyCallback}) {
        let requestUrl;
        let needFinallyCallback = false;
        let needTimeOutCallback = false;
        let needErrorCallback = false;
        let needRequestBefore = false;
        let needLog = this.isEnableLog();
        let currentTimeOut = timeout == null ? this.getDefaultTimeOut() : timeout;
        if (finallyCallback != null) {
            if (PropertiesHelper.isFunctionNotNull(finallyCallback)) {
                needFinallyCallback = true;
            } else {
                throw new Error("HttpHelper:[finallyCallback] should be Function ,and it can't be null.");
            }
        }
        if (requestBefore != null) {
            if (PropertiesHelper.isFunctionNotNull(requestBefore)) {
                needRequestBefore = true;
            } else {
                if (needFinallyCallback) {
                    finallyCallback({httpHelperMsg: "HttpHelper:[requestBefore] should be Function ,and it can't be null."})
                }
                throw new Error("HttpHelper:[requestBefore] should be Function ,and it can't be null.");
            }
        }
        if (!PropertiesHelper.isFunctionNotNull(successCallback)) {
            if (needFinallyCallback) {
                finallyCallback({httpHelperMsg: "HttpHelper:[successCallback] should be Function ,and it can't be null."})
            }
            throw new Error("HttpHelper:[successCallback] should be Function ,and it can't be null.");
        }
        if (errorCallback != null) {
            if (PropertiesHelper.isFunctionNotNull(errorCallback)) {
                needErrorCallback = true;
            } else {
                if (needFinallyCallback) {
                    finallyCallback({httpHelperMsg: "HttpHelper:[errorCallback] should be Function ,and it can't be null."})
                }
                throw new Error("HttpHelper:[errorCallback] should be Function ,and it can't be null.");
            }
        }
        if (timeOutCallback != null) {
            if (PropertiesHelper.isFunctionNotNull(timeOutCallback)) {
                needTimeOutCallback = true;
            } else {
                if (needFinallyCallback) {
                    finallyCallback({httpHelperMsg: "HttpHelper:[timeOutCallback] should be Function ,and it can't be null."})
                }
                throw new Error("HttpHelper:[timeOutCallback] should be Function ,and it can't be null.");
            }
        }
        if (contentType != null && !PropertiesHelper.isStringNotNull(contentType)) {
            if (needFinallyCallback) {
                finallyCallback({httpHelperMsg: "HttpHelper:[contentType] should be String ,and it can't be null."})
            }
            throw new Error("HttpHelper:[contentType] should be String ,and it can't be null.");
        }
        if (finalUrl == null) {
            if (!PropertiesHelper.isStringNotNull(path)) {
                if (needFinallyCallback) {
                    finallyCallback({httpHelperMsg: "HttpHelper:[path] should be String ,and it can't be null."})
                }
                throw new Error("HttpHelper:[path] should be String ,and it can't be null.");
            }
            requestUrl = this.getPrefix() + path;
        } else {
            requestUrl = finalUrl;
        }
        if (needRequestBefore) {
            requestBefore();
        }
        $.ajax({
            url: requestUrl,
            headers: headers,
            type: 'POST',
            contentType: contentType != null ? contentType : this.getDefaultContentType(),
            cache: cache != null ? cache : this.getDefaultCache(),
            data: requestData == null ? null : JSON.stringify(requestData),
            success: function (response) {
                if (needFinallyCallback) {
                    finallyCallback(response);
                }
                if (needLog) {
                    console.log(response);
                }
                successCallback(response);
            },
            error: function (response) {
                if (needFinallyCallback) {
                    finallyCallback(response);
                }
                if (needLog) {
                    console.log("HttpHelper(error):" + JSON.stringify(response));
                }
                if (response.status === 0 && response.statusText === "timeout") {
                    if (needTimeOutCallback) {
                        timeOutCallback(response);
                    } else {
                        alert("HttpHelper:请求超时(" + currentTimeOut + "ms).");
                    }
                } else {
                    if (needErrorCallback) {
                        errorCallback(response);
                    }
                }
            },
            timeout: currentTimeOut
        });
    }

    static httpPut({path, finalUrl, headers, contentType, cache, timeout, requestData, requestBefore, successCallback, errorCallback, timeOutCallback, finallyCallback}) {
        let requestUrl;
        let needFinallyCallback = false;
        let needTimeOutCallback = false;
        let needErrorCallback = false;
        let needRequestBefore = false;
        let needLog = this.isEnableLog();
        let currentTimeOut = timeout == null ? this.getDefaultTimeOut() : timeout;
        if (finallyCallback != null) {
            if (PropertiesHelper.isFunctionNotNull(finallyCallback)) {
                needFinallyCallback = true;
            } else {
                throw new Error("HttpHelper:[finallyCallback] should be Function ,and it can't be null.");
            }
        }
        if (requestBefore != null) {
            if (PropertiesHelper.isFunctionNotNull(requestBefore)) {
                needRequestBefore = true;
            } else {
                if (needFinallyCallback) {
                    finallyCallback({httpHelperMsg: "HttpHelper:[requestBefore] should be Function ,and it can't be null."})
                }
                throw new Error("HttpHelper:[requestBefore] should be Function ,and it can't be null.");
            }
        }
        if (!PropertiesHelper.isFunctionNotNull(successCallback)) {
            if (needFinallyCallback) {
                finallyCallback({httpHelperMsg: "HttpHelper:[successCallback] should be Function ,and it can't be null."})
            }
            throw new Error("HttpHelper:[successCallback] should be Function ,and it can't be null.");
        }
        if (errorCallback != null) {
            if (PropertiesHelper.isFunctionNotNull(errorCallback)) {
                needErrorCallback = true;
            } else {
                if (needFinallyCallback) {
                    finallyCallback({httpHelperMsg: "HttpHelper:[errorCallback] should be Function ,and it can't be null."})
                }
                throw new Error("HttpHelper:[errorCallback] should be Function ,and it can't be null.");
            }
        }
        if (timeOutCallback != null) {
            if (PropertiesHelper.isFunctionNotNull(timeOutCallback)) {
                needTimeOutCallback = true;
            } else {
                if (needFinallyCallback) {
                    finallyCallback({httpHelperMsg: "HttpHelper:[timeOutCallback] should be Function ,and it can't be null."})
                }
                throw new Error("HttpHelper:[timeOutCallback] should be Function ,and it can't be null.");
            }
        }
        if (contentType != null && !PropertiesHelper.isStringNotNull(contentType)) {
            if (needFinallyCallback) {
                finallyCallback({httpHelperMsg: "HttpHelper:[contentType] should be String ,and it can't be null."})
            }
            throw new Error("HttpHelper:[contentType] should be String ,and it can't be null.");
        }
        if (finalUrl == null) {
            if (!PropertiesHelper.isStringNotNull(path)) {
                if (needFinallyCallback) {
                    finallyCallback({httpHelperMsg: "HttpHelper:[path] should be String ,and it can't be null."})
                }
                throw new Error("HttpHelper:[path] should be String ,and it can't be null.");
            }
            requestUrl = this.getPrefix() + path;
        } else {
            requestUrl = finalUrl;
        }
        if (needRequestBefore) {
            requestBefore();
        }
        $.ajax({
            url: requestUrl,
            headers: headers,
            type: 'PUT',
            contentType: contentType != null ? contentType : this.getDefaultContentType(),
            cache: cache != null ? cache : this.getDefaultCache(),
            data: requestData == null ? null : JSON.stringify(requestData),
            success: function (response) {
                if (needFinallyCallback) {
                    finallyCallback(response);
                }
                if (needLog) {
                    console.log(response);
                }
                successCallback(response);
            },
            error: function (response) {
                if (needFinallyCallback) {
                    finallyCallback(response);
                }
                if (needLog) {
                    console.log("HttpHelper(error):" + JSON.stringify(response));
                }
                if (response.status === 0 && response.statusText === "timeout") {
                    if (needTimeOutCallback) {
                        timeOutCallback(response);
                    } else {
                        alert("HttpHelper:请求超时(" + currentTimeOut + "ms).");
                    }
                } else {
                    if (needErrorCallback) {
                        errorCallback(response);
                    }
                }
            },
            timeout: currentTimeOut
        });
    }

    static httpDelete({path, finalUrl, headers, contentType, cache, timeout, requestData, requestBefore, successCallback, errorCallback, timeOutCallback, finallyCallback}) {
        let requestUrl;
        let needFinallyCallback = false;
        let needTimeOutCallback = false;
        let needErrorCallback = false;
        let needRequestBefore = false;
        let needLog = this.isEnableLog();
        let currentTimeOut = timeout == null ? this.getDefaultTimeOut() : timeout;
        if (finallyCallback != null) {
            if (PropertiesHelper.isFunctionNotNull(finallyCallback)) {
                needFinallyCallback = true;
            } else {
                throw new Error("HttpHelper:[finallyCallback] should be Function ,and it can't be null.");
            }
        }
        if (requestBefore != null) {
            if (PropertiesHelper.isFunctionNotNull(requestBefore)) {
                needRequestBefore = true;
            } else {
                if (needFinallyCallback) {
                    finallyCallback({httpHelperMsg: "HttpHelper:[requestBefore] should be Function ,and it can't be null."})
                }
                throw new Error("HttpHelper:[requestBefore] should be Function ,and it can't be null.");
            }
        }
        if (!PropertiesHelper.isFunctionNotNull(successCallback)) {
            if (needFinallyCallback) {
                finallyCallback({httpHelperMsg: "HttpHelper:[successCallback] should be Function ,and it can't be null."})
            }
            throw new Error("HttpHelper:[successCallback] should be Function ,and it can't be null.");
        }
        if (errorCallback != null) {
            if (PropertiesHelper.isFunctionNotNull(errorCallback)) {
                needErrorCallback = true;
            } else {
                if (needFinallyCallback) {
                    finallyCallback({httpHelperMsg: "HttpHelper:[errorCallback] should be Function ,and it can't be null."})
                }
                throw new Error("HttpHelper:[errorCallback] should be Function ,and it can't be null.");
            }
        }
        if (timeOutCallback != null) {
            if (PropertiesHelper.isFunctionNotNull(timeOutCallback)) {
                needTimeOutCallback = true;
            } else {
                if (needFinallyCallback) {
                    finallyCallback({httpHelperMsg: "HttpHelper:[timeOutCallback] should be Function ,and it can't be null."})
                }
                throw new Error("HttpHelper:[timeOutCallback] should be Function ,and it can't be null.");
            }
        }
        if (contentType != null && !PropertiesHelper.isStringNotNull(contentType)) {
            if (needFinallyCallback) {
                finallyCallback({httpHelperMsg: "HttpHelper:[contentType] should be String ,and it can't be null."})
            }
            throw new Error("HttpHelper:[contentType] should be String ,and it can't be null.");
        }
        if (finalUrl == null) {
            if (!PropertiesHelper.isStringNotNull(path)) {
                if (needFinallyCallback) {
                    finallyCallback({httpHelperMsg: "HttpHelper:[path] should be String ,and it can't be null."})
                }
                throw new Error("HttpHelper:[path] should be String ,and it can't be null.");
            }
            requestUrl = this.getPrefix() + path;
        } else {
            requestUrl = finalUrl;
        }
        if (needRequestBefore) {
            requestBefore();
        }
        $.ajax({
            url: requestUrl,
            headers: headers,
            type: 'DELETE',
            contentType: contentType != null ? contentType : this.getDefaultContentType(),
            cache: cache != null ? cache : this.getDefaultCache(),
            data: requestData == null ? null : JSON.stringify(requestData),
            success: function (response) {
                if (needFinallyCallback) {
                    finallyCallback(response);
                }
                if (needLog) {
                    console.log(response);
                }
                successCallback(response);
            },
            error: function (response) {
                if (needFinallyCallback) {
                    finallyCallback(response);
                }
                if (needLog) {
                    console.log("HttpHelper(error):" + JSON.stringify(response));
                }
                if (response.status === 0 && response.statusText === "timeout") {
                    if (needTimeOutCallback) {
                        timeOutCallback(response);
                    } else {
                        alert("HttpHelper:请求超时(" + currentTimeOut + "ms).");
                    }
                } else {
                    if (needErrorCallback) {
                        errorCallback(response);
                    }
                }
            },
            timeout: currentTimeOut
        });
    }
}