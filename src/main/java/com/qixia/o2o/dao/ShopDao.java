package com.qixia.o2o.dao;

import com.qixia.o2o.bean.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 七夏
 * @create 2020-04-15
 */
public interface ShopDao {
    /**
     * 分页查询店铺，可输入的条件有：店铺名（模糊），店铺状态，区域id，owner
     */
    List<Shop> queryShopList(@Param("shopCondition")Shop shopCondition,
                             @Param("rowIndex")int rowIndex,@Param("pageSize")int pageSize);

    /**
     * 返回queryShopList的总数
     */
    int queryShopCount(@Param("shopCondition")Shop shopCondition);

    /**
     * 通过shop id查询店铺
     */
    Shop queryByShopId(long shopId);

    /**
     * 新增店铺
     * @param shop
     * @return
     */
    int insertShop(Shop shop);

    /**
     * 更新店铺信息
     * @param shop
     * @return
     */
    int updateShop(Shop shop);

}
