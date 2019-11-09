package org.xavier.blog.user.domain.po.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.xavier.blog.user.domain.enums.UserSexEnum;
import org.xavier.blog.user.domain.enums.UserTypeEnum;
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
    private Integer id;
    /**
     * 账号
     */
    private String uId;
    /**
     * 密码
     */
    private String pw;
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
    private UserSexEnum sex;
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
     * 合法性标识
     */
    private Boolean legal_Flag;
    /**
     * 注册毫秒级时间戳
     */
    private Long registerTs;
    /**
     * 最后修改毫秒级时间戳
     */
    private Long lastUpdateTs;

    /**
     * 参数校验
     */
    public void validate() {
        PropertiesHelper propertiesHelper = UtilsCreator.getDefaultPropertiesHelperInstance();
        propertiesHelper.stringNotNull(uName, 1, 20, "[uName] can't be null,and its length should within 20.");
        propertiesHelper.stringNotNull(pw, 6, 20, "[pw] can't be null,and its length should be between 6~20.");
        propertiesHelper.string(properties, 0, 1000, "Length of [properties] should within 1000.");
        if (properties != null) {
            properties = UtilsCreator.getDefaultJsonHelperInstance(false).format(properties);
        }
    }

    /**
     * 初始化可空默认参数
     */
    public void init() {
        if (uId == null) {
            uId = "";
        }
        if (headIcon == null) {
            headIcon = "/img/headIcon/default.png";
        }
        if (userType == null) {
            userType = UserTypeEnum.READER;
        }
        if (sex == null) {
            sex = UserSexEnum.SECRET;
        }
        if (birthday == null) {
            birthday = 0L;
        }
        if (phone == null) {
            phone = "";
        }
        if (email == null) {
            email = "";
        }
        if (biography == null) {
            biography = "这家伙很懒，什么都没有留下。";
        }
        if (exp == null) {
            exp = 0;
        }
        if (properties == null) {
            properties = "";
        }
        if (legal_Flag == null) {
            legal_Flag = true;
        }
    }

    public User() {
    }

    public Integer getid() {
        return id;
    }

    public void setid(Integer id) {
        this.id = id;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
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

    public UserSexEnum getSex() {
        return sex;
    }

    public void setSex(UserSexEnum sex) {
        this.sex = sex;
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

    public Boolean getLegal_Flag() {
        return legal_Flag;
    }

    public void setLegal_Flag(Boolean legal_Flag) {
        this.legal_Flag = legal_Flag;
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
