package com.qixia.o2o.service;

import com.qixia.o2o.BaseTest;
import com.qixia.o2o.bean.Area;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author 七夏
 * @create 2020-04-15
 */
public class AreaServiceTest extends BaseTest {
    @Autowired
    private AreaService areaService;

    @Test
    public void testGetAreaList(){
        List<Area> areaList = areaService.getAreaList();
        System.out.println(areaList);
    }


}
