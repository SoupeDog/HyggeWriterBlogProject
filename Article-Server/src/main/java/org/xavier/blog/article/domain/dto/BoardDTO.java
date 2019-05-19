package org.xavier.blog.article.domain.dto;


import org.xavier.blog.article.domain.po.board.Board;

/**
 * 描述信息：<br/>
 * 板块信息数据传输层对象
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/1/14
 * @since Jdk 1.8
 */
public class BoardDTO {
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
     * 最后修改时间 utc 毫秒级时间戳
     */
    private Long lastUpdateTs;
    /**
     * 创建时间 utc 毫秒级时间戳
     */
    private Long ts;


    public BoardDTO() {
    }

    public BoardDTO(Board board) {
        boardId = board.getBoardId();
        uId = board.getuId();
        boardName = board.getBoardName();
        lastUpdateTs = board.getLastUpdateTs();
        ts = board.getTs();
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
