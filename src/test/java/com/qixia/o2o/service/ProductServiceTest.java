package com.qixia.o2o.service;

import com.qixia.o2o.BaseTest;
import com.qixia.o2o.bean.Product;
import com.qixia.o2o.bean.ProductCategory;
import com.qixia.o2o.bean.ProductImg;
import com.qixia.o2o.bean.Shop;
import com.qixia.o2o.dto.ImageHolder;
import com.qixia.o2o.dto.ProductExecution;
import com.qixia.o2o.enums.ProductStateEnum;
import com.qixia.o2o.util.ImageUtil;
import org.apache.ibatis.plugin.Signature;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sun.dc.pr.PRError;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 七夏
 * @create 2020-04-18
 */
public class ProductServiceTest extends BaseTest{
    @Autowired
    private ProductService productService;

    @Test
    public void testModifyProduct() throws FileNotFoundException {
        Product product = new Product();
        ProductCategory pc = new ProductCategory();
        pc.setProductCategoryId(3L);
        Shop shop = new Shop();
        shop.setShopId(1L);
        product.setProductId(2L);
        product.setShop(shop);
        product.setProductCategory(pc);
        product.setProductName("正式的商品1");
        product.setProductDesc("正式的描述11");
        File thumbnailFile = new File("C:\\Users\\七夏\\Pictures\\Saved Pictures\\1da69c3f50268cf.jpg");
        InputStream is = new FileInputStream(thumbnailFile);
        ImageHolder thumbnail = new ImageHolder(thumbnailFile.getName(), is);
        File pro1 = new File("C:\\Users\\七夏\\Pictures\\Saved Pictures\\2016-08-16-184342.jpeg");
        InputStream is1 = new FileInputStream(pro1);
        File pro2 = new File("C:\\Users\\七夏\\Pictures\\Saved Pictures\\596ac9179856b.jpg");
        InputStream is2 = new FileInputStream(pro2);
        LinkedList<ImageHolder> list = new LinkedList<>();
        list.add(new ImageHolder(pro1.getName(), is1));
        list.add(new ImageHolder(pro2.getName(), is2));
        ProductExecution pe = productService.modifyProduct(product, thumbnail, list);
        System.out.println("+++++++++++" + pe.getStateInfo());
    }

    @Test
    public void testAddProduct() throws FileNotFoundException {
        Product product = new Product();
        Shop shop = new Shop();
        shop.setShopId(13L);
        ProductCategory pc = new ProductCategory();
        pc.setProductCategoryId(14L);
        product.setShop(shop);
        product.setPriority(90);
        product.setProductCategory(pc);
        product.setProductName("七夏的商品1");
        product.setProductDesc("七夏的商品1");
        product.setCreateTime(new Date());
        product.setEnableStatus(ProductStateEnum.SUCCESS.getState());
        //创建缩略图文件流
        File file = new File("C:\\Users\\七夏\\Pictures\\v2-44e0ab8eeb595a2d0e3bb39da9b5f130_hd.jpg");
        InputStream is = new FileInputStream(file);
        ImageHolder imageHolder = new ImageHolder(file.getName(), is);
        product.setImgAddr(file.getName());
        //创建两个详情图
        File file1 = new File("C:\\Users\\七夏\\Pictures\\Saved Pictures\\1da69c3f50268cf.jpg");
        InputStream is1 = new FileInputStream(file1);
        File file2 = new File("C:\\Users\\七夏\\Pictures\\Saved Pictures\\-7f5caf51de67407e.jpg");
        InputStream is2 = new FileInputStream(file2);
        ImageHolder imageHolder1 = new ImageHolder(file1.getName(), is1);
        ImageHolder imageHolder2 = new ImageHolder(file2.getName(), is2);

        List<ImageHolder> list = new LinkedList<>();
        list.add(imageHolder1);list.add(imageHolder2);
        ProductExecution pe = productService.addProduct(product, imageHolder, list);
        System.out.println("添加的商品" + pe.getProduct());
        System.out.println("=============" + pe.getStateInfo());


    }

}
