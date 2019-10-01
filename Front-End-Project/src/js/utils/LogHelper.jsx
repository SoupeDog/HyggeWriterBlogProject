let CurrentLogLevel = {tag: "warn", index: 20};    // 当前日志级别
console.log("[LogHelper] " + "constructor----------");
export default class LogHelper {

    static getCurrentLogLevel() {
        return CurrentLogLevel;
    }

    static setCurrentLogLevel(currentLogLevel) {
        CurrentLogLevel = currentLogLevel;
    }

    static getLevelIndex(levelTag) {
        switch (levelTag) {
            case "info":
                return 10;
            case "pointInfo":
                return 15;
            case "warn":
                return 20;
            case "error":
                return 30;
            case "always":
                return 100;
            default :
                return 0;
        }
    }

    static log({level, msg, isJson, tag, className}) {
        if (className == null || className.trim() == "") {
            if (!(this.getCurrentLogLevel().index > this.getLevelIndex(level))) {
                if (isJson != null && isJson) {
                    if (tag != null) {
                        console.log(tag + " : " + JSON.stringify(msg));
                    } else {
                        console.log(JSON.stringify(msg));
                    }
                } else {
                    if (msg == null) {
                        console.log(tag + " : ");
                    } else {
                        console.log(msg);
                    }
                }
            }
        } else {
            if (!(this.getCurrentLogLevel().index > this.getLevelIndex(level))) {
                if (isJson != null && isJson) {
                    if (tag != null) {
                        console.log("[" + className + "] " + tag + " : " + JSON.stringify(msg));
                    } else {
                        console.log("[" + className + "] " + JSON.stringify(msg));
                    }
                } else {
                    if (msg == null) {
                        console.log("[" + className + "] " + tag + " :");
                    } else {
                        console.log("[" + className + "] " + msg);
                    }
                }
            }
        }
    }


    static debug({msg, isJson, tag, className}) {
        this.log({className: className, level: "debug", msg: msg, isJson: isJson, tag: tag});
    }

    static info({msg, isJson, tag, className}) {
        this.log({className: className, level: "info", msg: msg, isJson: isJson, tag: tag});
    }

    static pointInfo({msg, isJson, tag, className}) {
        this.log({className: className, level: "pointInfo", msg: msg, isJson: isJson, tag: tag});
    }

    static warn({msg, isJson, tag, className}) {
        this.log({className: className, level: "warn", msg: msg, isJson: isJson, tag: tag});
    }

    static error({msg, isJson, tag, className}) {
        this.log({className: className, level: "error", msg: msg, isJson: isJson, tag: tag});
    }

    static always({msg, isJson, tag, className}) {
        this.log({className: className, level: "always", msg: msg, isJson: isJson, tag: tag});
    }
}