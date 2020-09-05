package org.xavier.blog.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.stereotype.Service;
import org.xavier.blog.task.bo.ArticleForElasticSearch;
import org.xavier.common.logging.core.HyggeLogger;

/**
 * 描述信息：<br/>
 * ES 索引初始化
 *
 * @author Xavier
 * @version 1.0
 * @date 2020/9/4
 * @since Jdk 1.8
 */
@Service
public class ESIndexCheckTask {
    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    HyggeLogger logger;

    public void doInitIndex() {
        // 获取索引索配置
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(ArticleForElasticSearch.class);
        if (!indexOperations.exists()) {
            // 创建索引
            indexOperations.create();
            // 配置映射
            indexOperations.putMapping(indexOperations.createMapping());
            logger.always("ArticleForElasticSearch 索引不存在，开始创建");
        }
    }
}