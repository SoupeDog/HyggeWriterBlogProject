package org.xavier.blog.domain.dto.group;

/**
 * 描述信息：<br/>
 * 群组
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/4/5
 * @since Jdk 1.8
 */
public class GroupDTO {
    /**
     * 组唯一标识
     */
    private String gId;
    /**
     * 群主
     */
    private String groupOwner;
    /**
     * 组名
     */
    private String groupName;
    /**
     * 创建时间 utc 毫秒级时间戳
     */
    private Long ts;


}