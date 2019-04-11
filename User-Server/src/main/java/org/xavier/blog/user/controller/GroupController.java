package org.xavier.blog.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.xavier.blog.common.HyggeWriterController;
import org.xavier.blog.user.domain.bo.GroupValidateBO;
import org.xavier.blog.user.service.GroupJoinRecordServiceImpl;
import org.xavier.web.annotation.EnableControllerLog;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/4/11
 * @since Jdk 1.8
 */
@CrossOrigin
@RestController
public class GroupController extends HyggeWriterController {

}