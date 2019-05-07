package org.xavier.blog.article.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.article.dao.StatementMapper;
import org.xavier.blog.article.domain.po.statement.Statement;
import org.xavier.web.extend.DefaultService;

/**
 * 描述信息：<br/>
 * 版权声明 Service 实现类
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/5/7
 * @since Jdk 1.8
 */
@Service
public class StatementServiceImpl extends DefaultService {
    @Autowired
    StatementMapper statementMapper;

//    public Boolean saveStatement(Statement statement) {
//
//    }


}