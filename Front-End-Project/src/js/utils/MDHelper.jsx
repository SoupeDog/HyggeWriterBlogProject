import PropertiesHelper from "./PropertiesHelper.jsx";
import LogHelper from "./LogHelper.jsx";

export default class MDHelper {
    // 根据目录数组生成目录树
    static getTocTree({currentTOCArray, errorCallback}) {
        // 初始化全部节点的 map 容器
        let allNodeMap = new Map();
        currentTOCArray.map((item, index) => {
            item.nodeId = index;
            allNodeMap.set(index, item);
        });

        let tocTree = new Array();
        let rootNodeLevel = 1;
        // 初始化上一个节点对象
        let prevNode = null;
        let currentParentNode = null;

        currentTOCArray.map((item, index) => {
            // 创建当前节点对象
            let currentNodeName = item.nodeName;
            item.level = MDHelper.getTitleLevel({
                nodeName: currentNodeName,
                errorCallback: errorCallback
            });
            item.childList = new Array();

            // 如果是首个节点，初始化根节点级别
            if (index == 0) {
                // 首个节点默认为根节点，初始其父节点为空
                item.parentNode = null;
                rootNodeLevel = item.level;
                prevNode = item;
                currentParentNode = item;
                tocTree.push(item);
                console.log("index == 0");
            } else {
                // 非首个节点
                // 当前节是根节点
                if (item.level == rootNodeLevel) {
                    // 当前节点为根节点，初始其父节点为空
                    item.parentNode = null;
                    prevNode = item;
                    tocTree.push(item);
                } else if (item.level > rootNodeLevel) {
                    // 当前节点是子节点
                    if (item.level == prevNode.level) {
                        // 当前节点是上一个节点的兄弟节点，分支同辈扩散
                        let parentOfPrevNode = allNodeMap.get(prevNode.parentNode);
                        item.parentNode = parentOfPrevNode.nodeId;
                        parentOfPrevNode.childList.push(item);
                        prevNode = item;
                    } else if (item.level > prevNode.level) {
                        // 当前节点是上一个节点的子节点，分支继续深入
                        item.parentNode = prevNode.nodeId;
                        prevNode.childList.push(item);
                        prevNode = item;
                    } else {
                        // 当前节点是上一个节点的长辈节点，分支按长辈扩散
                        let prevNodeSeniorNodeId = MDHelper.getNodeIdByLevel({
                            startNode: prevNode,
                            targetLevel: item.level,
                            allNodeMap: allNodeMap
                        });
                        if (prevNodeSeniorNodeId != null) {
                            let prevNodeSeniorNode = allNodeMap.get(prevNodeSeniorNodeId);
                            let parentOfPrevNodeSeniorNodeId = prevNodeSeniorNode.parentNode;
                            let parentOfPrevNodeSeniorNode = allNodeMap.get(parentOfPrevNodeSeniorNodeId);
                            item.parentNode = parentOfPrevNodeSeniorNode.nodeId;
                            parentOfPrevNodeSeniorNode.childList.push(item);
                            prevNode = parentOfPrevNodeSeniorNode;
                        } else {
                            MDHelper.defaultCallErrorCallback({
                                errorCallback: errorCallback,
                                msg: "非标准的目录关系，创建目录失败-2"
                            });
                        }
                    }
                } else {
                    MDHelper.defaultCallErrorCallback({
                        errorCallback: errorCallback,
                        msg: "非标准的目录关系，创建目录失败-1"
                    });
                }
            }
        });
        console.log(JSON.stringify(tocTree));
        return tocTree;
    }

    // 从起始节点向根节点遍历，寻找目标级别的节点位移标识
    static getNodeIdByLevel({startNode, targetLevel, allNodeMap}) {
        let parentNodeId = startNode.parentNode;
        if (parentNodeId == null) {
            // 未找到结果
            return null;
        } else {
            let parentNode = allNodeMap.get(parentNodeId);
            if (parentNode.level == targetLevel) {
                return parentNode.nodeId;
            } else {
                return MDHelper.getNodeIdByLevel({
                    startNode: parentNode,
                    targetLevel: targetLevel,
                    allNodeMap: allNodeMap
                });
            }
        }
    }


    static getTitleLevel({nodeName, errorCallback}) {
        switch (nodeName) {
            case "H1":
                return 1;
            case "H2":
                return 2;
            case "H3":
                return 3;
            case "H4":
                return 4;
            case "H5":
                return 5;
            default:
                MDHelper.defaultCallErrorCallback({
                    errorCallback: errorCallback,
                    msg: "仅支持五级目录"
                });
                return null;
        }
    }

    static defaultCallErrorCallback({errorCallback, msg}) {
        if (PropertiesHelper.isFunctionNotNull(errorCallback)) {
            if (PropertiesHelper.isStringNotNull(msg)) {
                errorCallback(msg);
            } else {
                errorCallback();
            }
        } else {
            LogHelper.error({msg: "Fail to call errorCallback of getTOCTree(). " + msg});
        }
    }
}