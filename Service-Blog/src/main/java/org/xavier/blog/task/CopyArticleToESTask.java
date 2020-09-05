package org.xavier.blog.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.dao.ArticleMapper;
import org.xavier.blog.domain.po.Article;
import org.xavier.blog.task.bo.ArticleForElasticSearch;
import org.xavier.blog.task.bo.CopyArticleToESTaskResult;
import org.xavier.common.exception.Universal403RuntimeException;
import org.xavier.common.logging.core.HyggeLogger;
import org.xavier.common.util.CollectionHelper;
import org.xavier.common.util.TimeHelper;
import org.xavier.common.util.UtilsCreator;
import org.xavier.common.util.enums.TimeFormatEnum;

import java.util.ArrayList;

/**
 * 描述信息：<br/>
 * 数据库文章同步到 ES 任务
 *
 * @author Xavier
 * @version 1.0
 * @date 2020/9/4
 * @since Jdk 1.8
 */
@Service
public class CopyArticleToESTask {
    /**
     * 默认单批次拉取数据库文章数量
     */
    public static final Integer DEFAULT_BATCH_SIZE = 10;
    /**
     * true 表示正在执行
     */
    private static volatile Boolean runningFlag = false;
    /**
     * 上次开始时间
     */
    private static volatile Long lastStartTs;
    /**
     * 上次结束时间
     */
    private static volatile Long lastEndTs;
    /**
     * 开始时间
     */
    private static volatile Long startTs;
    /**
     * 上一次记录同步记录
     */
    private static volatile CopyArticleToESTaskResult lastInfo;


    private static TimeHelper timeHelper = UtilsCreator.getDefaultTimeHelperInstance();

    private static CollectionHelper collectionHelper = UtilsCreator.getDefaultCollectionHelperInstance();

    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    HyggeLogger logger;

    public CopyArticleToESTaskResult start(Integer batchSize) {
        CopyArticleToESTaskResult info = null;
        if (runningFlag) {
            throw new Universal403RuntimeException(ErrorCode.ARTICLE_CATEGORY_DELETE_CONFLICT.getErrorCod(), String.format("CopyArticleToESTask is running,start at %s", timeHelper.format(startTs, TimeFormatEnum.yyyy_MM_dd_HH_mm_ss)));
        }
        synchronized (this) {
            runningFlag = true;
            startTs = System.currentTimeMillis();
            info = doCopy();
            lastEndTs = info.getEndTs();
            lastStartTs = startTs;
            startTs = null;
            runningFlag = false;
            logger.always("CopyArticleToESTask Finished：" + UtilsCreator.getDefaultJsonHelperInstance(false).format(info));
            lastInfo = info;
        }
        return info;
    }

    private CopyArticleToESTaskResult doCopy() {
        CopyArticleToESTaskResult result = new CopyArticleToESTaskResult();
        result.setStartTs(startTs);
        Integer totalArticleCount = articleMapper.queryArticleTotalCount();
        result.setTotalArticleCount(totalArticleCount);
        Integer remain = totalArticleCount;
        Integer currentPage = 1;
        Integer successCount = 0;
        while (remain > 0) {
            int actualSize = remain > DEFAULT_BATCH_SIZE ? DEFAULT_BATCH_SIZE : remain;
            ArrayList<Article> articleList = articleMapper.queryArticleByPage((currentPage - 1) * DEFAULT_BATCH_SIZE, actualSize);
            ArrayList<ArticleForElasticSearch> articleForElasticSearch = collectionHelper.filterCollectionNotEmptyAsArrayList(false, articleList, "Article can‘t be null.",
                    article -> new ArticleForElasticSearch(article));
            if (articleForElasticSearch.size() > 0) {
                elasticsearchRestTemplate.save(articleForElasticSearch);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                result.setSuccessCount(successCount);
                result.setFailArticleNo(collectionHelper.filterCollectionNotEmptyAsArrayList(true, articleList, "Article can't be null.",
                        (article) -> article.getArticleNo()));
                logger.error("CopyArticleToESTask Error：" + UtilsCreator.getDefaultJsonHelperInstance(false).format(result), e);
            }
            remain = remain - actualSize;
            successCount = successCount + actualSize;
            currentPage = currentPage + 1;
        }
        result.setSuccessCount(successCount);
        result.setEndTs(System.currentTimeMillis());
        return result;
    }

    public static CopyArticleToESTaskResult getLastInfo() {
        return lastInfo;
    }

    public static Boolean isRunning() {
        return runningFlag;
    }
}