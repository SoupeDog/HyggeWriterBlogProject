package org.xavier.blog.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 描述信息：<br/>
 * 应用启动后需要执行的 es 任务
 *
 * @author Xavier
 * @version 1.0
 * @date 2020/9/5
 * @since Jdk 1.8
 */

@Component
@Profile("!test")
public class ElasticCommand implements CommandLineRunner {
    @Autowired
    ESIndexCheckTask esIndexCheckTask;
    @Autowired
    CopyArticleToESTask copyArticleToESTask;

    @Override
    public void run(String... args) throws Exception {
        // 初始化索引检测
        esIndexCheckTask.doInitIndex();
        // 拷贝 mysql Article 数据到 es
        copyArticleToESTask.start(10);
    }
}