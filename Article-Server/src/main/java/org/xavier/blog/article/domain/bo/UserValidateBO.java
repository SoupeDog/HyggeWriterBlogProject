package org.xavier.blog.article.domain.bo;

import org.apache.logging.log4j.core.util.CronExpression;
import org.xavier.blog.article.domain.dto.UserDTO;
import org.xavier.blog.article.domain.enums.UserSexEnum;
import org.xavier.blog.article.domain.po.article.ArticleCategory;
import org.xavier.common.exception.Universal_500_X_Exception_Runtime;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * 描述信息：<br/>
 * 用户身份校验业务对象
 *
 * @author Xavier
 * @version 1.0
 * @date 2018.09.04
 * @since Jdk 1.8
 */
public class UserValidateBO {
    private UserDTO user;
    private ArrayList<String> groupIdList;
    private String secretKey;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public ArrayList<String> getGroupIdList() {
        return groupIdList;
    }

    public void setGroupIdList(ArrayList<String> groupIdList) {
        this.groupIdList = groupIdList;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Boolean chekPromission(ArticleCategory articleCategory) {
        if (articleCategory.getuId().equals(user.getuId())) {
            return true;
        }
        Boolean result = false;
        switch (articleCategory.getAccessPermit()) {
            case PUBLIC:
                result = true;
                break;
            case MALE:
                if (UserSexEnum.MALE.getDescription().equals(user.getSex())) {
                    result = true;
                }
                break;
            case FEMALE:
                if (UserSexEnum.FEMALE.getDescription().equals(user.getSex())) {
                    result = true;
                }
                break;
            case GROUP:
                if (groupIdList != null && groupIdList.contains(articleCategory.getExtendProperties())) {
                    result = true;
                }
                break;
            case SECRET_KEY:
                if (articleCategory.getExtendProperties().equals(secretKey)) {
                    result = true;
                }
                break;
            case PERSONAL:
                if (articleCategory.getExtendProperties().equals(user.getuId())) {
                    result = true;
                }
                break;
            case CRON:
                try {
                    CronExpression cronExpression = new CronExpression(articleCategory.getExtendProperties());
                    result = cronExpression.isSatisfiedBy(new Date());
                } catch (ParseException e) {
                    throw new Universal_500_X_Exception_Runtime(555F, "Unexpected cron expression(" + articleCategory.getExtendProperties() + ").");
                }
                break;
            default:
        }
        return result;
    }
}