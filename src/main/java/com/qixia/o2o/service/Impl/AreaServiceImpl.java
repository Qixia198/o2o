package com.qixia.o2o.service.Impl;

import com.qixia.o2o.bean.Area;
import com.qixia.o2o.dao.AreaDao;
import com.qixia.o2o.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 七夏
 * @create 2020-04-15
 */
@Service
public class AreaServiceImpl implements AreaService {
    @Autowired
    private AreaDao areaDao;

    @Override
    public List<Area> getAreaList() {
        return areaDao.queryArea();
    }
}
