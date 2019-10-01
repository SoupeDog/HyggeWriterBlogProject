export default class URLHelper {
    // url 默认前缀
    static getPrefix() {
        return "http://localhost:9000/";
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

    static openNewPage({finalUrl, inNewTab}) {
        if (inNewTab == null || inNewTab == false) {
            window.location.href = finalUrl;
        } else {
            window.open(finalUrl);
        }
    }
}