package org.xavier.blog.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.user.dao.GroupMapper;
import org.xavier.web.extend.DefaultService;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/4/11
 * @since Jdk 1.8
 */
@Service
public class GroupServiceImpl extends DefaultService {
    @Autowired
    GroupMapper groupMapper;



}
