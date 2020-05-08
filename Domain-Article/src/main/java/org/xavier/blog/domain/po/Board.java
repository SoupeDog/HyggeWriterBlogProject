package org.xavier.blog.domain.po;


import org.xavier.blog.common.enums.DefaultStateEnum;
import org.xavier.common.util.PropertiesHelper;
import org.xavier.common.util.UtilsCreator;

import java.util.Objects;

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
    private Integer boardId;
    /**
     * 板块显示编号
     */
    private String boardNo;

    /**
     * 板块名称
     */
    private String boardName;
    /**
     * 创建者唯一标识
     */
    private String uid;

    /**
     * 板块状态 0 禁用 1 开放
     */
    private DefaultStateEnum state;
    /**
     * 创建时间 utc 毫秒级时间戳
     */
    private Long createTs;
    /**
     * 最后修改时间 utc 毫秒级时间戳
     */
    private Long lastUpdateTs;

    /**
     * 参数校验
     */
    public void validate() {
        PropertiesHelper propertiesHelper = UtilsCreator.getDefaultPropertiesHelperInstance();
        propertiesHelper.stringNotNull(boardName, 1, 30, "[boardName] can't be null,and its length should within 30.");
    }

    public Integer getBoardId() {
        return boardId;
    }

    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
    }

    public String getBoardNo() {
        return boardNo;
    }

    public void setBoardNo(String boardNo) {
        this.boardNo = boardNo;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public DefaultStateEnum getState() {
        return state;
    }

    public void setState(DefaultStateEnum state) {
        this.state = state;
    }

    public Long getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Long createTs) {
        this.createTs = createTs;
    }

    public Long getLastUpdateTs() {
        return lastUpdateTs;
    }

    public void setLastUpdateTs(Long lastUpdateTs) {
        this.lastUpdateTs = lastUpdateTs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Objects.equals(boardNo, board.boardNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardNo);
    }
}