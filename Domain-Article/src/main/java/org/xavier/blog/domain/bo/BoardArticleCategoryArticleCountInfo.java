package org.xavier.blog.domain.bo;

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
    private BoardArticleCountInfo boardArticleCountInfo;
    private LinkedHashMap<String, ArticleCategoryArticleCountInfo> articleCategoryCountInfoMap = new LinkedHashMap();

    public void initBoardTotalCount() {
        Integer totalCount = 0;
        for (ArticleCategoryArticleCountInfo item : articleCategoryCountInfoMap.values()) {
            totalCount += item.getTotalCount();
        }
        boardArticleCountInfo.setTotalCount(totalCount);
    }

    public BoardArticleCountInfo getBoardArticleCountInfo() {
        return boardArticleCountInfo;
    }

    public void setBoardArticleCountInfo(BoardArticleCountInfo boardArticleCountInfo) {
        this.boardArticleCountInfo = boardArticleCountInfo;
    }

    public LinkedHashMap<String, ArticleCategoryArticleCountInfo> getArticleCategoryCountInfoMap() {
        return articleCategoryCountInfoMap;
    }

    public void setArticleCategoryCountInfoMap(LinkedHashMap<String, ArticleCategoryArticleCountInfo> articleCategoryCountInfoMap) {
        this.articleCategoryCountInfoMap = articleCategoryCountInfoMap;
    }
}
