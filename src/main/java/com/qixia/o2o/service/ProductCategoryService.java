package com.qixia.o2o.service;

import com.qixia.o2o.bean.ProductCategory;
import com.qixia.o2o.bean.Shop;
import com.qixia.o2o.dto.ProductCategoryExecution;
import com.qixia.o2o.exceptions.ProductCategotyOperationException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 七夏
 * @create 2020-04-17
 */
public interface ProductCategoryService {

    /**
     * 将此类别下的商品里的类别id设置为空，在删除该商品类别
     */
    ProductCategoryExecution deleteProductCategory(long productCategoryId,long shopId) throws ProductCategotyOperationException;

    /**
     * 获取该shopId的商品类别
     *
     * @param shopId
     * @return
     */
    List<ProductCategory> queryProductCategoryList(long shopId);

    /**
     * 批量增加商品类别
     */
    ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) throws ProductCategotyOperationException;
}
