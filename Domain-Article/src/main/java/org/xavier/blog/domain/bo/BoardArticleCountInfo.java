package org.xavier.blog.domain.bo;

import org.xavier.blog.domain.po.Board;

/**
 * 描述信息：<br/>
 * 板块文章计数器
 *
 * @author Xavier
 * @version 1.0
 * @date 20-5-6
 * @since Jdk 1.8
 */
public class BoardArticleCountInfo {
    /**
     * 板块唯一标识
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
     * 板块内文章数量
     */
    private Integer totalCount;

    public BoardArticleCountInfo() {
    }

    public BoardArticleCountInfo(Board board) {
        this.boardId = board.getBoardId();
        this.boardNo = board.getBoardNo();
        this.boardName = board.getBoardName();
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

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}