import PropertiesHelper from "./PropertiesHelper.jsx";

export default class URLHelper {
    // url 默认前缀
    static getJumpPrefix() {
        // return "https://www.xavierwang.cn/";
        return "http://192.168.18.12:9000/";
    }

    // url 默认前缀
    static getStaticPrefix() {
        return "https://www.xavierwang.cn/static/";
    }

    static getQueryString(key) {
        let fullURL = window.location.href;
        let start = fullURL.indexOf('?');
        let search = fullURL.substring(start + 1);
        let searchArr = search.split('&');
        let searchObj = {};
        for (let i = 0; i < searchArr.length; i++) {
            let arr = searchArr[i].split('=');
            searchObj[arr[0]] = arr[1];
        }
        if (searchObj[key]) {
            return decodeURI(searchObj[key]);
        } else {
            return null;
        }
    }

    static openNewPage({finalUrl, inNewTab, delayTime}) {
        if (inNewTab == null || inNewTab == false) {
            if (PropertiesHelper.isNumberNotNull(delayTime) && delayTime != null) {
                window.setTimeout(function () {
                    window.location.href = finalUrl;
                }, delayTime);
            } else {
                window.location.href = finalUrl;
            }
        } else {
            if (PropertiesHelper.isNumberNotNull(delayTime) && delayTime != null) {
                window.setTimeout(function () {
                    window.open(finalUrl);
                }, delayTime);
            } else {
                window.open(finalUrl);
            }
        }
    }
}