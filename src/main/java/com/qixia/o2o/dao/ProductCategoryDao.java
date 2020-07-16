package com.qixia.o2o.dao;

import com.qixia.o2o.bean.Product;
import com.qixia.o2o.bean.ProductCategory;
import com.qixia.o2o.bean.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 七夏
 * @create 2020-04-17
 */
public interface ProductCategoryDao {
    /**
     * 删除指定商品类别
     */
    int deleteProductCategory(@Param("productCategoryId")long productCategoryId,@Param("shopId")long shopId);


    /**
     * 获取该shopId中的商品类别
     * @param shopId
     * @return
     */
    List<ProductCategory> queryProductCategoryList(long shopId);

    /**
     * 批量新增商品类别
     * @param productCategoryList
     */
    int batchInsertProductCategory(List<ProductCategory> productCategoryList);
}
