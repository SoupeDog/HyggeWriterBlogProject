export default class TimeHelper {
    // 一天的毫秒数
    static getDayMsec() {
        return 86400000;
    }

    // 填充 target 为 size 位数
    static formatNumber({target, size}) {
        if (typeof target != "number") {
            throw new Error("TimeHelper:[target] of formatNumber(target, size) should be number.");
        }
        if (typeof size != "number") {
            throw new Error("TimeHelper:[size] of formatNumber(target, size) should be number.");
        }
        let compareNumber = Math.pow(10, size);
        let needAddCount = 0;
        let result = "";
        let prefix = "";
        if (target < 0) {
            prefix = "-";
        }
        target = Math.abs(target);
        if (target < 2) {
            needAddCount = size - 1;
        } else {
            while (Math.pow(10, needAddCount + 1) * target < compareNumber) {
                needAddCount += 1;
            }
        }
        for (let i = 0; i < needAddCount; i++) {
            result += "0";
        }
        result += target;
        return prefix + result;
    }

    // 获取当前时间 ± x 天的毫秒级时间戳
    static getTimeStamp(x) {
        let currentTs = new Date().getTime();
        if (x == null) {
            return currentTs;
        }
        if (typeof x != "number") {
            throw new Error("TimeHelper:[target] of getTimeStamp(x) should be number.");
        }
        currentTs -= x * this.getDayMsec();
        return currentTs;
    }

    // 目标毫秒级时间戳格式化成字符串,默认格式为 yyyy-mm-dd hh:mm:ss
    static formatTimeStampToString({target, type}) {
        if (typeof target != "number") {
            throw new Error("TimeHelper:[target] of formatTimeStampToString(target, type) should be number.");
        }
        let currentDate = new Date(target);
        let year = currentDate.getFullYear();
        let month = currentDate.getMonth() + 1;
        let day = currentDate.getDate();
        let hour = currentDate.getHours();
        let minute = currentDate.getMinutes();
        let second = currentDate.getSeconds();
        switch (type) {
            case "yyyy-mm-dd":
                return year + "-" + this.formatNumber({target: month, size: 2}) + "-" + this.formatNumber({
                    target: day,
                    size: 2
                });
            case "hh:mm:ss":
                return this.formatNumber({target: hour, size: 2}) + ":" +
                    this.formatNumber({
                        target: minute,
                        size: 2
                    }) + ":" +
                    this.formatNumber({target: second, size: 2});
            default:
                return year + "-" + this.formatNumber({target: month, size: 2}) + "-" + this.formatNumber({
                        target: day,
                        size: 2
                    }) + " " +
                    this.formatNumber({target: hour, size: 2}) + ":" + this.formatNumber({
                        target: minute,
                        size: 2
                    }) + ":" + this.formatNumber({target: second, size: 2});
        }
    }

    // 获取目标时间戳 ± x 个自然天的 00:00:00 时刻时间戳
    static getNaturalDayTimeStamp({target, x}) {
        let currentDate = new Date(target);
        let year = currentDate.getFullYear();
        let month = currentDate.getMonth();
        let day = currentDate.getDate();
        if (typeof target != "number") {
            throw new Error("TimeHelper:[target] of getNaturalDayTimeStamp(target, x) should be number.");
        }
        let resultDate = new Date(year, month, day, 0, 0, 0, 0);
        if (x == null) {
            x = 0;
        }
        if (typeof x != "number") {
            throw new Error("TimeHelper:[x] of getNaturalDayTimeStamp(target, x) should be number.");
        }
        return resultDate.getTime() + x * this.getDayMsec();
    }
}