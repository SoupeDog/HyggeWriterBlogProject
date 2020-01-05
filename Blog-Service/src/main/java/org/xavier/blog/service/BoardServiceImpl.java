package org.xavier.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.dao.BoardMapper;
import org.xavier.blog.domain.po.Board;
import org.xavier.common.enums.ColumnType;
import org.xavier.common.exception.Universal404Exception;
import org.xavier.common.util.bo.ColumnInfo;
import org.xavier.webtoolkit.base.DefaultUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 19-12-31
 * @since Jdk 1.8
 */
@Service
public class BoardServiceImpl extends DefaultUtils {
    @Autowired
    BoardMapper boardMapper;
    @Autowired
    UserServiceImpl userService;

    private static final List<ColumnInfo> checkInfo = new ArrayList<ColumnInfo>() {{
        add(new ColumnInfo("boardName", "boardName", ColumnType.STRING, false, 1, 32));
        add(new ColumnInfo("uid", "uid", ColumnType.STRING, false, 1, 32));
    }};

    public Board queryBoardByBoardNoNotNull(String boardNo) throws Universal404Exception {
        Board result = queryBoardByBoardNo(boardNo);
        if (result == null) {
            throw new Universal404Exception(ErrorCode.BOARD_NOTFOUND.getErrorCod(), "Board(" + boardNo + ") was not found.");
        }
        return result;
    }

    public Board queryBoardByBoardNo(String boardNo) {
        propertiesHelper.stringNotNull(boardNo, 0, 32, "[boardNo] can't be null,and it should be a string[0,32].");
        ArrayList<Board> queryResult = boardMapper.queryBoardListByBoardNo(new ArrayList<String>(1) {{
            add(boardNo);
        }});
        if (queryResult.size() > 0) {
            return queryResult.get(0);
        } else {
            return null;
        }
    }

    /**
     * 查询所有板块信息
     */
    public ArrayList<Board> queryAllBoardList(Integer currentPage, Integer pageSize, String orderKey, Boolean isDESC) {
        propertiesHelper.intRangeNotNull(currentPage, 1, Integer.MAX_VALUE, "[currentPage] should be a int number more than 0.");
        propertiesHelper.intRangeNotNull(pageSize, 1, Integer.MAX_VALUE, "[size] should be a int number more than 0.");
        ArrayList<Board> result;
        // 过滤排序关键字
        switch (orderKey) {
            case "ts":
                break;
            default:
                orderKey = "ts";
        }
        if (isDESC) {
            result = boardMapper.queryAllBoardList((currentPage - 1) * pageSize, pageSize, orderKey, "DESC");
        } else {
            result = boardMapper.queryAllBoardList((currentPage - 1) * pageSize, pageSize, orderKey, "ASC");
        }
        return result;
    }
}