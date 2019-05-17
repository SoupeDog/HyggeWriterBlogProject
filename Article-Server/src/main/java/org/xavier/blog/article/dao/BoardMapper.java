package org.xavier.blog.article.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xavier.blog.article.domain.po.board.Board;

import java.util.ArrayList;
import java.util.Map;

/**
 * 描述信息：<br/>
 * 板块 DAO
 *
 * @author Xavier
 * @version 1.0
 * @date 2017/12/7
 * @since Jdk 1.8
 */
@Mapper
public interface BoardMapper {
    /**
     * 创建板块
     *
     * @param board 板块实体
     * @return 受影响行
     */
    Integer saveBoard(@Param("board") Board board);

    /**
     * 逻辑删除板块
     *
     * @param boardIdList 板块唯一标识 List
     * @return 受影响行
     */
    Integer removeBoardMultipleByBoardIds_Logically(@Param("boardIdList") ArrayList<String> boardIdList, @Param("lastUpdateTs") Long lastUpdateTs);

    /**
     * 修改板块信息
     *
     * @param boardId 板块唯一标识
     * @param data    修改数据
     * @return 受影响行
     */
    Integer updateBoard(@Param("boardId") String boardId, @Param("data") Map data, @Param("lastUpdateTs_CAS") Long lastUpdateTs_CAS);

    /**
     * 根据 id 批量查询板块信息
     *
     * @param boardIdList boardId List
     * @return 查询结果集
     */
    ArrayList<Board> queryBoardListByBoardIds(@Param("boardIdList") ArrayList<String> boardIdList);

    /**
     * 查询所有板块信息
     */
    ArrayList<Board> queryAllBoardList(@Param("startPoint") Integer startPoint, @Param("size") Integer size, @Param("orderKey") String orderKey, @Param("order") String order);
}