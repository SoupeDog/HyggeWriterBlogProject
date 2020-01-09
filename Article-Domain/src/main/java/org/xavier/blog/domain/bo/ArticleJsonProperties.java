package org.xavier.blog.domain.bo;

import org.xavier.common.util.PropertiesHelper;
import org.xavier.common.util.UtilsCreator;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 19-12-28
 * @since Jdk 1.8
 */
public class ArticleJsonProperties {
    /**
     * 背景图片地址
     */
    private String bgi;

    /**
     * 背景音乐
     */
    private BgmConfig bgmConfig;

    public void setDefaultConfigIfAbsent() {
        PropertiesHelper propertiesHelper = UtilsCreator.getDefaultPropertiesHelperInstance();
        this.bgi = propertiesHelper.stringOfNullable(bgi, "https://s1.ax2x.com/2018/10/24/5XWiJq.jpg");
        if (bgmConfig != null) {
            bgmConfig.setDefaultConfigIfAbsent();
        } else {
            BgmConfig temp = new BgmConfig();
            temp.setDefaultConfigIfAbsent();
            this.bgmConfig = temp;
        }
    }

    public String getBgi() {
        return bgi;
    }

    public void setBgi(String bgi) {
        this.bgi = bgi;
    }

    public BgmConfig getBgmConfig() {
        return bgmConfig;
    }

    public void setBgmConfig(BgmConfig bgmConfig) {
        this.bgmConfig = bgmConfig;
    }
}