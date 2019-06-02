package org.xavier.blog.article.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.article.dao.BoardMapper;
import org.xavier.blog.article.domain.dto.BoardDTO;
import org.xavier.blog.article.domain.po.board.Board;
import org.xavier.blog.article.service.remote.UserServiceImpl;
import org.xavier.blog.common.ErrorCode;
import org.xavier.common.enums.ColumnType;
import org.xavier.common.exception.Universal_403_X_Exception;
import org.xavier.common.exception.Universal_404_X_Exception;
import org.xavier.common.logging.HyggeLoggerMsgBuilder;
import org.xavier.common.utils.UtilsCreator;
import org.xavier.common.utils.bo.ColumnInfo;
import org.xavier.web.extend.DefaultService;

import java.util.*;

/**
 * 描述信息：<br/>
 * 板块 Service 实现类
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/5/17
 * @since Jdk 1.8
 */
@Service
public class BoardServiceImpl extends DefaultService {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    BoardMapper boardMapper;

    private static final List<ColumnInfo> checkInfo = new ArrayList<ColumnInfo>() {{
        add(new ColumnInfo(ColumnType.STRING, "uId", "uId", false, 9, 10));
        add(new ColumnInfo(ColumnType.STRING, "boardName", "boardName", false, 1, 60));
    }};

    /**
     * 添加板块
     */
    public Boolean saveBoard(String operatorUId, Board board, Long currentTs) {
        board.setBoardId(UtilsCreator.getInstance_DefaultRandomHelper().getUUID());
        board.setuId(operatorUId);
        board.setTs(currentTs);
        board.setLastUpdateTs(currentTs);
        board.setLegal_Flag(true);
        Integer saveBoard_affectedLine = boardMapper.saveBoard(board);
        Boolean saveBoard_Flag = saveBoard_affectedLine == 1;
        if (!saveBoard_Flag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("saveBoard_EffectedLine", "1", saveBoard_affectedLine, board));
        }
        return saveBoard_Flag;
    }


    /**
     * 批量删除板块
     */
    public Boolean removeBoard_Multiple(String operatorUId, ArrayList<String> boardIdList, Long upTs) throws Universal_403_X_Exception {
        ArrayList<String> boardIdListForQuery = listHelper.filterStringListNotEmpty(boardIdList, "boardIdList", 32, 32);
        ArrayList<Board> boardList = boardMapper.queryBoardListByBoardIds(boardIdListForQuery);
        String firstUId = null;
        // 删除默认值为成功
        Boolean removeBoard_Flag = true;
        ArrayList<String> boardIdListForRemove = new ArrayList();
        for (Board temp : boardList) {
            if (firstUId == null) {
                firstUId = temp.getuId();
            }
            if (!firstUId.equals(temp.getuId())) {
                throw new Universal_403_X_Exception(ErrorCode.OWNERS_OF_TARGETS_WERE_NOT_UNIQUE.getErrorCod(), "Owners of targets were not unique.");
            }
            boardIdListForRemove.add(temp.getBoardId());
        }
        if (firstUId != null && boardIdListForRemove.size() > 0) {
            userService.checkRight(operatorUId, firstUId);
            Integer removeBoard_affectedLine = boardMapper.removeBoardMultipleByBoardIds_Logically(boardIdListForRemove, upTs);
            removeBoard_Flag = removeBoard_affectedLine == boardIdListForRemove.size();
            if (!removeBoard_Flag) {
                logger.warn(HyggeLoggerMsgBuilder.assertFail("removeBoard_affectedLine", String.valueOf(boardIdListForRemove.size()), removeBoard_affectedLine, new LinkedHashMap() {{
                    put("operatorUId", operatorUId);
                    put("boardIdList", boardIdListForRemove);
                    put("upTs", upTs);
                }}));
            }
        }
        return removeBoard_Flag;
    }


    /**
     * 修改版权声明
     */
    public Boolean updateBoard(String operatorUId, String boardId, Map rowData) throws Universal_404_X_Exception, Universal_403_X_Exception {
        propertiesHelper.stringNotNull(boardId, 32, 32, "[boardId] can't be null,and its length should be 32.");
        Board board = quarryBoardByBoardId(boardId);
        if (board == null) {
            throw new Universal_404_X_Exception(ErrorCode.STATEMENT_NOTFOUND.getErrorCod(), "Board(" + boardId + ") was not found.");
        }
        userService.checkRight(operatorUId, board.getuId());
        HashMap data = sqlHelper.createFinalUpdateDataWithTimeStamp(rowData, checkInfo, LASTUPDATETS);
        mapHelper.mapNotEmpty(data, "Effective Update-Info was null.");
        Long upTs = propertiesHelper.longRangeNotNull(rowData.get("ts"), "[ts] can't be null,and it should be a number.");
        Integer updateBoard_affectedLine = boardMapper.updateBoard(boardId, data, upTs);
        Boolean updateBoard_Flag = updateBoard_affectedLine == 1;
        if (!updateBoard_Flag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("updateBoard_affectedLine", "1", updateBoard_affectedLine, new LinkedHashMap() {{
                put("operatorUId", operatorUId);
                put("statementId", boardId);
                put("data", data);
                put("upTs", upTs);
            }}));
        }
        return updateBoard_Flag;
    }

    /**
     * 根据版权唯一标识查询板块
     */
    public Board quarryBoardByBoardId(String boardId) {
        propertiesHelper.stringNotNull(boardId, 32, 32, "[boardId] can't be null,and its length should be 32.");
        ArrayList<Board> resultTemp = boardMapper.queryBoardListByBoardIds(listHelper.createSingleList(boardId));
        if (resultTemp == null || resultTemp.size() < 1) {
            return null;
        } else {
            return resultTemp.get(0);
        }
    }

    /**
     * 根据版权唯一标识查询板块
     */
    public Board quarryBoardByBoardId__WithExistValidate(String boardId) throws Universal_404_X_Exception {
        Board result = quarryBoardByBoardId(boardId);
        if (result == null) {
            throw new Universal_404_X_Exception(ErrorCode.BOARD_NOTFOUND.getErrorCod(), "Board(" + boardId + ") was not found.");
        }
        return result;
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
            result = boardMapper.queryAllBoardList((currentPage - 1) * pageSize, pageSize, orderKey, DESC);
        } else {
            result = boardMapper.queryAllBoardList((currentPage - 1) * pageSize, pageSize, orderKey, ASC);
        }
        return result;
    }


    public ArrayList<BoardDTO> boardToBoardDTO(ArrayList<Board> target) {
        ArrayList<BoardDTO> result = new ArrayList(target.size());
        BoardDTO dtoTemp;
        for (Board temp : target) {
            dtoTemp = new BoardDTO(temp);
            result.add(dtoTemp);
        }
        return result;
    }

    public BoardDTO boardToBoardDTO(Board target) {
        return new BoardDTO(target);
    }
}