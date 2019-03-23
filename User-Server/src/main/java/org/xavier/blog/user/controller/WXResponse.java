//package org.xavier.blog.user.controller;
//
//import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
//import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
//
///**
// * 描述信息：<br/>
// *
// * @author Xavier
// * @version 1.0
// * @date 2019/3/19
// * @since Jdk 1.8
// */
//@JacksonXmlRootElement(localName = "xml")
//public class WXResponse {
//
//    @JacksonXmlProperty(localName = "ToUserName")
//    private String ToUserName;
//    @JacksonXmlProperty(localName = "FromUserName")
//    private String FromUserName;
//    @JacksonXmlProperty(localName = "CreateTime")
//    private Long CreateTime;
//    @JacksonXmlProperty(localName = "MsgType")
//    private String MsgType;
//    @JacksonXmlProperty(localName = "Content")
//    private String Content;
//    @JacksonXmlProperty(localName = "MsgId")
//    private String MsgId;
//
//    public String getToUserName() {
//        return ToUserName;
//    }
//
//    public void setToUserName(String toUserName) {
//        ToUserName = toUserName;
//    }
//
//    public String getFromUserName() {
//        return FromUserName;
//    }
//
//    public void setFromUserName(String fromUserName) {
//        FromUserName = fromUserName;
//    }
//
//    public Long getCreateTime() {
//        return CreateTime;
//    }
//
//    public void setCreateTime(Long createTime) {
//        CreateTime = createTime;
//    }
//
//    public String getMsgType() {
//        return MsgType;
//    }
//
//    public void setMsgType(String msgType) {
//        MsgType = msgType;
//    }
//
//    public String getContent() {
//        return Content;
//    }
//
//    public void setContent(String content) {
//        Content = content;
//    }
//
//    public String getMsgId() {
//        return MsgId;
//    }
//
//    public void setMsgId(String msgId) {
//        MsgId = msgId;
//    }
//}
