package com.qixia.o2o.dao;

import com.qixia.o2o.bean.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 七夏
 * @create 2020-04-18
 */
public interface ProductDao {
    /**
     * 删除商品类别之前，将商品类别置空
     */
    int updateProductCategoryToNull(long productCategoryId);

    /**
     * 查询某商铺下的商铺总数（可以组合查询）
     */
    int queryProductCount(@Param("productCondition")Product productCondition);

    /**
     * 更新商品信息
     */
    int updateProduct(Product product);


    /**
     * 通过productId查询对应的商品
     * @param productId
     */
    Product queryProductById(long productId);

    /**
     * 查询商品
     * @param productCondition
     * @param pageIndex
     * @param pageSize
     */
    List<Product> queryProductList(@Param("productCondition")Product productCondition,int pageIndex,int pageSize);

    /**
     * 增加商品
     * @param product
     */
    int insertProduct(Product product);



}
