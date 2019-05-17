package org.xavier.blog.article.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.article.dao.BoardMapper;
import org.xavier.blog.article.domain.po.board.Board;
import org.xavier.common.enums.ColumnType;
import org.xavier.common.logging.HyggeLoggerMsgBuilder;
import org.xavier.common.utils.UtilsCreator;
import org.xavier.common.utils.bo.ColumnInfo;
import org.xavier.web.extend.DefaultService;

import java.util.ArrayList;
import java.util.List;

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
    BoardMapper boardMapper;

    private static final List<ColumnInfo> checkInfo = new ArrayList<ColumnInfo>() {{
        add(new ColumnInfo(ColumnType.STRING, "content", "content", false, 1, 300));
        add(new ColumnInfo(ColumnType.STRING, "properties", "properties", true, 0, 1000));
    }};

    /**
     * 添加版权声明
     */
    public Boolean saveBoard(String operatorUId, Board board, Long currentTs) {
        board.setBoardId(UtilsCreator.getInstance_DefaultRandomHelper().getUUID());
        board.setuId(operatorUId);
        board.setTs(currentTs);
        board.setLastUpdateTs(currentTs);
        board.setLegal_Flag(true);
        Integer saveBoard_affectedLine = boardMapper.saveBoard(board);
        Boolean saveStatement_Flag = saveBoard_affectedLine == 1;
        if (!saveStatement_Flag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("saveBoard_EffectedLine", "1", saveBoard_affectedLine, board));
        }
        return saveStatement_Flag;
    }
}