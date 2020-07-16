package com.qixia.o2o.dao;

import com.qixia.o2o.BaseTest;
import com.qixia.o2o.bean.ProductCategory;
import com.qixia.o2o.bean.Shop;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 七夏
 * @create 2020-04-17
 */
public class ProductCategoryDaoTest extends BaseTest{
    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Test
    public void testDeleteProductCategory(){
        long shopId = 1L;
        int count = 0;
        List<ProductCategory> productCategories = productCategoryDao.queryProductCategoryList(shopId);
        for (ProductCategory pc : productCategories) {
            if("商品类别1".equals(pc.getProductCategoryName()) || "商品类别2".equals(pc.getProductCategoryName()) ||
            "123".equals(pc.getProductCategoryName()) || "商品类别11".equals(pc.getProductCategoryName()) || "商品类别10".equals(pc.getProductCategoryName())){
                count = productCategoryDao.deleteProductCategory(pc.getProductCategoryId(), shopId);
            }
        }
        System.out.println("===============" + count);
    }


    @Test
    @Ignore
    public void testBatchInsertProductCategory(){
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryName("商品类别1");
        productCategory.setPriority(1);
        productCategory.setCreateTime(new Date());
        productCategory.setShopId(1L);

        ProductCategory productCategory1 = new ProductCategory();
        productCategory1.setProductCategoryName("商品类别2");
        productCategory1.setPriority(1);
        productCategory1.setCreateTime(new Date());
        productCategory1.setShopId(13L);
        ArrayList<ProductCategory> list = new ArrayList<>();
        list.add(productCategory);
        list.add(productCategory1);
        int count = productCategoryDao.batchInsertProductCategory(list);
        System.out.println("=================" + count);
    }

    @Test
    @Ignore
    public void testQueryProductCategory(){
        List<ProductCategory> productCategories = productCategoryDao.queryProductCategoryList(1L);
        System.out.println(productCategories);
    }
}
