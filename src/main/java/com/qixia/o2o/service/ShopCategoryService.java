package com.qixia.o2o.service;

import com.qixia.o2o.bean.ShopCategory;

import java.util.List;

/**
 * @author 七夏
 * @create 2020-04-16
 */
public interface ShopCategoryService {
    /**
     * 根据查询条件获取ShopCategory列表
     * @param shopCategoryCondition
     * @return
     */
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}
