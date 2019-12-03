package org.xavier.blog.article.domain.po.board;


import org.xavier.common.util.PropertiesHelper;
import org.xavier.common.util.UtilsCreator;

/**
 * 描述信息：<br/>
 * 板块
 *
 * @author Xavier
 * @version 1.0
 * @date 2017.10.27
 * @since Jdk 1.8
 */
public class Board {
    /**
     * 唯一标识
     */
    private String boardId;
    /**
     * 创建者唯一标识
     */
    private String uId;
    /**
     * 板块名称
     */
    private String boardName;
    /**
     * 板块合法性标识 null:待审核 true:合法 false:非法
     */
    private Boolean legal_Flag;
     /**
     * 最后修改时间 utc 毫秒级时间戳
     */
    private Long lastUpdateTs;
    /**
     * 创建时间 utc 毫秒级时间戳
     */
    private Long ts;

    /**
     * 参数校验
     */
    public void validate() {
        PropertiesHelper propertiesHelper = UtilsCreator.getDefaultPropertiesHelperInstance();
        propertiesHelper.stringNotNull(boardName, 1, 30, "[boardName] can't be null,and its length should within 30.");
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public Boolean getLegal_Flag() {
        return legal_Flag;
    }

    public void setLegal_Flag(Boolean legal_Flag) {
        this.legal_Flag = legal_Flag;
    }

    public Long getLastUpdateTs() {
        return lastUpdateTs;
    }

    public void setLastUpdateTs(Long lastUpdateTs) {
        this.lastUpdateTs = lastUpdateTs;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }
}