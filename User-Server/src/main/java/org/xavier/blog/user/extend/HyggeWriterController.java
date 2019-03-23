package org.xavier.blog.user.extend;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.xavier.web.extend.DefaultController;
import org.xavier.web.extend.GatewayResponse;

import java.nio.charset.Charset;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 2018.07.27
 * @since Jdk 1.8
 */
@CrossOrigin
public class HyggeWriterController extends DefaultController {
    //请求类型不对
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public ResponseEntity<?> requestHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        logger.warn("Unsupported Media Type.", e);
        return this.fail(HttpStatus.UNSUPPORTED_MEDIA_TYPE, 415F, "Unsupported Media Type.");
    }

    //请求内容不能解析
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<?> requestHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.warn("HttpMessageNotReadableException.", e);
        return this.fail(HttpStatus.BAD_REQUEST, 400F, "Bad request.");
    }

    //请求内容不能解析
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<?> requestHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        logger.warn("Method Not Allowed", e);
        return this.fail(HttpStatus.METHOD_NOT_ALLOWED, 405F, "HttpRequestMethodNotSupportedException.");
    }

    //参数类型不匹配
    @ExceptionHandler({TypeMismatchException.class})
    public ResponseEntity<?> requestTypeMismatch(TypeMismatchException e) {
        logger.warn("TypeMismatchException", e);
        return this.fail(HttpStatus.BAD_REQUEST, 400F, "TypeMismatchException.");
    }

    //参数类型不匹配
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> requestMethodArgumentNotValid(MethodArgumentNotValidException e) {
        logger.warn("MethodArgumentNotValidException", e);
        return this.fail(HttpStatus.BAD_REQUEST, 400F, "MethodArgumentNotValidException.");
    }

    @Override
    public ResponseEntity<?> success() {
        MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(HttpStatus.OK);
        builder.contentType(mediaType);
        GatewayResponse result = new GatewayResponse();
        result.setType((byte) 1);
        result.setCode(200F);
        result.setTs(System.currentTimeMillis());
        return builder.body(result);
    }

    @Override
    public ResponseEntity<?> success(Object entity) {
        MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(HttpStatus.OK);
        GatewayResponse result = new GatewayResponse();
        builder.contentType(mediaType);
        result.setType((byte) 1);
        result.setCode(200F);
        result.setData(entity);
        result.setTs(System.currentTimeMillis());
        return builder.body(result);
    }

    @Override
    public ResponseEntity<?> fail(HttpStatus status, Float errorCode, String msg) {
        MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(status);
        builder.contentType(mediaType);
        GatewayResponse result = new GatewayResponse();
        result.setType((byte) 2);
        result.setCode(errorCode);
        result.setMsg(msg);
        result.setTs(System.currentTimeMillis());
        return builder.body(result);
    }


}
