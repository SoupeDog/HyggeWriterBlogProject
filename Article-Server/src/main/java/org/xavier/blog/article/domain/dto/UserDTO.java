package org.xavier.blog.article.domain.dto;

import org.xavier.blog.article.domain.enums.UserTypeEnum;

/**
 * 描述信息：<br/>
 * 用户 数据传输层对象
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
    private String uId;
    /**
     * 头像 url
     */
    private String headIcon;
    /**
     * 用户类型
     */
    private UserTypeEnum userType;
    /**
     * 用户昵称
     */
    private String uName;
    /**
     * 性别标识
     */
    private String sex;
    /**
     * 生日毫秒级时间戳
     */
    private Long birthday;
    /**
     * 个人简介
     */
    private String biography;
    /**
     * 经验值
     */
    private Integer exp;
    /**
     * 用户自定义配置
     */
    private String properties;
    /**
     * 注册毫秒级时间戳
     */
    private Long registerTs;
    /**
     * 最后修改毫秒级时间戳
     */
    private Long lastUpdateTs;

    public UserDTO() {
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public UserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(UserTypeEnum userType) {
        this.userType = userType;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public Long getRegisterTs() {
        return registerTs;
    }

    public void setRegisterTs(Long registerTs) {
        this.registerTs = registerTs;
    }

    public Long getLastUpdateTs() {
        return lastUpdateTs;
    }

    public void setLastUpdateTs(Long lastUpdateTs) {
        this.lastUpdateTs = lastUpdateTs;
    }
}