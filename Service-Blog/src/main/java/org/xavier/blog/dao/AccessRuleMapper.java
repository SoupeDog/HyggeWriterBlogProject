package org.xavier.blog.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.xavier.blog.domain.po.AccessRule;

import java.util.ArrayList;

/**
 * 描述信息：<br/>
 * 访问许可操作
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/4/5
 * @since Jdk 1.8
 */
@Mapper
public interface AccessRuleMapper {

    /**
     * 根据 articleCategoryNo 查询文章类别信息
     *
     * @param articleCategoryNo 文章类别唯一标识
     * @return 查询结果
     */
    @Select("select accessRuleId,articleCategoryId,articleCategoryNo,requirement,accessPermit,extendProperties,createTs,lastUpdateTs from `accessRule` where articleCategoryNo=#{articleCategoryNo}")
    ArrayList<AccessRule> queryAccessRuleByArticleCategoryNo(@Param("articleCategoryNo") String articleCategoryNo);

    ArrayList<AccessRule> queryAccessRuleByArticleCategoryNoMultiple(@Param("articleCategoryNoList") ArrayList<String> articleCategoryNoList);
}