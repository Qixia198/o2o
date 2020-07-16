package com.qixia.o2o.service;

import com.qixia.o2o.bean.HeadLine;

import java.io.IOException;
import java.util.List;

/**
 * @author 七夏
 * @create 2020-04-20
 */
public interface HeadLineService {

    /**
     * 根据传入的条件返回指定的头条列表
     */
    List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException;

}
