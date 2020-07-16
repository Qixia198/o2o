package com.qixia.o2o.dao;

import com.qixia.o2o.bean.Area;

import java.util.List;

/**
 * @author 七夏
 * @create 2020-04-15
 */
public interface AreaDao {
    /**
     * 获取区域列表
     * @return areaList
     */
    List<Area> queryArea();

}
