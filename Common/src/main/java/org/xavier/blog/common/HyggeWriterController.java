package org.xavier.blog.common;


import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.xavier.webtoolkit.base.DefaultController;
import org.xavier.webtoolkit.domain.ErrorResult;

import java.nio.charset.Charset;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 2018.07.27
 * @since Jdk 1.8
 */
public class HyggeWriterController extends DefaultController {
    //请求类型不对
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public ResponseEntity<?> requestHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        logger.warn("Unsupported Media Type.", e);
        return this.fail(HttpStatus.UNSUPPORTED_MEDIA_TYPE, 415, "Unsupported Media Type.");
    }

    //请求内容不能解析
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<?> requestHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.warn("HttpMessageNotReadableException.", e);
        return this.fail(HttpStatus.BAD_REQUEST, 400, "Bad request.");
    }

    //请求内容不能解析
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<?> requestHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        logger.warn("Method Not Allowed", e);
        return this.fail(HttpStatus.METHOD_NOT_ALLOWED, 405, "HttpRequestMethodNotSupportedException.");
    }

    //参数类型不匹配
    @ExceptionHandler({TypeMismatchException.class})
    public ResponseEntity<?> requestTypeMismatch(TypeMismatchException e) {
        logger.warn("TypeMismatchException", e);
        return this.fail(HttpStatus.BAD_REQUEST, 400, "TypeMismatchException.");
    }

    //参数类型不匹配
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> requestMethodArgumentNotValid(MethodArgumentNotValidException e) {
        logger.warn("MethodArgumentNotValidException", e);
        return this.fail(HttpStatus.BAD_REQUEST, 400, "MethodArgumentNotValidException.");
    }

    @Override
    protected Object successHook(Object object) {
        GatewayResponse result = GatewayResponse.GatewayResponseBuilder.buildSuccessGatewayResponse(object);
        return result;
    }

    @Override
    protected HttpStatus getFailHttpStatus(HttpStatus status, Number errorCode, String msg) {
        return HttpStatus.OK;
    }

    @Override
    protected Object failHook(ErrorResult errorResult) {
        GatewayResponse result = GatewayResponse.GatewayResponseBuilder.buildFailGatewayResponse(2, errorResult.getErrorCode(), errorResult.getMsg());
        return result;
    }
}