package com.qixia.o2o.dao;

import com.github.pagehelper.PageHelper;
import com.qixia.o2o.BaseTest;
import com.qixia.o2o.bean.Product;
import com.qixia.o2o.bean.ProductCategory;
import com.qixia.o2o.bean.Shop;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @author 七夏
 * @create 2020-04-18
 */
public class ProductDaoTest extends BaseTest {
    @Autowired
    private ProductDao productDao;

    @Test
    public void testQueryProductCount(){
        Product product = new Product();
/*        Shop shop = new Shop();
        shop.setShopId(1L);
        product.setShop(shop);*/
        product.setProductName("测试");
        int count = productDao.queryProductCount(product);
        System.out.println("==============" + count);
    }

    @Test
    public void testQueryProductList(){
        Product product = new Product();
/*        Shop shop = new Shop();
        shop.setShopId(1L);
        product.setShop(shop);*/
        product.setProductName("测试");
        //PageHelper.startPage(1, 100);
        List<Product> products = productDao.queryProductList(product, 0, 0);
        for (Product p : products) {
            System.out.println("=============" + p);
        }
    }


    @Test
    public void testQueryProductById(){
        Product product = productDao.queryProductById(2L);
        System.out.println("===============" + product.getProductName());
    }

    @Test
    public void testUpdateProduct(){
        Product product = new Product();
        ProductCategory pc = new ProductCategory();
        Shop shop = new Shop();
        shop.setShopId(1L);
        product.setProductId(2L);
        pc.setProductCategoryId(3L);
        product.setShop(shop);
        product.setProductCategory(pc);
        product.setProductName("test1");
        product.setPriority(15);
        int i = productDao.updateProduct(product);
        System.out.println("++++++++++" + i);
    }

    @Test
    public void testInsertProduct(){
        Shop shop = new Shop();
        shop.setShopId(1L);
        ProductCategory pc = new ProductCategory();
        pc.setProductCategoryId(3L);
        Product product = new Product();
        product.setProductName("测试1");
        product.setProductDesc("测试Desc1");
        product.setImgAddr("test1");
        product.setPriority(1);
        product.setEnableStatus(1);
        product.setCreateTime(new Date());
        product.setLastEditTime(new Date());
        product.setProductCategory(pc);
        product.setShop(shop);

        Product product1 = new Product();
        product1.setProductName("测试2");
        product1.setProductDesc("测试Desc2");
        product1.setImgAddr("test2");
        product1.setPriority(2);
        product1.setEnableStatus(0);
        product1.setCreateTime(new Date());
        product1.setLastEditTime(new Date());
        product1.setProductCategory(pc);
        product1.setShop(shop);

        Product product2 = new Product();
        product2.setProductName("test3");
        product2.setProductDesc("测试Desc3");
        product2.setImgAddr("test3");
        product2.setPriority(3);
        product2.setEnableStatus(1);
        product2.setCreateTime(new Date());
        product2.setLastEditTime(new Date());
        product2.setProductCategory(pc);
        product2.setShop(shop);

        int effected1 = productDao.insertProduct(product);
        int effected2 = productDao.insertProduct(product1);
        int effected3 = productDao.insertProduct(product2);
        System.out.println("=================1" + effected1);
        System.out.println("=================2" + effected2);
        System.out.println("=================3" + effected3);

    }
}
