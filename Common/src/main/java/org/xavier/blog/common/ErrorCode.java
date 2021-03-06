package org.xavier.blog.common;

/**
 * 描述信息：<br/>
 * 公用错误码
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/1/14
 * @since Jdk 1.8
 */
public enum ErrorCode {
    TARGET_DATA_EMPTY(400001),

    UPDATE_DATA_EMPTY(400002),

    UNEXPECTED_TOKEN(403001),
    TOKEN_OVERDUE(403002),
    UNEXPECTED_TOKEN_REFRESH_KEY(403003),
    UNEXPECTED_TOKEN_SCOPE(403004),
    UNEXPECTED_PASSWORD_OR_UID(403005),

    INSUFFICIENT_PERMISSIONS(403010),
    OWNERS_OF_TARGETS_WERE_NOT_UNIQUE(403020),
    TASK_IS_RUNNING(403030),


    USER_NOTFOUND(404010),
    ARTICLE_NOTFOUND(404020),
    BOARD_NOTFOUND(404030),
    STATEMENT_NOTFOUND(404040),
    ARTICLECATEGORY_NOTFOUND(404050),
    GROUP_NOTFOUND(404060),
    TOKEN_NOTFOUND(404001),

    USER_EXISTS(409010),
    ARTICLE_EXISTS(409020),
    BOARD_EXISTS(409030),
    STATEMENT_EXISTS(409040),
    ARTICLECATEGORY_EXISTS(409050),
    GROUP_JOIN_CORD_EXISTS(409060),
    GROUP_EXISTS(409070),


    TOKEN_REFRESH_CONFLICT(409001),
    TOKEN_CREATE_CONFLICT(409002),
    USER_DELETE_CONFLICT(409010),
    USER_UPDATE_CONFLICT(409011),
    ARTICLE_DELETE_CONFLICT(409021),
    ARTICLE_UPDATE_CONFLICT(409022),
    BOARD_DELETE_CONFLICT(409031),
    STATEMENT_DELETE_CONFLICT(409041),
    STATEMENT_UPDATE_CONFLICT(409042),
    ARTICLE_CATEGORY_DELETE_CONFLICT(409051),
    ARTICLE_CATEGORY_UPDATE_CONFLICT(409052),
    GROUP_DELETE_CONFLICT(409071),
    GROUP_UPDATE_CONFLICT(409072),


    DATEBASE_FALL_TO_SAVE(500010),
    DATEBASE_FALL_TO_UPDATE(500020),
    DATEBASE_FALL_TO_DELETE(500030),
    REQUEST_FALL_TO_CALL_UPSTREAM_SERVICES(500040),
    FAIL_TO_UPLOAD_FILE(500050),
    FAIL_TO_QUERY_FILE(500060),
    FAIL_TO_DELETE_FILE(500070);


    private Integer errorCod;

    ErrorCode(Integer errorCod) {
        this.errorCod = errorCod;
    }

    public Integer getErrorCod() {
        return errorCod;
    }

    public void setErrorCod(Integer errorCod) {
        this.errorCod = errorCod;
    }
}
