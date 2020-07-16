package com.qixia.o2o.dao;

import com.qixia.o2o.bean.ProductImg;

import java.util.List;

/**
 * @author 七夏
 * @create 2020-04-18
 */
public interface ProductImgDao {

    /**
     * 查询返回指定id的商品详情图
     * @param productId
     * @return
     */
    List<ProductImg> queryProductImgList(long productId);

    /**
     * 批量添加商品图片
     * @param productImgList
     * @return
     */
    int batchInsertProductImg(List<ProductImg> productImgList);

    /**
     * 删除指定的id的商品详情图
     * @param productId
     * @return
     */
    int deleteProductImgByProductId(long productId);

}
