package org.xavier.blog.domain.dto.user;


import org.xavier.blog.common.enums.UserSexEnum;
import org.xavier.blog.common.enums.UserStateEnum;
import org.xavier.blog.common.enums.UserTypeEnum;
import org.xavier.blog.domain.po.User;

/**
 * 描述信息：<br/>
 * 用户数据传输层对象
 *
 * @author Xavier
 * @version 1.0
 * @date 2018.01.15
 * @since Jdk 1.8
 */
public class UserDTO {
    /**
     * 账号
     */
    private String uid;
    /**
     * 用户类型
     */
    private UserTypeEnum userType;
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

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.uid = user.getUid();
        this.userType = user.getUserType();
        this.userName = user.getUserName();
        this.userAvatar = user.getUserAvatar();
        this.sex = user.getSex();
        this.biography = user.getBiography();
        this.birthday = user.getBirthday();
        this.email = user.getEmail();
        this.userState = user.getUserState();
        this.createTs = user.getCreateTs();
        this.lastUpdateTs = user.getLastUpdateTs();
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