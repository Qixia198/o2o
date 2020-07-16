package com.qixia.o2o.dao;

import com.qixia.o2o.bean.ShopCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 七夏
 * @create 2020-04-16
 */
public interface ShopCategoryDao {
    List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition")ShopCategory shopCategoryCondition);

    /**
     * 获取所有商品类别
     */

}
