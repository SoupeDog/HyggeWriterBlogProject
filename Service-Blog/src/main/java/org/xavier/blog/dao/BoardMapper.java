package org.xavier.blog.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xavier.blog.domain.po.Board;

import java.util.ArrayList;
import java.util.Map;

/**
 * 描述信息：<br/>
 * 板块操作
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/4/5
 * @since Jdk 1.8
 */
@Mapper
public interface BoardMapper {

    /**
     * 持久化板块
     *
     * @param board 目标文章
     * @return 受影响行
     */
    Integer saveBoard(@Param("Board") Board board);

    /**
     * 逻辑删除板块
     *
     * @param boardNoList 板块唯一标识 List
     * @return 受影响行
     */
    Integer removeBoardMultipleByBoardNoLogically(@Param("boardNoList") ArrayList<String> boardNoList, @Param("lastUpdateTs") Long lastUpdateTs);

    /**
     * 修改板块信息
     *
     * @param boardNo 板块唯一标识
     * @param data    修改数据
     * @return 受影响行
     */
    Integer updateBoard(@Param("boardNo") String boardNo, @Param("data") Map data, @Param("lastUpdateTs") Long lastUpdateTs);

    /**
     * 根据 id 批量查询板块信息
     *
     * @param boardNoList boardId List
     * @return 查询结果集
     */
    ArrayList<Board> queryBoardListByBoardNo(@Param("boardNoList") ArrayList<String> boardNoList);

    /**
     * 查询所有板块信息
     */
    ArrayList<Board> queryAllBoardList(@Param("startPoint") Integer startPoint, @Param("size") Integer size, @Param("orderKey") String orderKey, @Param("order") String order);
}