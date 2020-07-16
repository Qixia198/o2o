package com.qixia.o2o.service;

import com.qixia.o2o.bean.Shop;
import com.qixia.o2o.dto.ImageHolder;
import com.qixia.o2o.dto.ShopExecution;
import com.qixia.o2o.exceptions.ShopOperationException;


import java.io.InputStream;

/**
 * @author 七夏
 * @create 2020-04-15
 */
public interface ShopService {
    /**
     * 根据shopCondition分页返回对应店铺列表数据
     */
    ShopExecution getShopList(Shop shopCondition,int pageIndex,int pageSize);


    /**
     * 通过shopId查询店铺
     * @param shopId
     * @return
     */
    Shop getShopById(long shopId);

    /**
     * 更新店铺信息，包括图片的处理
     */
    ShopExecution modifyShop(Shop shop,ImageHolder thumbnail) throws ShopOperationException;



    /**
     * 添加商铺
     * @param shop
     */
    ShopExecution addShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException;

}
