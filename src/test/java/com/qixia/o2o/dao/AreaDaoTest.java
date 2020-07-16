package com.qixia.o2o.dao;

import com.qixia.o2o.BaseTest;
import com.qixia.o2o.bean.Area;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author 七夏
 * @create 2020-04-15
 */
public class AreaDaoTest extends BaseTest {
    @Autowired
    private AreaDao areaDao;

    @Test
    public void testQueryArea(){
        List<Area> areas = areaDao.queryArea();
        System.out.println(areas);
    }

}
