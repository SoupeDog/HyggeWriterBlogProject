package org.xavier.blog.domain.bo;

import org.xavier.blog.domain.po.Board;

import java.util.LinkedHashMap;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 20-5-8
 * @since Jdk 1.8
 */
public class BoardArticleCategoryArticleCountInfo {
    private Board board;
    private LinkedHashMap<String, ArticleCategoryArticleCountInfo> articleCategoryCountInfoMap = new LinkedHashMap();

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public LinkedHashMap<String, ArticleCategoryArticleCountInfo> getArticleCategoryCountInfoMap() {
        return articleCategoryCountInfoMap;
    }

    public void setArticleCategoryCountInfoMap(LinkedHashMap<String, ArticleCategoryArticleCountInfo> articleCategoryCountInfoMap) {
        this.articleCategoryCountInfoMap = articleCategoryCountInfoMap;
    }
}
