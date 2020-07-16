package com.qixia.o2o.service;

import com.qixia.o2o.bean.Product;
import com.qixia.o2o.dto.ImageHolder;
import com.qixia.o2o.dto.ProductExecution;
import com.qixia.o2o.exceptions.ProductOperationException;

import java.io.InputStream;
import java.util.List;

/**
 * @author 七夏
 * @create 2020-04-18
 */
public interface ProductService {

    /**
     * 查询某店铺下的所有商品
     */
    ProductExecution getProductList(Product product);

    /**
     * 更新商品信息
     */
    ProductExecution modifyProduct(Product product, ImageHolder thumbnail,List<ImageHolder> productImgList) throws ProductOperationException;

    /**
     * 根据productId查询商品
     */
    Product getProductById(long productId);


    /**
     * 添加商品信息以及图片处理
     */
    ProductExecution addProduct(Product product, ImageHolder thumbnail,List<ImageHolder> productImgList) throws ProductOperationException;

}
