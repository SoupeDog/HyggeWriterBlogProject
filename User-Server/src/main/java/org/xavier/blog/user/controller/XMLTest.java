//package org.xavier.blog.user.controller;
//
//import com.fasterxml.jackson.dataformat.xml.XmlMapper;
//import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
//import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
//import org.dom4j.Document;
//import org.dom4j.DocumentException;
//import org.dom4j.io.SAXReader;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//
///**
// * 描述信息：<br/>
// *
// * @author Xavier
// * @version 1.0
// * @date 2019/3/19
// * @since Jdk 1.8
// */
//
//@RestController
//public class XMLTest {
//
//
//    @PostMapping(value = "/xml")
//    public Object test(@RequestBody WXResponse asd) throws IOException {
////        XmlMapper xmlMapper = new XmlMapper();
////        WXResponse wxResponse = xmlMapper.readValue(asd, WXResponse.class);
//        System.out.println(asd);
//        return asd;
//    }
//}
