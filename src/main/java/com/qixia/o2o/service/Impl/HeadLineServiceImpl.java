package com.qixia.o2o.service.Impl;

import com.qixia.o2o.bean.HeadLine;
import com.qixia.o2o.dao.HeadLineDao;
import com.qixia.o2o.service.HeadLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author 七夏
 * @create 2020-04-20
 */
@Service
public class HeadLineServiceImpl implements HeadLineService {
    @Autowired
    private HeadLineDao headLineDao;

    @Override
    public List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException {
        List<HeadLine> headLines = headLineDao.queryHeadLine(headLineCondition);
        return headLines;
    }
}
