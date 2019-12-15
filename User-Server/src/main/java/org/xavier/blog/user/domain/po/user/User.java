package org.xavier.blog.user.domain.po.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.xavier.blog.common.enums.UserSexEnum;
import org.xavier.blog.common.enums.UserStateEnum;
import org.xavier.blog.common.enums.UserTypeEnum;
import org.xavier.common.util.PropertiesHelper;
import org.xavier.common.util.UtilsCreator;

/**
 * 描述信息：<br/>
 * 用户对象
 *
 * @author Xavier
 * @version 1.0
 * @date 2017/9/30
 * @since Jdk 1.8
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    /**
     * 唯一标示
     */
    private Integer userId;
    /**
     * 账号
     */
    private String uid;
    /**
     * 用户类型
     */
    private UserTypeEnum userType;
    /**
     * 密码
     */
    private String pw;
    /**
     * 用户昵称
     */
    private String userName;
    /**
     * 头像 url
     */
    private String userAvatar;
    /**
     * 性别标识
     */
    private UserSexEnum sex;
    /**
     * 个人简介
     */
    private String biography;
    /**
     * 生日毫秒级时间戳
     */
    private Long birthday;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 联系邮箱
     */
    private String email;
    /**
     * 用户状态
     */
    private UserStateEnum userState;
    /**
     * 注册毫秒级时间戳
     */
    private Long createTs;
    /**
     * 最后修改毫秒级时间戳
     */
    private Long lastUpdateTs;

    public void validate() {
        PropertiesHelper propertiesHelper = UtilsCreator.getDefaultPropertiesHelperInstance();
        propertiesHelper.stringNotNull(pw, 6, 50, "[pw] can't be null,and it should be a string[6,50].");
        propertiesHelper.stringNotNull(userName, 1, 50, "[pw] can't be null,and it should be a string[1,50].");
    }

    public void init(Long currentTs) {
        PropertiesHelper propertiesHelper = UtilsCreator.getDefaultPropertiesHelperInstance();
        this.userType = UserTypeEnum.NORMAL;
        this.userAvatar = propertiesHelper.stringOfNullable(this.userAvatar, "默认头像.png", 1, 200, "[userAvatar] should be a string[1,200]");
        if (this.sex == null) {
            this.sex = UserSexEnum.SECRET;
        }
        this.biography = propertiesHelper.stringOfNullable(this.biography, "这家伙很懒，什么都没留下~", 0, 200, "[biography] should be a string[1,200]");
        this.createTs = currentTs;
        this.lastUpdateTs = currentTs;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public UserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(UserTypeEnum userType) {
        this.userType = userType;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public UserSexEnum getSex() {
        return sex;
    }

    public void setSex(UserSexEnum sex) {
        this.sex = sex;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserStateEnum getUserState() {
        return userState;
    }

    public void setUserState(UserStateEnum userState) {
        this.userState = userState;
    }

    public Long getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Long createTs) {
        this.createTs = createTs;
    }

    public Long getLastUpdateTs() {
        return lastUpdateTs;
    }

    public void setLastUpdateTs(Long lastUpdateTs) {
        this.lastUpdateTs = lastUpdateTs;
    }
}
