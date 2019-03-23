package org.xavier.blog.user.domain.bo;

import org.apache.logging.log4j.core.util.CronExpression;
import org.xavier.blog.user.domain.enums.UserSexEnum;
import org.xavier.blog.user.domain.po.article.ArticleCategory;
import org.xavier.blog.user.domain.po.user.User;
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
    private User user;
    private ArrayList<String> groupInfo;
    private String secretKey;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<String> getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(ArrayList<String> groupInfo) {
        this.groupInfo = groupInfo;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Boolean chekPromission(ArticleCategory articleCategory) {
        Boolean result = false;
        switch (articleCategory.getAccessPermit()) {
            case PUBLIC:
                result = true;
                break;
            case MALE:
                if (user.getSex() == UserSexEnum.MALE) {
                    result = true;
                }
                break;
            case FEMALE:
                if (user.getSex() == UserSexEnum.FEMALE) {
                    result = true;
                }
                break;
            case GROUP:
                if (groupInfo.contains(articleCategory.getExtendProperties())) {
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
