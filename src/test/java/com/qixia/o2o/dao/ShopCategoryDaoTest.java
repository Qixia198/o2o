package com.qixia.o2o.dao;

import com.qixia.o2o.BaseTest;
import com.qixia.o2o.bean.ShopCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author 七夏
 * @create 2020-04-16
 */
public class ShopCategoryDaoTest extends BaseTest{
    @Autowired
    private ShopCategoryDao shopCategoryDao;

    @Test
    public void testQueryShopCategory(){
        List<ShopCategory> shopCategoryList =  shopCategoryDao.queryShopCategory(new ShopCategory());
        System.out.println(shopCategoryList);
        ShopCategory testCategory = new ShopCategory();
        ShopCategory parentCategory = new ShopCategory();
        parentCategory.setShopCategoryId(1L);
        testCategory.setParent(parentCategory);
        shopCategoryList = shopCategoryDao.queryShopCategory(testCategory);
        System.out.println(shopCategoryList);
    }

}