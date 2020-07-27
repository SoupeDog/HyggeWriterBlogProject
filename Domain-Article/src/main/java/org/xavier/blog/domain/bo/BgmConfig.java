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
public class BgmConfig {
    /**
     * 0 本地音乐 1 网易云音乐外链
     */
    private Integer bgmType;
    /**
     * 音乐路径
     */
    private String src;
    /**
     * 音乐封面
     */
    private String cover;
    /**
     * 歌曲名
     */
    private String name;
    /**
     * 艺术家
     */
    private String artist;
    /**
     * 歌词文件路径
     */
    private String lrc;

    /**
     * 是否启用 0 禁用 1 启用
     */
    private Boolean state;

    public void setDefaultConfigIfAbsent() {
        PropertiesHelper propertiesHelper = UtilsCreator.getDefaultPropertiesHelperInstance();
        this.state = propertiesHelper.booleanFormatOfNullable(state, false, "[state] should be a boolean.");
        if (state) {
            propertiesHelper.intRangeNotNull(bgmType, "[bgmType] can't be null,and it should be a integer number.");
            propertiesHelper.stringNotNull(src, "[src] can't be null,and it should be a string.");
        }
    }

    public Integer getBgmType() {
        return bgmType;
    }

    public void setBgmType(Integer bgmType) {
        this.bgmType = bgmType;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }
}