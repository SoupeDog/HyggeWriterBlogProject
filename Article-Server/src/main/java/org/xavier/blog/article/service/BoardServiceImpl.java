package org.xavier.blog.article.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.article.dao.BoardMapper;
import org.xavier.blog.article.domain.dto.BoardDTO;
import org.xavier.blog.article.domain.po.board.Board;
import org.xavier.blog.article.service.remote.UserServiceImpl;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.common.enums.UserTypeEnum;
import org.xavier.common.enums.ColumnType;
import org.xavier.common.exception.Universal400Exception;
import org.xavier.common.exception.Universal403Exception;
import org.xavier.common.exception.Universal404Exception;
import org.xavier.common.logging.HyggeLoggerMsgBuilder;
import org.xavier.common.util.UtilsCreator;
import org.xavier.common.util.bo.ColumnInfo;
import org.xavier.webtoolkit.base.DefaultUtils;

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
public class BoardServiceImpl extends DefaultUtils {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    BoardMapper boardMapper;

    private static final List<ColumnInfo> checkInfo = new ArrayList<ColumnInfo>() {{
        add(new ColumnInfo("uId", "uId", ColumnType.STRING, false, 9, 10));
        add(new ColumnInfo("boardName", "boardName", ColumnType.STRING, false, 1, 60));
    }};

    /**
     * 添加板块
     */
    public Boolean saveBoard(String operatorUId, Board board, Long currentTs) {
        board.setBoardId(UtilsCreator.getDefaultRandomHelperInstance().getUniversallyUniqueIdentifier());
        board.setuId(operatorUId);
        board.setTs(currentTs);
        board.setLastUpdateTs(currentTs);
        board.setLegal_Flag(true);
        Integer saveBoardAffectedRow = boardMapper.saveBoard(board);
        Boolean saveBoardFlag = saveBoardAffectedRow == 1;
        if (!saveBoardFlag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("saveBoard_EffectedLine", "1", saveBoardAffectedRow, board));
        }
        return saveBoardFlag;
    }


    /**
     * 批量删除板块
     */
    public Boolean removeBoardMultiple(String currentUserUId, ArrayList<String> boardIdList, Long upTs) throws Universal403Exception {
        ArrayList<String> boardIdListForQuery = collectionHelper.filterCollectionNotEmptyAsArrayList(true, boardIdList, "[boardIdList] can't be empty.", String.class, String.class, (x) -> x.trim());
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
                throw new Universal403Exception(ErrorCode.OWNERS_OF_TARGETS_WERE_NOT_UNIQUE.getErrorCod(), "Owners of targets were not unique.");
            }
            boardIdListForRemove.add(temp.getBoardId());
        }
        if (firstUId != null && boardIdListForRemove.size() > 0) {
            userService.checkRight(currentUserUId, UserTypeEnum.ROOT, firstUId);
            Integer removeBoard_affectedLine = boardMapper.removeBoardMultipleByBoardIds_Logically(boardIdListForRemove, upTs);
            removeBoard_Flag = removeBoard_affectedLine == boardIdListForRemove.size();
            if (!removeBoard_Flag) {
                logger.warn(HyggeLoggerMsgBuilder.assertFail("removeBoard_affectedLine", String.valueOf(boardIdListForRemove.size()), removeBoard_affectedLine, new LinkedHashMap() {{
                    put("currentUserUId", currentUserUId);
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
    public Boolean updateBoard(String currentUserUId, String boardId, Map rowData) throws Universal404Exception, Universal403Exception, Universal400Exception {
        propertiesHelper.stringNotNull(boardId, 32, 32, "[boardId] can't be null,and its length should be 32.");
        Board board = quarryBoardByBoardId(boardId);
        if (board == null) {
            throw new Universal404Exception(ErrorCode.STATEMENT_NOTFOUND.getErrorCod(), "Board(" + boardId + ") was not found.");
        }
        userService.checkRight(currentUserUId, UserTypeEnum.ROOT, board.getuId());
        Long upTs = propertiesHelper.longRangeNotNull(rowData.get("ts"), "[ts] can't be null,and it should be a number.");
        HashMap data = sqlHelper.createFinalUpdateDataWithDefaultTsColumn(upTs,rowData, checkInfo);
        if (data.size() < 2) {
            throw new Universal400Exception(ErrorCode.UPDATE_DATA_EMPTY.getErrorCod(), "Effective-Update-Properties can't be empty.");
        }
        Integer updateBoardAffectedRow = boardMapper.updateBoard(boardId, data, upTs);
        Boolean updateBoardFlag = updateBoardAffectedRow == 1;
        if (!updateBoardFlag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("updateBoardAffectedRow", "1", updateBoardAffectedRow, new LinkedHashMap() {{
                put("currentUserUId", currentUserUId);
                put("statementId", boardId);
                put("data", data);
                put("upTs", upTs);
            }}));
        }
        return updateBoardFlag;
    }

    /**
     * 根据版权唯一标识查询板块
     */
    public Board quarryBoardByBoardId(String boardId) {
        propertiesHelper.stringNotNull(boardId, 32, 32, "[boardId] can't be null,and its length should be 32.");
        ArrayList<Board> resultTemp = boardMapper.queryBoardListByBoardIds(new ArrayList<String>() {{
            add(boardId);
        }});
        if (resultTemp == null || resultTemp.size() < 1) {
            return null;
        } else {
            return resultTemp.get(0);
        }
    }

    /**
     * 根据版权唯一标识查询板块
     */
    public Board quarryBoardByBoardId__WithExistValidate(String boardId) throws Universal404Exception {
        Board result = quarryBoardByBoardId(boardId);
        if (result == null) {
            throw new Universal404Exception(ErrorCode.BOARD_NOTFOUND.getErrorCod(), "Board(" + boardId + ") was not found.");
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
            result = boardMapper.queryAllBoardList((currentPage - 1) * pageSize, pageSize, orderKey, "DESC");
        } else {
            result = boardMapper.queryAllBoardList((currentPage - 1) * pageSize, pageSize, orderKey, "ASC");
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