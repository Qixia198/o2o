package com.qixia.o2o.dao;

import com.qixia.o2o.bean.HeadLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 七夏
 * @create 2020-04-20
 */
public interface HeadLineDao {

    /**
     * 根据传入的查询条件（头条名查询条件）
     */
    List<HeadLine> queryHeadLine(@Param("headLineCondition")HeadLine headLineCondition);

}
