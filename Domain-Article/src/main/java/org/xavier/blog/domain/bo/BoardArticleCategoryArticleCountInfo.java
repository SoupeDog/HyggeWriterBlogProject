package org.xavier.blog.domain.bo;

import java.util.ArrayList;
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
    private ArrayList<ArticleCategoryArticleCountInfo> articleCategoryCountInfoList = new ArrayList();

    public void initBoardTotalCount() {
        Integer totalCount = 0;
        for (ArticleCategoryArticleCountInfo item : articleCategoryCountInfoList) {
            totalCount += item.getTotalCount();
        }
        boardArticleCountInfo.setTotalCount(totalCount);
    }

    public void initArticleCategorySort() {
        articleCategoryCountInfoList.sort((o1, o2) -> {
            Integer articleCategoryId1 = o1.getArticleCategoryId();
            Integer articleCategoryId2 = o2.getArticleCategoryId();
            if (articleCategoryId1.equals(articleCategoryId2)) {
                return 0;
            } else if (articleCategoryId1 > articleCategoryId2) {
                return 1;
            } else {
                return -1;
            }
        });
    }

    public BoardArticleCountInfo getBoardArticleCountInfo() {
        return boardArticleCountInfo;
    }

    public void setBoardArticleCountInfo(BoardArticleCountInfo boardArticleCountInfo) {
        this.boardArticleCountInfo = boardArticleCountInfo;
    }

    public ArrayList<ArticleCategoryArticleCountInfo> getArticleCategoryCountInfoList() {
        return articleCategoryCountInfoList;
    }

    public void setArticleCategoryCountInfoList(ArrayList<ArticleCategoryArticleCountInfo> articleCategoryCountInfoList) {
        this.articleCategoryCountInfoList = articleCategoryCountInfoList;
    }
}
