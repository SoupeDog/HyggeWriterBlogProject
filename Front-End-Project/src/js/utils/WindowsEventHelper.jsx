import LogHelper from "./LogHelper.jsx";

const Scroll_FunctionMap = new Map();
const Resize_FunctionMap = new Map();
const FunctionLimiter_Map = new Map();
const FunctionLimiter_Time = new Map();
LogHelper.info({className: "WindowsEventHelper", msg: "constructor----------"});

export default class WindowsEventHelper {

    static addCallback_Scroll({name, callbackFunction, delta}) {
        if (Scroll_FunctionMap.has(name) || FunctionLimiter_Time.has(name)) {
            // throw new Error("Duplicate callback key : " + name);
            LogHelper.warn({className:"WindowsEventHelper",msg:"AddCallback_Scroll duplicate callback key : " + name});
        }
        Scroll_FunctionMap.set(name,callbackFunction);
        if (delta != null) {
            FunctionLimiter_Time.set(name, delta);
        }
        LogHelper.info({className: "WindowsEventHelper", msg: "AddCallback_Scroll : " + name});
    }

    static getScroll_FunctionMap() {
        return Scroll_FunctionMap;
    }

    static addCallback_Resize({name, callbackFunction, delta}) {
        if (Scroll_FunctionMap.has(name) || FunctionLimiter_Time.has(name)) {
            // throw new Error("Duplicate callback key : " + name);
            LogHelper.warn({className:"WindowsEventHelper",msg:"AddCallback_Resize duplicate callback key : " + name});
        }
        Resize_FunctionMap.set(name, callbackFunction);
        if (delta != null) {
            FunctionLimiter_Time.set(name, delta);
        }
        LogHelper.info({className: "WindowsEventHelper", msg: "AddCallback_Resize : " + name});
    }

    static getResize_FunctionMap() {
        return Resize_FunctionMap;
    }

    static start_OnScroll() {
        window.onscroll = () => {
            LogHelper.info({className: "WindowsEventHelper", msg: "OnScroll----------"+Scroll_FunctionMap.size});
            let currentScrollY = window.scrollY;
            for (let [name, callbackFunction] of Scroll_FunctionMap) {
                if (typeof callbackFunction == "function") {
                    if (!FunctionLimiter_Map.has(name)) {
                        FunctionLimiter_Map.set(name, true);
                        callbackFunction({currentScrollY});
                        let delta = FunctionLimiter_Time.has(name) ? FunctionLimiter_Time.get(name) : 500;
                        window.setTimeout(function () {
                            FunctionLimiter_Map.delete(name)
                        }, delta);
                    }
                }
            }
        }
    }

    static start_OnResize() {
        window.onresize = () => {
            LogHelper.info({className: "WindowsEventHelper", msg: "OnResize----------"+Resize_FunctionMap.size});
            let currentHight = window.innerHeight;
            let currentWidth = window.innerWidth;
            for (let [name, callbackFunction] of Resize_FunctionMap) {
                if (typeof callbackFunction == "function") {
                    if (!FunctionLimiter_Map.has(name)) {
                        FunctionLimiter_Map.set(name, true);
                        callbackFunction({currentWidth, currentHight});
                        let delta = FunctionLimiter_Time.has(name) ? FunctionLimiter_Time.get(name) : 500;
                        window.setTimeout(function () {
                            FunctionLimiter_Map.delete(name)
                        }, delta);
                    }
                }
            }
        }
    }
}