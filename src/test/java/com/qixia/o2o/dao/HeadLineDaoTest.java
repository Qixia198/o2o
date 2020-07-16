package com.qixia.o2o.dao;

import com.qixia.o2o.BaseTest;
import com.qixia.o2o.bean.HeadLine;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author 七夏
 * @create 2020-04-20
 */
public class HeadLineDaoTest extends BaseTest{
    @Autowired
    private HeadLineDao headLineDao;

    @Test
    public void testQueryHeadLine(){
        HeadLine headLine = new HeadLine();
        headLine.setEnableStatus(1);
        List<HeadLine> headLines = headLineDao.queryHeadLine(headLine);
        System.out.println("===================" + headLines.size());
    }
}
